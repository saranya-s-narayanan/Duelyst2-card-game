package events;


import com.fasterxml.jackson.databind.JsonNode;

import actions.PerformAction;
import akka.actor.ActorRef;
import commands.BasicCommands;
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
					PerformAction.highlightTiles(gameState.player1,out,tilex,tiley,gameState); //highlight or unhighlight the tiles to move or attack
					
				}else // tile occupied by enemy units
				{

					if(gameState.player1.getHighlighted()==true) // if the tiles are already highlighted
					{
						AppConstants.printLog("------> TileClicked :: Already highlighted..Attack Unit..");
						PerformAction.attackUnit(gameState.player1,gameState.avatar,out,tilex,tiley,gameState);
						gameState.player1.setHighlighted(false);
					}else {
						AppConstants.printLog("------> TileClicked :: Not highlighted yet or invalid click ! ");
						BasicCommands.addPlayer1Notification(out, "Invalid click!", 2);
				    	AppConstants.callSleep(100);
					}
				}

			}else // player 2 clicked the tile --> Not practical
			{
				BasicCommands.addPlayer1Notification(out, "Invalid click!", 2);
		    	AppConstants.callSleep(100);

			}

		}
		
		
		
		
	}

}
