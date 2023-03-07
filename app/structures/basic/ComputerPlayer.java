package structures.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.PerformAction;
import akka.actor.ActorRef;
import commands.BasicCommands;
import events.CardClicked;
import events.EndTurnClicked;
import events.TileClicked;
import play.libs.Json;
import structures.GameState;
import utils.AppConstants;

/** creating a computerPlayer class so we can add extra functionalities down the line
 * 
 *
 */
public class ComputerPlayer extends Player{

//	List <Card> cardInHand;//to keep track of card in the hand --> seems similar as 'hand'?
	List <Tile> tileWithMyUnit;//to keep track of tiles occupied by AI units
	List <Tile> tileWithPlayerUnits;//to keep track of human player units
	List <Tile> possibleSummonList;

	Tile avatarTile;
	
	boolean movesEnd=false,cardsDrawEnd=false;
    /** constructor to create a player with set health and mana which calls 
     * setPlayer to place the data on the front end.
     * 
     * @param playerID
     * @param out
     * @param avatar
     * @param deck2Cards
     */
    public ComputerPlayer(int playerID, ActorRef out, BetterUnit avatar, String[] unitFiles) {
        super(playerID, out, avatar, unitFiles);
       
    }
    
  
	public void startAILogic(ActorRef out, GameState gameState) {
		AppConstants.printLog("<-------------------------------------------------------------------------------->");

		cardsDrawEnd=false;
		movesEnd=false;
		// Logic to decide what action to perform can be written here
		
		
		boolean isContinue=true; //boolean to keep track of how long loop should continue
		
		while(isContinue==true)
		{
			checkUnitTiles(out,gameState);

			
			// Check if avatar is in range of any enemy unit - SS
			ArrayList<Tile> dangerTiles=checkIfUnitInDanger(currentTile,out,gameState);
			
			if(dangerTiles.size()>0)
			{
				AppConstants.printLog("<-------- AI :: startAILogic():: Avatar is in danger ! ");
				
				// Avatar is in danger, need to move away or attack the enemy
				
				// If the player avatar is nearby and it's health <= AI attack value, attack and thus game over
				if(dangerTiles.contains(gameState.player1.getCurrentTile()))
				{
					
					if(gameState.player1.getAvatar().getHealth()<=getAvatar().getAttack())
					{
						// Player's health is <= AI attack value, attack
						// If attack is finalized, and move and atack or direct attacks
						attackAIUnit(out, gameState, currentTile, gameState.player1.getCurrentTile());
					}
				}else {
					// Otherwise,try defensive way, move backward
					AppConstants.printLog("<-------- AI :: startAILogic():: Find possible moves! ");

					// Get list of possible backward moves respect to current avatar tile position
//					ArrayList<Tile> possibleTilesToMove=possibleMoves("backward",currentTile,out,gameState);
					

				}		
				
			}else {
				AppConstants.printLog("<-------- AI :: startAILogic():: Avatar is NOT in danger ! ");

				// Avatar is safe from direct or indirect unit attacks. Can proceed with other units or cards
				
				if(tileWithMyUnit.size()<2 ) // No units are summoned yet 
				{
					AppConstants.printLog("<-------- AI :: startAILogic():: Summon a unit !");
					drawCardAndProcessAction(1,out,gameState);
					
				}else if(cardsDrawEnd==false) // Can draw card again 
				{
					AppConstants.printLog("<-------- AI :: startAILogic():: Summon a unit !");
					
					// If number of player units is more than 5, cast spell or summon unit 
					if(playerUnits.size()>5)
						drawCardAndProcessAction(0,out,gameState);
					else
						drawCardAndProcessAction(1,out,gameState);

					
				}
				
			}
			
			// No moves left and can't draw cards further, exit loop and end turn
//			if(cardsDrawEnd==true && movesEnd==true) --> moves are not implememted yet
			
				if(cardsDrawEnd==true) // to test
					isContinue=false;

		}
		
		
		// End turn
		ObjectNode eventMessage = Json.newObject();
		eventMessage.put("messagetype", "endturnclicked");
		

		EndTurnClicked ec=new EndTurnClicked();
		ec.processEvent(out, gameState, eventMessage); 

		
//		Boolean movesLeft=true;
//		if(movesLeft){
//			movesLeft=listPossibleMove(out,gameState);
//		}
		

		
	}
	
	
	private void drawCardAndProcessAction(int mode, ActorRef out, GameState gameState) {
		AppConstants.printLog("<-------- AI :: drawCardAndProcessAction():: Mana : "+getMana());

		// check cards and summon unit
		int handIdxToUse=checkHand(mode);//checking the cards in the hand (mode==1 --> retrieve index of card with only units)
		AppConstants.printLog("<-------- AI :: startAILogic():: handIdxToUse : "+handIdxToUse);

		if(handIdxToUse>-1)
		{
			// We have a unit to summon, now find for a possible tile to summon
			Tile tileToSummon=findAtileToSummon(currentTile,out,gameState);
			
			if(tileToSummon!=null)
			{
				// We have got a tile to summon and a hand index to draw
				drawCardAI(handIdxToUse+1,out,gameState,currentTile,tileToSummon);


			}else {
				cardsDrawEnd=true;
			}
			
		}else {
			cardsDrawEnd=true; //Cannot draw further cards in this turn
		}
	}


	/** Method finds a tile to summon a unit on board
	 * 
	 * @param currentTile
	 * @param gameState 
	 * @param out 
	 * @return
	 */

	private Tile findAtileToSummon(Tile currentTile, ActorRef out, GameState gameState) {
		
		Tile tileToSummon = null;
		
		possibleSummonList= PerformAction.getSummonableTiles(out, gameState, gameState.player2);
		//gameState.board.highlightTilesRed(out, (ArrayList<Tile>) possibleSummonList);

		for(Tile tile:possibleSummonList)
		{
			AppConstants.printLog("<-------- AI :: startAILogic():: findAtileToSummon : tile: ["+tile.getTilex()+","+tile.getTiley()+"]");
			
			if(tile.getUnitFromTile()==null)
			{
				tileToSummon=tile;
				break;
			}

		}
		
		// All summonable tiles are already occupied
		if(tileToSummon==null)
		{
			// Summon unit in any vacant forward tile
			  for (int i = 4; i < gameState.board.tiles.length; i++) {
		            for (int j = 0; j < gameState.board.tiles[i].length; j++) {
		            	
//		               gameState.board.drawTileWithSleep(out, gameState.board.returnTile(i, j), 2, AppConstants.drawTileSleepTime);
		            	if(gameState.board.returnTile(i, j).getUnitFromTile()==null)
		    			{
		            		gameState.board.returnTile(i, j);
		            		break;
		            	}		            	
		            }
		     }
			
		}

		
		return tileToSummon;
	}


	private void drawCard(ActorRef out, GameState gameState) {


		// ------------ Logic to check and finalize draw card ----------
		
		AppConstants.printLog("<-------- AI :: drawCard():: hand size: "+hand.size());
		
		for(Card card:hand)
			AppConstants.printLog("<-------- AI :: drawCard():: hand card: "+card.getCardname());

		Tile tileToSummon=new Tile(); // The tile to summon 

		int handIdx=1;
		if(hand.size()<4)
		{
	        BasicCommands.addPlayer1Notification(out, "AI summoning a unit [7,1] test ", 2);

		// To test unit summon
			tileToSummon.tilex=7;
			tileToSummon.tiley=1;
		}else if(hand.size()==5)
		{
	        BasicCommands.addPlayer1Notification(out, "AI summoning a unit [6,3] test ", 2);

			handIdx=1;
			// To test unit summon
			tileToSummon.tilex=5;
			tileToSummon.tiley=3;
		}else {
	        BasicCommands.addPlayer1Notification(out, "AI summoning a unit [3,2] test ", 2);

			handIdx=3;
			// To test unit summon
			tileToSummon.tilex=6;
			tileToSummon.tiley=3;
		}
		
		
		drawCardAI(handIdx,out,gameState,currentTile,tileToSummon);
		
//		checkMovement(out,gameState);
		
		// checkAttack(out,gameState);
	}

	//method the check the cards in the hand
	//use map or dict to store these in order to utilize later when deciding which card to summon
	
	/** Method to check cards and return the first match handindex to use
	 * 
	 * @param mode -> 0 - unit/spell, 1- only unit, 2- only spell
	 */
	public int checkHand(int mode){
//		cardInHand=hand; --> similar as hand
		for (int i=0;i<hand.size();i++) {

			Card c=hand.get(i);
			
			if(mode==1) // can be only unit
			{
				if(c.getManacost()<=getMana()) //  check mana
				{
					return i; // return index
				}
			}else if(mode==2) { // can be only spell
				if(c.getId()==4 || c.getId()==8 || c.getId()==14 || c.getId()==18 || c.getId()==22 || c.getId()==27 || c.getId()==32 || c.getId()==37)
				{
					if(c.getManacost()<=getMana())
					{
						return i;
					}
				}
			}else { // can be either unit or spell
				if(c.getManacost()<=getMana()) //  check mana
				{
					return i; // return index
				}
			}
			
		}
		return -1;
	}

	// method to get the tiles with the units on the board
	public void checkUnitTiles(ActorRef out,GameState gameState) {
		//AI unit's tile
		tileWithMyUnit=gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player2);
		for (Tile tile : tileWithMyUnit) {
			if(tile.getUnitFromTile().getId()==41) System.out.println("Tiles with AI units: "+tile.getTilex()+" "+ tile.getTiley()+ " with unit AI_Aviatar and id: " + tile.getUnitFromTile().getId());
			else System.out.println("Tiles with AI units: "+tile.getTilex()+" "+ tile.getTiley()+ " with unit: "+ tile.getUnitFromTile().getName()+ " and id: " + tile.getUnitFromTile().getId());
		}
		//player unit's tile
		tileWithPlayerUnits = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player1);
		for (Tile tile : tileWithPlayerUnits) {
			if(tile.getUnitFromTile().getId()==40) System.out.println("Tiles with Player units: "+tile.getTilex()+" "+ tile.getTiley()+ " with unit Human_Avatar and id: " + tile.getUnitFromTile().getId());
			else System.out.println("Tiles with Player units: "+tile.getTilex()+" "+ tile.getTiley()+ " with unit: "+ tile.getUnitFromTile().getName()+ " and id: " + tile.getUnitFromTile().getId());
		}
	}

	

	private void checkAttack(ActorRef out, GameState gameState) {
		
        BasicCommands.addPlayer1Notification(out, "AI attack", 2);

		Tile tileToAttack=new Tile(); // The tile to move 
		
		//-------------- Logic to identify a tile to move---------
		
		
		// To test direct attack
//		tileToAttack.tilex=7;
//		tileToAttack.tiley=1;
		
		//To test move and attack
		tileToAttack.tilex=5;
		tileToAttack.tiley=4;
				
		// If attack is finalized, and move and atack or direct attacks
		attackAIUnit(out, gameState, currentTile, tileToAttack);
		
		
	}

	/** This method will check for an optimal movement to perform and updates front end
	 * 
	 * @param out
	 * @param gameState
	 */
	private void checkMovement(ActorRef out, GameState gameState) {

        BasicCommands.addPlayer1Notification(out, "AI movement", 2);

		Tile tileToMove=new Tile(); // The tile to move 
		
		//-------------- Logic to identify a tile to move---------
		
		
		// To test
		tileToMove.tilex=5;
		tileToMove.tiley=2;
		
		// If movement is finalized, and move 
		moveAIUnit(out, gameState, currentTile, tileToMove);
	
	}
	
	/** Methods checks is a unit is surrounded by enemy units or not
	 * If enemy units are in range, it will return the list of adjacent enemy unit tile
	 * otherwise, the list will be empty
	 * @param tile
	 * @param out
	 * @param gameState
	 * @return
	 */

	private ArrayList<Tile> checkIfUnitInDanger(Tile tile, ActorRef out, GameState gameState) {
		// TODO Auto-generated method stub
		
		ArrayList<Tile> enemyDangerTiles=new ArrayList<>();
		
		// Get adjacent tile of tile to move and attack
		ArrayList<Tile> adjacentTilesMoveAttack=gameState.board.highlightTilesMoveAndAttack(0,gameState.player2,out,tile,gameState);
		
		// Check for enemy units in the adjacentTilesMoveAttack
		for(Tile enemyTile:tileWithPlayerUnits)
		{
			// If enemy units are present, return true
			if(adjacentTilesMoveAttack.contains(enemyTile))
				enemyDangerTiles.add(enemyTile);
		}
		
		
		return enemyDangerTiles;
	}

	/**
	 * This method will programmatically perform move for a player 2's unit 
	 * @param out
	 * @param gameState
	 */
	private void moveAIUnit(ActorRef out, GameState gameState,Tile startTile,Tile tileToMove) {
		ObjectNode eventMessage = Json.newObject();
		eventMessage.put("messagetype", "tileclicked");
		eventMessage.put("start_tilex",""+startTile.getTilex());
		eventMessage.put("start_tiley", ""+startTile.getTiley());
		eventMessage.put("tilex",""+tileToMove.getTilex());
		eventMessage.put("tiley", ""+tileToMove.getTiley());
		eventMessage.put("action", AppConstants.move);

		TileClicked tc=new TileClicked();
		tc.processEvent(out, gameState, eventMessage); // send it to the Tileclicked event processor

	}

	/**
	 * This method will programmatically perform move for a player 2's unit 
	 * @param out
	 * @param gameState
	 */
	private void attackAIUnit(ActorRef out, GameState gameState,Tile startTile,Tile tileToAttack) {
		ObjectNode eventMessage = Json.newObject();
		eventMessage.put("messagetype", "tileclicked");
		eventMessage.put("start_tilex",""+startTile.getTilex());
		eventMessage.put("start_tiley", ""+startTile.getTiley());
		eventMessage.put("tilex",""+tileToAttack.getTilex());
		eventMessage.put("tiley", ""+tileToAttack.getTiley());
		eventMessage.put("action", AppConstants.attack);

		TileClicked tc=new TileClicked();
		tc.processEvent(out, gameState, eventMessage); // send it to the Tileclicked event processor

	}

	
	private void drawCardAI(int handIdx, ActorRef out, GameState gameState,Tile startTile,Tile tileToSummon) {
		ObjectNode eventMessage = Json.newObject();
		eventMessage.put("messagetype", "cardclicked");
		eventMessage.put("position",""+handIdx);
		eventMessage.put("start_tilex",""+startTile.getTilex());
		eventMessage.put("start_tiley", ""+startTile.getTiley());
		eventMessage.put("tilex",""+tileToSummon.getTilex());
		eventMessage.put("tiley", ""+tileToSummon.getTiley());
		eventMessage.put("action", AppConstants.drawCardSummon);


		TileClicked tc=new TileClicked();
		tc.processEvent(out, gameState, eventMessage); // send it to the Tileclicked event processor
	}
	

	/**
	 * This method will give a list of possible moves like summon or move/attack
	 * @param out
	 * @param gameState
	 */
	public Boolean listPossibleMove(ActorRef out, GameState gameState) {
		//need to list all possible moves for the AI player
		Boolean done =false;//boolean to send back
//		possibleSummon(out,gameState);
		possibleMoveAttack(out,gameState);
		return done;
	}

	/**
	 * This method will give possible summon cards and a list of tiles on which it can be summoned
	 * @param out
	 * @param gameState
	 */
	public void possibleSummon(ActorRef out, GameState gameState){
		int handindex=-1;
		//first check which cards can be played
		for (Card card : hand) {
			handindex =hand.indexOf(card)+1;
			//check if mana cost of card is more than the mana of the AI
			if(getMana()>=card.getManacost()){
				System.out.println("card with hand position: "+ handindex+" name: "+ card.getCardname()+" can be played");
				//get summonable tiles
				possibleSummonList= PerformAction.getSummonableTiles(out, gameState, gameState.player2);
				Tile mostForwardTile= new Tile();
				for (Tile tile : possibleSummonList) {
					// System.out.println("Possible summon tiles: ["+ tile.getTilex()+","+tile.getTiley()+"]");
					int maxTileX=9;
					int maxTileY=9;
					if(tile.getTilex()<=maxTileX){
						if(tile.getTiley()<=maxTileY) mostForwardTile=tile;
					}
				}
				System.out.println("Most forward Tile: ["+ mostForwardTile.getTilex()+","+mostForwardTile.getTiley()+"]");
			}
		}
	}


	/**
	 * This method will give a list of possible moves for all the AI units
	 * @param verticalDirection --> "downward" or "upward"
	 * @param horizontalDirection --> "backward" or "forward"
	 * @param out
	 * @param gameState
	 */
//	public ArrayList<Tile> possibleMoves(String code, Tile tile, ActorRef out, GameState gameState) {
//		//possible moves if the unit has not moved or attacked
//		
//		ArrayList <Tile> possibleTilesForMove = gameState.board.highlightTilesMoveAndAttack(0, gameState.player2, out, tile, gameState);
//		AppConstants.printLog("<-------- AI :: drawCard():: possibleTilesForMove size : "+possibleTilesForMove.size());
//
//		// Get possible moves w.r.t horizontal direction
//		if(code.equals("backward") || code.equals("forward"))
//		{
//		for (Tile tileToMove : possibleTilesForMove) {
//				if(tileToMove.getUnitFromTile()== null ){
//					if(code.equals("backward")) { // get backward tiles to move
//						if(tileToMove.getTilex()>tile.getTilex())
//							possibleTilesForMove.add(tileToMove);
//					}else if(code.equals("forward")) { // get forward tiles to move
//						if(tileToMove.getTilex()<tile.getTilex())
//							possibleTilesForMove.add(tileToMove);
//					}else {
//						possibleTilesForMove.add(tileToMove);
//
//					}
//					}
//			}
//		}else // Get possible moves w.r.t vertical direction
//			if(code.equals("upward") || code.equals("downward"))
//			{
//			for (Tile tileToMove : possibleTilesForMove) {
//					if(tileToMove.getUnitFromTile()== null ){
//						if(code.equals("downward")) { // get downward tiles to move
//							if(tileToMove.getTiley()>tile.getTiley())
//								possibleTilesForMove.add(tileToMove);
//						}else if(code.equals("upward")) { // get forward tiles to move
//							if(tileToMove.getTiley()<tile.getTiley())
//								possibleTilesForMove.add(tileToMove);
//						}else {
//							possibleTilesForMove.add(tileToMove);
//
//						}
//						}
//				}
//			}
//		return possibleTilesForMove;	
//		}
	
	
	/**
	 * This method will give a list of possible moves/attack for all the AI units
	 * @param out
	 * @param gameState
	 */
	public void possibleMoveAttack(ActorRef out, GameState gameState) {
		//possible moves if the unit has not moved or attacked
		for (Tile tile : tileWithMyUnit) {
			System.out.println("Tile with my unit: "+ tile.toString());
			if(tile.getUnitFromTile().getId()==41){//for AI avatar
				System.out.println("Unit: "+tile.getUnitFromTile().getName()+" with id: "+tile.getUnitFromTile().getId()+" has not attacked or moved");
				System.out.println("need to defend Avatar");
				//add method to move the avatar around to defend to be done
				

				
				
			}
			else{//for other units of AI
				List <Tile> possibleTilesForMove = gameState.board.highlightTilesMoveAndAttack(0, gameState.player2, out, tile, gameState);
				for (Tile tileToMove : possibleTilesForMove) {
					if(tileToMove.getUnitFromTile()!= null && (tileToMove.getUnitFromTile().getId()<20 || tileToMove.getUnitFromTile().getId()==40)){
						System.out.println("Possible moves: Attack for unit: "+tile.getUnitFromTile().getName() + " to tile: "+tileToMove.toString());		
					}
					System.out.println("Possible moves: Move for unit: "+tile.getUnitFromTile().getName() + " to tile: "+tileToMove.toString());	
				}
			}
		}
	}
	
	/** Method returns a random number between min and max
	 * 
	 * @param Min
	 * @param Max
	 * @return
	 */
	public static int random_int(int Min, int Max)
	{
	     return (int) (Math.random()*(Max-Min))+Min;
	}
	
	
}
