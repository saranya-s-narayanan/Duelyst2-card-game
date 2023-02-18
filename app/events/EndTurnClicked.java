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
		if(gameState.isGameActive) // if the frontend connection is active
		{
			AppConstants.printLog("------> End turn Clicked:: Game is active !");
			
			if(gameState.activePlayer==1) //Player 1 pressed end turn
			{
				gameState.player1.drawAnotherCard(out);
				
				
			}else { // Player 2 pressed end turn
				gameState.player2.drawAnotherCard(out);

			}
		}
	}

}
