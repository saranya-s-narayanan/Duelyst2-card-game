package structures.basic;

import com.fasterxml.jackson.databind.node.ObjectNode;

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

	Tile currentTile; // To keep track of avatar's tile
	
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
    
    public void setCurrentTile(Tile currentTile) {
    	this.currentTile=currentTile;
    }
    
    public Tile getCurrentTile() {
    	return currentTile;
    }

	public void startAILogic(ActorRef out, GameState gameState) {

		// Logic to decide what action to perform can be written here
		
//		checkMovement(out,gameState); //to check movement possibilities
		
//		checkAttack(out,gameState); // To check attack possibilities

		drawCard(out,gameState); // To check drawcard possibilities
		
		
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
	

	
	
}
