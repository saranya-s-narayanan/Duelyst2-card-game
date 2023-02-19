package events;


import com.fasterxml.jackson.databind.JsonNode;

import actions.PerformAction;
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
			int tilex = message.get("tilex").asInt();
			int tiley = message.get("tiley").asInt();
			
		
			if(gameState.player1Turn==true) // Player 1 clicked the tile
			{
				AppConstants.printLog("------> TileClicked :: ("+tilex+","+tiley+") by player 1");
				
				if(gameState.player1.isAvatarOnTile(tilex,tiley)==true) // tile occupied by the avatar
				{
					PerformAction.checkAction(gameState.player1,out,tilex,tiley,gameState);
					
				}else // tile occupied by enemy units
				{
					AppConstants.printLog("------> TileClicked :: Attack Unit");

					PerformAction.attackUnit(out,tilex,tiley,gameState);
				}

			}else // player 2 clicked the tile
			{
				AppConstants.printLog("------> TileClicked :: ("+tilex+","+tiley+") by player 2");

			}

		}
		
		
		
		
	}

}
