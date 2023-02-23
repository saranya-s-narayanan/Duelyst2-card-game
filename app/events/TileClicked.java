package events;


import com.fasterxml.jackson.databind.JsonNode;


import actions.PerformAction;

import akka.actor.ActorRef;

import commands.BasicCommands;

import structures.GameState;

import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.AppConstants;

import static actions.PerformAction.moveUnit;


/**
 * Indicates that the user has clicked an object on the game canvas, in this case a tile.
 * <p>
 * The event returns the x (horizontal) and y (vertical) indices of the tile that was
 * <p>
 * clicked. Tile indices start at 1.
 * <p>
 * <p>
 * <p>
 * {
 * <p>
 * messageType = “tileClicked”
 * <p>
 * tilex = <x index of the tile>
 * <p>
 * tiley = <y index of the tile>
 * <p>
 * }
 *
 * @author Dr. Richard McCreadie
 */

public class TileClicked implements EventProcessor {

    public static Tile startTile; // start tile
    

    @Override

    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {


        if (gameState.isGameActive) // if the frontend connection is active

        {

            int tilex = message.get("tilex").asInt();

            int tiley = message.get("tiley").asInt();

            Tile clickedTile = gameState.board.returnTile(tilex, tiley); // clicked tile object

            if (gameState.player1Turn == true) // Player 1 clicked the tile

            {

                highlightAndMove(out, gameState, clickedTile, gameState.player1); // add turns
            }
            else {
                highlightAndMove(out, gameState, clickedTile, gameState.player2);

            }
            


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


    private void highlightAndMove(ActorRef out, GameState gameState, Tile clickedTile, Player player) {
        if (startTile == null) { // if the start tile hasn't been set yet
            Unit selectedUnit = clickedTile.getUnitFromTile(); // get the unit from the clicked tile
            AppConstants.printLog("------> UnitClicked :: On tile " + clickedTile.getTilex() + " " + clickedTile.getTiley() + " by player 1");
            AppConstants.callSleep(100);

            if (selectedUnit != null) { // if the unit is not null
                startTile = clickedTile; // set the start tile to the clicked tile
                gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTiles(out, startTile));
                AppConstants.callSleep(100);

            } else {
                BasicCommands.addPlayer1Notification(out, "Please select a tile with a unit.", 2); // if the unit is null

            }

        } else if (startTile.getUnitFromTile().getIsPlayer() == player.getID()){ // Second click moves the unit to the clicked tile


            gameState.board.clearTileHighlighting(out, gameState.board.getAdjacentTiles(out, startTile)); // clear the highlighting once move is clicked
            AppConstants.printLog("------> TileClicked :: Moving unit to tile " + clickedTile.getTilex() + " " + clickedTile.getTiley());
            AppConstants.callSleep(200);

            moveUnit(out, startTile, clickedTile, gameState); // move the unit to the clicked tile
            startTile = null; // Reset the start tile to no unit
        }
        else {
            gameState.board.clearTileHighlighting(out, gameState.board.getAdjacentTiles(out, startTile));
            AppConstants.callSleep(200);
        startTile = null; // Reset the start tile to no unit
        }
    }

    public static void setStartTile(boolean bool){
        if(bool==false) startTile=null;
    }


}
