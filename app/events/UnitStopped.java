package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import utils.AppConstants;

/**
 * Indicates that a unit instance has stopped moving. 
 * The event reports the unique id of the unit.
 * 
 * { 
 *   messageType = “unitStopped”
 *   id = <unit id>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class UnitStopped implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(gameState.isGameActive) // if the frontend connection is active
		{
			AppConstants.printLog("------> TileClicked :: Game is active ! ");
			int unitid = message.get("id").asInt();
		}
		
	}

}
