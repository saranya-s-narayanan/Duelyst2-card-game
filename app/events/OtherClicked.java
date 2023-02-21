package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import utils.AppConstants;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * somewhere that is not on a card tile or the end-turn button.
 * 
 * { 
 *   messageType = “otherClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class OtherClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		
		if(gameState.isGameActive) // if the frontend connection is active
		{
			AppConstants.printLog("------> OtherClicked :: Game is active ! ");
			gameState.board.clearTileHighlighting(out, gameState.board); // this will clear tile highlighting once other is clicked
		}
		
	}

}


