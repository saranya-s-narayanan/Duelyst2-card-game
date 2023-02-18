package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import utils.AppConstants;

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
			AppConstants.printLog("------> End turn Clicked:: Game is active !");

			if(gameState.getTurn()==true){//if it was player's turn then only they can use end turn
				AppConstants.printLog("------> End turn Clicked by the player !");
				gameState.player1.drawAnotherCard(out);//draw a card from the deck
				AppConstants.printLog("------> End turn Clicked by the player :: Draw new card complete !");
				gameState.player1.setMana(0);//flushing the mana
				AppConstants.printLog("------> End turn Clicked by the player :: mana set to 0 !");
				gameState.setTurn(false);//passing the turn to AI
				AppConstants.printLog("------> End turn Clicked by the player :: turn passed to AI !");
			}
			else{//for when AI ends its turn
				AppConstants.printLog("------> End turn Clicked by the AI !");
				gameState.incrementPlayerTurn();//incrementing the turn number the player will have
				AppConstants.printLog("------> End turn Clicked by the AI :: player turn incremented !");
				gameState.player1.setMana(gameState.getPlayerTurnNumber()+1);//setting the mana to turn+1
				AppConstants.printLog("------> End turn Clicked by the AI :: player mana set to Turn+1 !");
				gameState.setTurn(true);//passing the turn to the player
				AppConstants.printLog("------> End turn Clicked by the AI :: turn passed to player !");
			}
			
			
		}
	}

}
