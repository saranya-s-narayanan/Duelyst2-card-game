package structures.basic;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
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

		
//		checkMovement(out,gameState);
		checkHand();//checking the cards in the hand
		
		checkAttack(out,gameState);
	}

	//method the check the cards in the hand
	//use map or dict to store these in order to utilize later when deciding which card to summon
	public void checkHand(){
		List <Card> cardInHand=super.hand;
		for (Card card : cardInHand) {
			System.out.println("card in AI's hand: "+ card.getCardname()+ " with Mana cost: "+ card.getManacost());
		}
	}

	private void checkAttack(ActorRef out, GameState gameState) {
		Tile tileToAttack=new Tile(); // The tile to move 
		
		//-------------- Logic to identify a tile to move---------
		
		
		// To test direct attack
//		tileToAttack.tilex=7;
//		tileToAttack.tiley=1;
		
		//To test move and attack
		tileToAttack.tilex=5;
		tileToAttack.tiley=4;
				
		// If movement is finalized, and move 
		attackAIUnit(out, gameState, currentTile, tileToAttack);
		
		
	}

	/** This method will check for an optimal movement to perform and updates front end
	 * 
	 * @param out
	 * @param gameState
	 */
	private void checkMovement(ActorRef out, GameState gameState) {

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
		tc.processEvent(out, gameState, eventMessage); // send it to the initalize event processor

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
		tc.processEvent(out, gameState, eventMessage); // send it to the initalize event processor

	}

	

	
	
}
