package events;



import com.fasterxml.jackson.databind.JsonNode;


import actions.PerformAction;

import akka.actor.ActorRef;

import commands.BasicCommands;

import structures.GameState;

import structures.basic.Tile;
import structures.basic.Unit;
import utils.AppConstants;

import static actions.PerformAction.moveUnit;


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

	private Tile startTile; // start tile
	@Override

	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {


		if(gameState.isGameActive) // if the frontend connection is active

		{

			int tilex = message.get("tilex").asInt();

			int tiley = message.get("tiley").asInt();

			Tile clickedTile = gameState.board.returnTile(tilex, tiley); // clicked tile object

			if(startTile == null) { // if the start tile hasn't been set yet
				Unit selectedUnit = clickedTile.getUnitFromTile(); // get the unit from the clicked tile
				if(selectedUnit != null) { // if the unit is not null
					startTile = clickedTile; // set the start tile to the clicked tile

				} else {
					BasicCommands.addPlayer1Notification(out, "Please select a tile with a unit.", 2); // if the unit is null
				}
			} else { // Second click moves the unit to the clicked tile
				moveUnit(out, startTile, clickedTile, gameState); // move the unit to the clicked tile
				startTile = null; // Reset the start tile to no unit
			}


//			if(gameState.player1Turn==true) // Player 1 clicked the tile
//
//			{
//
//				AppConstants.printLog("------> TileClicked :: ("+tilex+","+tiley+") by player 1");
//
//
//				if(gameState.player1.isAvatarOnTile(tilex,tiley)==true) // tile occupied by the avatar
//
//				{
//
//					PerformAction.highlightTiles(gameState.player1,out,tilex,tiley,gameState); //highlight or unhighlight the tiles to move or attack
//
//
//				}else // tile occupied by enemy units
//
//				{
//
//
//					if(gameState.player1.getHighlighted()==true) // if the tiles are already highlighted
//
//					{
//
//						AppConstants.printLog("------> TileClicked :: Already highlighted..Attack Unit..");
//
//						PerformAction.attackUnit(gameState.player1,gameState.avatar,out,tilex,tiley,gameState);
//
//						gameState.player1.setHighlighted(false);
//
//					}else {
//
//						AppConstants.printLog("------> TileClicked :: Not highlighted yet or invalid click ! ");
//
//						BasicCommands.addPlayer1Notification(out, "Invalid click!", 2);
//
//						AppConstants.callSleep(100);
//
//					}
//
//				}
//
//
//			}else // player 2 clicked the tile --> Not practical
//
//			{
//
//				BasicCommands.addPlayer1Notification(out, "Invalid click!", 2);
//
//				AppConstants.callSleep(100);
//
//
//			}
//
//
		}





	}


}
