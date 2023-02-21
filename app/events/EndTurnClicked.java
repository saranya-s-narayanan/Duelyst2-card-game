package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import utils.AppConstants;
import commands.BasicCommands;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(gameState.isGameActive){ // if the frontend connection is active

			if(gameState.player1Turn==true){//if it was player's turn then only they can use end turn
				
				endPlayer1Turn(out,gameState);
				//startPlayer2Turn(); // To do
			}
			else{//for when AI ends its turn
				
				endPlayer2Turn(out,gameState);
				//startPlayer1Turn(); // To do
			}
			
			
		}
	}

	private void endPlayer2Turn(ActorRef out, GameState gameState) {
		// TODO Auto-generated method stub
		AppConstants.printLog("------> End turn Clicked by the AI !");
		
		gameState.playerTurnNumber++;//incrementing the turn number the player will have
		AppConstants.printLog("------> End turn Clicked by the AI :: player turn incremented !");
		
		gameState.player1.setMana(gameState.playerTurnNumber+1);//setting the mana to turn+1
		AppConstants.printLog("------> End turn Clicked by the AI :: player mana set to Turn+1 !");
		
		gameState.player1Turn=true;//passing the turn to the player
		AppConstants.printLog("------> End turn Clicked by the AI :: turn passed to player !");
		BasicCommands.addPlayer1Notification(out, "Your Turn", 2);
		gameState.player1.setPlayer(out);//making change on the front end
	}

	private void endPlayer1Turn(ActorRef out, GameState gameState) {
		// TODO Auto-generated method stub
		AppConstants.printLog("------> End turn Clicked by the player !");
		
		gameState.player1.drawAnotherCard(out,1);//draw a card from the deck
		AppConstants.printLog("------> End turn Clicked by the player :: Draw new card complete !");
		
		gameState.player1.setMana(0);//flushing the mana
		AppConstants.printLog("------> End turn Clicked by the player :: mana set to 0 !");
		
		gameState.player1Turn=false;//passing the turn to AI
		AppConstants.printLog("------> End turn Clicked by the player :: turn passed to AI !");
		BasicCommands.addPlayer1Notification(out, "Passing Turn Over", 2);
		gameState.player1.setPlayer(out);//making change on the front end
	}

}
