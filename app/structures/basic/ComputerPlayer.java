package structures.basic;

import java.util.ArrayList;
import java.util.HashMap;
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
	List <Tile> possibleSummonList;//to keep track of possible tiles for summoning
	List <Tile> possibleAttackableList = new ArrayList<Tile>();//to keep track of possible attackable tile
	List <Tile> possibleMovableList=new ArrayList<Tile>();// a list of possible moves for units
	Map<Unit,List<Tile>> bestMoveTile = new HashMap<Unit,List<Tile>>();//to decide which unit has the best move avaible
	Map<Unit,List<Tile>> bestAttackTile = new HashMap<Unit,List<Tile>>();//to decide which unit has the best attack avaible
	Map<Card,List<Tile>> bestSummonTile = new HashMap<Card,List<Tile>>();//to decide where to summon which card
	Map<Unit,Tile> optimalMoveTile = new HashMap<Unit, Tile>();
	Map<Unit,Tile> optimalAttackTile = new HashMap<Unit, Tile>();
	ArrayList<ObjectNode> possibilities=new ArrayList<>(); // Finalized object nodes with correct parameters
		

	Tile avatarTile;
	
	boolean movesEnd=false,cardsDrawEnd=false;
	
	Thread aiThread;
	
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
    
	public void testCardSummon(ActorRef out, GameState gameState) {
		
		int handIdx=0;   //-------> change index to test
		
		Tile tileToSummon=gameState.board.returnTile(6, 3); // The tile to move 
		
		//To test summon

		drawCardAI(handIdx+1,out,gameState,currentTile,tileToSummon);

	}
	
	public void testAttack(ActorRef out, GameState gameState) {
		
        BasicCommands.addPlayer1Notification(out, "AI attack", 2);

		Tile tileToAttack=new Tile(); // The tile to move 
	
		//To test move and attack
		tileToAttack.tilex=6;  //-------> change tileX to test
		tileToAttack.tiley=2;  //-------> change tileY to test
				
		// If attack is finalized, and move and atack or direct attacks
		attackAIUnit(out, gameState, currentTile, tileToAttack);
		
		
	}

	
	public void testMovement(ActorRef out, GameState gameState) {

        BasicCommands.addPlayer1Notification(out, "AI movement", 2);

		Tile tileToMove=new Tile(); // The tile to move 
		
		//To test move and attack
		tileToMove.tilex=7;  //-------> change tileX to test
		tileToMove.tiley=3;  //-------> change tileY to test
		
		// If movement is finalized, and move 
		moveAIUnit(out, gameState, currentTile, tileToMove);
	
	}
	
	public void startAIThread(ActorRef out,GameState gameState) {
		AppConstants.printLog("<------------------------------Starting AI Thread-------------------------------------------------->");

		aiThread = new Thread(new Runnable() {
		    @Override
		    public void run() {
   	
		    	try {

		    		cardsDrawEnd=false;
		    		movesEnd=false;
		    		
		    		startAILogic(out, gameState);
		    	
		    	}catch (Exception e) {
		    		
					e.printStackTrace();
					
					//Exit loop
					cardsDrawEnd=true;
					movesEnd=true;
					
					//Stop thread
					aiThread.interrupt();
					
					// End turn
					ObjectNode eventMessage = Json.newObject();
					eventMessage.put("messagetype", "endturnclicked");
					

					EndTurnClicked ec=new EndTurnClicked();
					ec.processEvent(out, gameState, eventMessage); 
					
				}
		    }
		});  
		aiThread.start();	
	}
  
	private void startAILogic(ActorRef out, GameState gameState) {
		AppConstants.printLog("<------------------------------startAILogic-------------------------------------------------->");
			
		boolean isContinue=true; //boolean to keep track of how long loop should continue
		// int i=0;//running the loop only ten times per turn for now to handle infinite loop
		while(isContinue)
		{
			AppConstants.printLog("<--------------------Game State at the moment---------------------->");
			//Test
			for(Card card:hand)
				AppConstants.printLog("Card in hand with Id: "+card.getId()+", and name: "+card.getCardname()+ " and mana cost: "+card.getManacost());
			
			
			checkUnitTiles(out,gameState);
			AppConstants.printLog("<--------------------Possible Moves---------------------->");
			listPossibleMove(out, gameState);

			AppConstants.printLog("<--------------------Possible Summon---------------------->");
			for (Card card : bestSummonTile.keySet()) {
				System.out.println("card: " + card.getCardname()+ "tiles: "+ bestSummonTile.get(card));
			}

			AppConstants.printLog("<--------------------Possible Move---------------------->");
			for (Unit unit : bestMoveTile.keySet()) {
				System.out.println("Unit: "+unit.getName() + " to tiles"+bestMoveTile.get(unit));
			}
			AppConstants.printLog("<--------------------Possible Attack---------------------->");
			for (Unit unit : bestAttackTile.keySet()) {
				System.out.println("Unit: "+unit.getName() + " to tiles"+bestAttackTile.get(unit));
			}
			
			if(cardsDrawEnd==true) // to test
				isContinue=false;
			
			//If the number of enemy units is less than 5, save spells for later and do summoning a unit alone
// 			if(tileWithPlayerUnits.size()<5)
// 			{
// 				// Summon a unit
// //				AppConstants.printLog("<-------- AI :: startAILogic():: Summon a unit !");
// 				drawCardAndProcessAction(0,out,gameState); // mode 1- units only
// 				if(tileWithMyUnit.size()>1){
// 					//move a unit
// 					AppConstants.printLog("<--------------------Move unit initiated---------------------->");
// 					moveAIProcessAction(out,gameState);
// 					if(optimalAttackTile.values()!=null){
// 						attackAIProcessAction(out, gameState);
// 					}
// 				}
				
// 			}else {
				
				
// 			}
			// Summon a unit
			//				AppConstants.printLog("<-------- AI :: startAILogic():: Summon a unit !");
			drawCardAndProcessAction(0,out,gameState); // mode 1- units only
			if(tileWithMyUnit.size()>1){
				//move a unit
				AppConstants.printLog("<--------------------Move unit initiated---------------------->");
				moveAIProcessAction(out,gameState);
				if(optimalAttackTile.values()!=null){
					attackAIProcessAction(out, gameState);
				}
			}
			
			// i++;
			// if(i>5) isContinue=false;

		}
		
		
	// Exited loop
		
		// End turn
		ObjectNode eventMessage = Json.newObject();
		eventMessage.put("messagetype", "endturnclicked");
		optimalAttackTile.clear();
		optimalMoveTile.clear();
		bestAttackTile.clear();
		bestSummonTile.clear();
		bestMoveTile.clear();
		

		EndTurnClicked ec=new EndTurnClicked();
		ec.processEvent(out, gameState, eventMessage); 
		
			
			// -------------------------- COMMENTING FOR NOW----------------------------------------------
//			// Check if avatar is in range of any enemy unit - SS
//			ArrayList<Tile> dangerTiles=checkIfUnitInDanger(currentTile,out,gameState);
//			
//			if(dangerTiles.size()>0)
//			{
//				AppConstants.printLog("<-------- AI :: startAILogic():: Avatar is in danger ! ");
//				
//				// Avatar is in danger, need to move away or attack the enemy
//				
//				// If the player avatar is nearby and it's health <= AI attack value, attack and thus game over
//				if(dangerTiles.contains(gameState.player1.getCurrentTile()))
//				{
//					
//					if(gameState.player1.getAvatar().getHealth()<=getAvatar().getAttack())
//					{
//						// Player's health is <= AI attack value, attack
//						attackAIUnit(out, gameState, currentTile, gameState.player1.getCurrentTile());
//					}
//				}else {
//					// Otherwise,try defensive way, move backward
//					AppConstants.printLog("<-------- AI :: startAILogic():: Find possible moves! ");
//
//					// Get list of possible backward moves respect to current avatar tile position
//					// ArrayList<Tile> possibleTilesToMove=possibleMoves("backward",currentTile,out,gameState);
//					
//
//				}
//				i++;
//				if(i>10) isContinue=false;		
//								
//			}else {
//				AppConstants.printLog("<-------- AI :: startAILogic():: Avatar is NOT in danger ! ");
//
//				// Avatar is safe from direct or indirect unit attacks. Can proceed with other units or cards
//				
//				if(tileWithMyUnit.size()<2 ) // No units are summoned yet 
//				{
//					AppConstants.printLog("<-------- AI :: startAILogic():: Summon a unit !");
//					drawCardAndProcessAction(1,out,gameState);
//					
//				}else if(cardsDrawEnd==false) // Can draw card again 
//				{
//					AppConstants.printLog("<-------- AI :: startAILogic():: Summon a unit !");
//					
//					// If number of player units is more than 5, cast spell or summon unit 
//					if(playerUnits.size()>5)
//						drawCardAndProcessAction(0,out,gameState);
//					else  // Otherwise, summon units only
//						drawCardAndProcessAction(1,out,gameState);
//
//					
//				}
//		}

		//------------------------------------------------------------------------------------------------

				
			
			// No moves left and can't draw cards further, exit loop and end turn
//			if(cardsDrawEnd==true && movesEnd==true) --> moves are not implememted yet
			
				

		
	

		
//		Boolean movesLeft=true;
//		if(movesLeft){
//			movesLeft=listPossibleMove(out,gameState);
//		}
		

		
	}
	
	
	
	
	
	private void drawCardAndProcessAction(int mode, ActorRef out, GameState gameState) {
		AppConstants.printLog("<-------- AI :: drawCardAndProcessAction():: Mana of AI: "+getMana());
		int handIdxToUse=-1;
		// check cards and summon unit
		if(cardsDrawEnd==false){
			handIdxToUse=checkHand(mode, gameState);//checking the cards in the hand (mode==1 --> retrieve index of card with only units)
			AppConstants.printLog("<-------- AI :: startAILogic():: handIdxToUse : "+handIdxToUse);
		}
		
		

		if(handIdxToUse>-1)
		{
			// We have a unit to summon, now find for a possible tile to summon
			Tile tileToSummon=findAtileToSummon(currentTile,out,gameState);
			
			if(tileToSummon!=null)
			{
				bestSummonTile.remove(getCardByHandPos(handIdxToUse));//remove the card which is being summoned
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
	 * The method looks for the eney unit closest to the AI avatar
	 * Find a tile from the summonableTileAroundAvatar with minimum distance to the enemy unit and returns that tile
	 * 
	 * @param currentTile
	 * @param gameState 
	 * @param out 
	 * @return
	 */
	
	
private Tile findAtileToSummon(Tile currentTile, ActorRef out, GameState gameState) {
		
		Tile tileToSummon = null;
		//should be changing this to accomodate for different cards
		possibleSummonList= PerformAction.getSummonableTilesAroundAvatar(out, gameState,currentTile);
		// gameState.board.highlightTilesRed(out, (ArrayList<Tile>) possibleSummonList);

		// Find the closest enemy unit on board
		int closestEnemyUnitIdx=findClosestEnemyUnit(currentTile);
		
		tileToSummon=findClosestTileToEnemy(closestEnemyUnitIdx,possibleSummonList);

		
		return tileToSummon;
	}

/** Method find the tile with minimum distance to the closest enemy unit 
 * 
 * @param EnemyUnitIdx
 * @param summonList
 * @return
 */
	private Tile findClosestTileToEnemy(int EnemyUnitIdx, List<Tile> summonList) {
		
		Tile tile=null;
		
		double minDistance=999;
		for(Tile summonTile:possibleSummonList)
		{
			double distance=calculateDistanceBetweenPoints(tileWithPlayerUnits.get(EnemyUnitIdx).getTilex(), tileWithPlayerUnits.get(EnemyUnitIdx).getTiley(), summonTile.getTilex(), summonTile.getTiley());

			if(distance<minDistance && summonTile.getUnitFromTile()==null) {
				tile=summonTile;
				minDistance=distance;
			}
		}
		
		return tile;
	}
	
	/** Method finds an enemy unit close to the AI avatar
	 * 
	 * @param ourTile
	 * @return
	 */

	private int findClosestEnemyUnit(Tile ourTile) {
		
		int idx=-1;
		
		double minDistance=999;
		
		for(int i=0;i<tileWithPlayerUnits.size();i++)
		{
			double distance=calculateDistanceBetweenPoints(ourTile.getTilex(), ourTile.getTiley(), tileWithPlayerUnits.get(i).getTilex(), tileWithPlayerUnits.get(i).getTiley());
//			AppConstants.printLog("AI:: findClosestEnemyUnit:: DISTANCE: "+distance);
			if(distance<minDistance)
			{
				minDistance=distance;
				// idx=tileWithPlayerUnits.get(i).getUnitFromTile().getId();//getting the actual id for that unit
				idx =i;
			}
		}
		
		
		return idx;
		
	}

//	private Tile findAtileToSummon(Tile currentTile, ActorRef out, GameState gameState) {
//		
//		Tile tileToSummon = null;
//		
//		possibleSummonList= PerformAction.getSummonableTilesAroundAvatar(out, gameState,currentTile);
//		gameState.board.highlightTilesRed(out, (ArrayList<Tile>) possibleSummonList);
//
//		for(Tile tile:possibleSummonList)
//		{
//			
//			if(tile.getUnitFromTile()==null)
//			{
//				AppConstants.printLog("<-------- AI :: startAILogic():: findAtileToSummon : tile: ["+tile.getTilex()+","+tile.getTiley()+"]");
//
//				tileToSummon=tile;
//				break;
//			}
//
//		}
//		
//		// All summonable tiles are already occupied
//		if(tileToSummon==null)
//		{
//			// Summon unit in any vacant forward tile
//			  for (int i = 4; i < gameState.board.tiles.length; i++) {
//		            for (int j = 0; j < gameState.board.tiles[i].length; j++) {
//		            	
//		               gameState.board.drawTileWithSleep(out, gameState.board.returnTile(i, j), 2, AppConstants.drawTileSleepTime);
//		            	if(gameState.board.returnTile(i, j).getUnitFromTile()==null)
//		    			{
//		            		gameState.board.returnTile(i, j);
//		            		break;
//		            	}		            	
//		            }
//		     }
//			
//		}
//
//		
//		return tileToSummon;
//	}




	//method the check the cards in the hand
	//use map or dict to store these in order to utilize later when deciding which card to summon
	
	/** Method to check cards and return the first match handindex to use
	 * 
	 * @param mode -> 0 - unit/spell, 1- only unit, 2- only spell
	 */
	public int checkHand(int mode, GameState gameState){
		for (int i=0;i<hand.size();i++) {

			Card c=hand.get(i);
			
			if(mode==1) // can be only unit
			{
				if(c.getManacost()<=getMana() && (c.getId()!=22 && c.getId()!=27 && c.getId()!=32 && c.getId()!=37)) //  check mana
				{
					return i; // return index
				}
			}else if(mode==2) { // can be only spell
				if(c.getManacost()<=getMana() && (c.getId()==22 || c.getId()==27 || c.getId()==32 || c.getId()==37))
				{
					if(c.getManacost()<=getMana())
					{
						return i;
					}
				}
			}
			else { // can be either unit or spell
				if(c.getManacost()<=getMana()) //  check mana
				{
					if(c.getId()==22 || c.getId()==27){//check if card in hand is staffofykir
						if(tileWithPlayerUnits.size()>5 || gameState.summonedUnits.get(1).getHealth()<18){//check if number of player units is more than 5 or AI avatar health is less than 18
							return i;
						}
						else continue;
					}
					else if(c.getId()==32 || c.getId()==37){
						//find unit with max health and max attack and play this card
					}
					else return i; // return index
				}
			}
			
		}
		return -1;
	}

	/** Methods to get the tiles with the units on the board
	 * 
	 * @param out
	 * @param gameState
	 * @return
	 */
	public void checkUnitTiles(ActorRef out,GameState gameState) {
		//AI unit's tile
		tileWithMyUnit=gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player2);
		AppConstants.printLog("<---------------------------------------------Tile with AI units------------------------------------------->");
		for (Tile tile : tileWithMyUnit) {
			if(tile.getUnitFromTile().getId()==41) System.out.println("Tiles with AI units: "+tile.toString()+ " with unit AI_Aviatar and id: " + tile.getUnitFromTile().getId());
			else System.out.println("Tiles with AI units: "+tile.toString()+ " with unit: "+ tile.getUnitFromTile().getName()+ " and id: " + tile.getUnitFromTile().getId());
		}
		//player unit's tile
		AppConstants.printLog("<---------------------------------------------Tile with player units------------------------------------------->");
		tileWithPlayerUnits = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player1);
		for (Tile tile : tileWithPlayerUnits) {
			if(tile.getUnitFromTile().getId()==40) System.out.println("Tiles with Player units: "+tile.toString()+ " with unit Human_Avatar and id: " + tile.getUnitFromTile().getId());
			else System.out.println("Tiles with Player units: "+tile.toString()+ " with unit: "+ tile.getUnitFromTile().getName()+ " and id: " + tile.getUnitFromTile().getId());
		}
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
		callSleepAI(200);
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
		callSleepAI(200);
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
		callSleepAI(200);
	}
	

	/**
	 * This method will give a list of possible moves like summon or move/attack
	 * @param out
	 * @param gameState
	 */
	public Boolean listPossibleMove(ActorRef out, GameState gameState) {
		//need to list all possible moves for the AI player
		Boolean done =false;//boolean to send back
		// AppConstants.printLog("<--------------------Possible Summon---------------------->");
		possibleSummon(out,gameState);
		// AppConstants.printLog("<--------------------Possible Move/Attack---------------------->");
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
				// System.out.println("card with hand position: "+ handindex+" name: "+ card.getCardname()+" can be played");
				//get summonable tiles
				if(card.getId()==22 || card.getId()==32){//for staffofykir can only be played on the avatar
					possibleSummonList = new ArrayList<Tile>();
					possibleSummonList.add(gameState.summonedUnits.get(1).getTileFromUnitP2(41, gameState, out));
					bestSummonTile.put(card, possibleSummonList);
				}
				else if(card.getId()== 27 || card.getId()==37){//for entropic decay
					possibleSummonList = tileWithPlayerUnits;
					possibleSummonList.remove(0);
					bestSummonTile.put(card, possibleSummonList);
				}
				else{//for all the other units with no special abilities
					possibleSummonList= PerformAction.getSummonableTiles(out, gameState, gameState.player2);
				
					bestSummonTile.put(card, possibleSummonList);//adding the list into the map with the card
				
				}
				
				//for debug
				// for (Tile tile : possibleSummonList) {
				// 	System.out.println("Possible summon tiles: "+ tile.toString());
				// }
			}
		}
	}



	
	/**
	 * This method will give a list of possible moves/attack for all the AI units
	 * @param out
	 * @param gameState
	 */
	public void possibleMoveAttack(ActorRef out, GameState gameState) {
		//possible moves if the unit has not moved or attacked
		for (Tile tile : tileWithMyUnit) {
			if(tile.getUnitFromTile().getId()==41){//for AI avatar
				// System.out.println("Unit: "+tile.getUnitFromTile().getName()+" with id: "+tile.getUnitFromTile().getId()+" has not attacked or moved");
				List <Tile> possibleTilesForMove = gameState.board.highlightTilesMoveAndAttack(0, gameState.player2, out, tile, gameState);
				possibleAttackableList=new ArrayList<Tile>();
				possibleMovableList = new ArrayList<Tile>();
				for (Tile tile2 : possibleTilesForMove) {
					if(tile2.getUnitFromTile()!=null && (tile2.getUnitFromTile().getId()<20 || tile2.getUnitFromTile().getId()==40)){//if enemy unit on those tiles
						possibleAttackableList.add(tile2);
					}
					else possibleMovableList.add(tile2);//if no unit 
				}
				bestMoveTile.put(tile.getUnitFromTile(), possibleMovableList);
				bestAttackTile.put(tile.getUnitFromTile(), possibleAttackableList);

				//for debugging
				// for (Tile tile2 : possibleMovableList) {
				// 	System.out.println("Tiles for Move: "+ tile2.toString());
				// }
				// for (Tile tile2 : possibleAttackableList) {
				// 	System.out.println("Tiles for Attack: "+ tile2.toString());
				// }
			}
			else{//for other units of AI
				possibleAttackableList=new ArrayList<Tile>();
				possibleMovableList = new ArrayList<Tile>();
				//System.out.println("Tile with my unit: "+ tile.toString());
				List <Tile> possibleTilesForMove = gameState.board.highlightTilesMoveAndAttack(0, gameState.player2, out, tile, gameState);
				for (Tile tile2 : possibleTilesForMove) {
					if(tile2.getUnitFromTile()!=null && (tile2.getUnitFromTile().getId()<20 || tile2.getUnitFromTile().getId()==40)){//if enemy unit on those tiles
						possibleAttackableList.add(tile2);
					}
					else possibleMovableList.add(tile2);//if no unit 
				}

				bestMoveTile.put(tile.getUnitFromTile(), possibleMovableList);
				bestAttackTile.put(tile.getUnitFromTile(), possibleAttackableList);
				//for debugging
				// for (Tile tile2 : possibleMovableList) {
				// 	System.out.println("Move for unit: "+tile.getUnitFromTile().getName() + " to tile: "+tile2.toString());
				// 	// System.out.println("Tiles for Move: "+ tile2.toString());
				// }
				// for (Tile tile2 : possibleAttackableList) {
				// 	System.out.println("Attack for unit: "+tile.getUnitFromTile().getName() + " to tile: "+tile2.toString());
				// 	// System.out.println("Tiles for Attack: "+ tile2.toString());
				// }
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
	
	/** Method returns the distance between two tile positions
	 * 
	 * @param x1 -> X position of tile A	
	 * @param y1 -> Y position of tile A
	 * @param x2 -> X position of tile B
	 * @param y2 -> Y position of tile B
	 * @return
	 */
	public double calculateDistanceBetweenPoints( double x1,  double y1,  double x2,  double y2) {       
			    return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
			}

	

	/** Method to handle Move logic 
	 * 
	 * @param out 	
	 * @param gameState
	 * 
	 */
	public void moveAIProcessAction(ActorRef out,GameState gameState) {
		//selecting best tile to move for the unit
		//currently we have the map which has unit and all the tiles they can move to
		
		
		double minDistance =999;
		double distance;
		Tile bestTile=null;
		for (Unit unit : bestMoveTile.keySet()) {//for all units in the map
			List<Tile> tiles = bestMoveTile.get(unit);//getting the list of all the tiles
			for (Tile tile : tiles) {
				// Find the closest enemy unit on board
				int closestEnemyUnitIdx=findClosestEnemyUnit(tile);
				distance = calculateDistanceBetweenPoints(tile.getTilex(), tile.getTiley(), tileWithPlayerUnits.get(closestEnemyUnitIdx).getTilex(), tileWithPlayerUnits.get(closestEnemyUnitIdx).getTiley());
				// System.out.println("closest enemy unit id: "+closestEnemyUnitIdx+ " from tile: "+tile.toString()+" with distance: "+distance);
				if(unit.getMoved()==false){
					if(distance<minDistance) {
						minDistance=distance;
						bestTile=tile;
					}
				}
			}
			minDistance =999;
			optimalMoveTile.put(unit, bestTile);
		}
		for (Unit unit : optimalMoveTile.keySet()) {
			System.out.println("Best move positon for unit: "+unit.getName()+ " is tile:"+optimalMoveTile.get(unit));
			if(unit.getMoved()==false){
				// System.out.println("unit can move: "+unit.getName());
				if(optimalMoveTile.get(unit)!=null){
					if(unit.getId()!= 41){//don't move Avatar for now
						AppConstants.printLog("<--------------------Unit Moving---------------------->");
						System.out.println("unit: "+ unit.getName()+" moving from tile: "+unit.getTileFromUnitP2(unit.getId(), gameState, out).toString() +" to tile: "+optimalMoveTile.get(unit).toString());
						moveAIUnit(out, gameState, unit.getTileFromUnitP2(unit.getId(), gameState, out), optimalMoveTile.get(unit));
						callSleepAI(2000);
						unit.setMoved(true);
						// possibleMoveAttack(out, gameState);
					}
					else{
						//move for avatar to be done
					}
					
				}
			}
			
			
		}
		
	}


	/** Method to handle attack logic 
	 * 
	 * @param out 	
	 * @param gameState
	 * 
	 */
	public void attackAIProcessAction(ActorRef out,GameState gameState){
		//select the best attack tile for every unit
		Tile bestTile=null;
		for (Unit unit : bestAttackTile.keySet()) {
			List<Tile> tiles = bestAttackTile.get(unit);
			int lowHealth=99;
			for (Tile tile : tiles) {
				if(tile.getUnitFromTile().getHealth()<lowHealth){
					lowHealth=tile.getUnitFromTile().getHealth();
					bestTile=tile;
				}
			}
			optimalAttackTile.put(unit, bestTile);
		}
		for (Unit unit : optimalAttackTile.keySet()) {
			System.out.println("Best Attack positon for unit: "+unit.getName()+ " is tile:"+optimalAttackTile.get(unit));
			
				if(unit.getAttacked()==false){
					if(optimalAttackTile.get(unit)!=null){
						if(unit.getId()!=41){
							AppConstants.printLog("<--------------------Unit Attacking---------------------->");
							System.out.println("unit: "+ unit.getName()+" attacking tile: "+optimalAttackTile.get(unit).toString());
							attackAIUnit(out, gameState, unit.getTileFromUnitP2(unit.getId(), gameState, out), optimalAttackTile.get(unit));
							unit.setAttacked(true);
							// if(optimalAttackTile.get(unit).getUnitFromTile()==null){
							// 	optimalAttackTile.remove(unit);
							// }
						}
					}
				}
			
		}
	}

	/** Method to put AI to sleep 
	 * 
	 * @param millis 	
	 * 
	 */
	private void callSleepAI(long millis){
		try{
			Thread.sleep(millis);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
	}
	public static void unitDied(){
		//remove unit from the different DS when they die
	}
}
