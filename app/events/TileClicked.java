package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import utils.AppConstants;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * clicked. Tile indices start at 1.
 * 
 * { 
 *   messageType = “tileClicked”
 *   tilex = <x index of the tile>
 *   tiley = <y index of the tile>
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class TileClicked implements EventProcessor{

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

		if(gameState.isGameActive) // if the frontend connection is active
		{
			AppConstants.printLog("------> TileClicked :: Game is active ! ");
			int tilex = message.get("tilex").asInt();
			int tiley = message.get("tiley").asInt();
		}
		
		
		
		
	}

}
