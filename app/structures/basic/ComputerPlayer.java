package structures.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.PerformAction;
import akka.actor.ActorRef;
import commands.BasicCommands;
import events.CardClicked;
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

		// Logic to decide what action to perform can be written here
		
		checkHand();//checking the cards in the hand
		checkUnitTiles(out,gameState);
		
		Tile tileTomove=new Tile();
		tileTomove.setTilex(4);
		tileTomove.setTiley(2);
		


		// Check if avatar is in range of any enemy unit - SS
		ArrayList<Tile> dangerTiles=checkIfUnitInDanger(currentTile,out,gameState);
		
		if(dangerTiles.size()>0)
		{
			AppConstants.printLog("<-------- AI :: startAILogic():: Avatar is in danger ! ");
			
			// Avatar is in danger, need to move away or attack the enemy
			
			// If the player avatar is nearby and it's health <= AI attack value, attack and thus game over
			if(dangerTiles.contains(gameState.player1.getCurrentTile()))
			{

				// Test
//				gameState.player1.getAvatar().setHealth(2);
//				gameState.player1.setHealth(2);
				
				if(gameState.player1.getAvatar().getHealth()<=getAvatar().getAttack())
				{
					// Player's health is <= AI attack value, attack
					// If attack is finalized, and move and atack or direct attacks
					attackAIUnit(out, gameState, currentTile, gameState.player1.getCurrentTile());
				}
			}else {
				// Otherwise,try defensive way, move backward
				AppConstants.printLog("<-------- AI :: startAILogic():: Fine possible moves! ");

				// Get list of possible backward moves respect to current avatar tile position
				possibleMoves("backward",currentTile,out,gameState);
				
			}		
			
		}else {
			AppConstants.printLog("<-------- AI :: startAILogic():: Avatar is NOT in danger ! ");

			// Avatar is safe from direct or indirect unit attacks. Can proceed with other units
		}
		

//		Boolean movesLeft=true;
//		if(movesLeft){
//			movesLeft=listPossibleMove(out,gameState);
//		}
		

		
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
	public void checkHand(){
//		cardInHand=hand; --> similar as hand
		for (Card card : hand) {
			System.out.println("card in AI's hand: "+ card.getCardname()+ " with Mana cost: "+ card.getManacost());
		}
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
	 * @param verticalDirection --> 0- no change, 1 - forward, -1 -  backward
	 * @param horizontalDirection --> 0- no change, 1-  upward, -1 - downward
	 * @param out
	 * @param gameState
	 */
	public ArrayList<Tile> possibleMoves(String code, Tile tile, ActorRef out, GameState gameState) {
		//possible moves if the unit has not moved or attacked
		
		ArrayList <Tile> possibleTilesForMove = gameState.board.highlightTilesMoveAndAttack(0, gameState.player2, out, tile, gameState);
		
		// Get possible moves w.r.t horizontal direction
		if(code.equals("backward") || code.equals("forward"))
		{
		for (Tile tileToMove : possibleTilesForMove) {
				if(tileToMove.getUnitFromTile()== null ){
					if(code.equals("backward")) { // get backward tiles to move
						if(tileToMove.getTilex()>tile.getTilex())
							possibleTilesForMove.add(tileToMove);
					}else if(code.equals("forward")) { // get forward tiles to move
						if(tileToMove.getTilex()<tile.getTilex())
							possibleTilesForMove.add(tileToMove);
					}else {
						possibleTilesForMove.add(tileToMove);

					}
					}
			}
		}else // Get possible moves w.r.t vertical direction
			if(code.equals("upward") || code.equals("downward"))
			{
			for (Tile tileToMove : possibleTilesForMove) {
					if(tileToMove.getUnitFromTile()== null ){
						if(code.equals("downward")) { // get downward tiles to move
							if(tileToMove.getTiley()>tile.getTiley())
								possibleTilesForMove.add(tileToMove);
						}else if(code.equals("upward")) { // get forward tiles to move
							if(tileToMove.getTiley()<tile.getTiley())
								possibleTilesForMove.add(tileToMove);
						}else {
							possibleTilesForMove.add(tileToMove);

						}
						}
				}
			}
		return possibleTilesForMove;	
		}
	
	
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
	
	
}
