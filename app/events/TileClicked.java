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
    public JsonNode cardClick;//variable to hold the Json message that comes in when a click is made

    @Override

    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {


        if (gameState.isGameActive) // if the frontend connection is active

        {
            gameState.clickMessage=message.get("messagetype");//message to keep track of previous click on front-end
			AppConstants.printLog("------> message type:---->"+gameState.clickMessage);
            int tilex = message.get("tilex").asInt();

            int tiley = message.get("tiley").asInt();

            Tile clickedTile = gameState.board.returnTile(tilex, tiley); // clicked tile object
            gameState.startTile=clickedTile;//added to keep track of the start tile on the board
            
            if (gameState.player1Turn == true){ // Player 1 clicked the tile
                highlightAndMove(out, gameState, clickedTile);
            }
            

        }


    }


    private void highlightAndMove(ActorRef out, GameState gameState, Tile clickedTile) {
        if (startTile == null) { // if the start tile hasn't been set yet
            Unit selectedUnit = clickedTile.getUnitFromTile(); // get the unit from the clicked tile
            AppConstants.printLog("------> UnitClicked :: On tile " + clickedTile.getTilex() + " " + clickedTile.getTiley() + " by player 1");
            AppConstants.callSleep(100);

            if (selectedUnit != null) { // if the unit is not null
                startTile = clickedTile; // set the start tile to the clicked tile
                
                // Get the unit index from the summoned arraylist position
                int unitIdx=PerformAction.getUnitIndexFromSummonedUnitlist(startTile.getUnitFromTile(),gameState.summonedUnits);
        		

                if(gameState.summonedUnits.get(unitIdx).getMoved()==false && gameState.summonedUnits.get(unitIdx).getAttacked()==false) // Unit hasn't moved or attacked yet
                {
                    AppConstants.printLog("------> UnitClicked :: Unit has NOT moved yet!");
                	gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTiles(out, startTile)); // highlight tiles to move and attack
                	
                }else if(gameState.summonedUnits.get(unitIdx).getAttacked()==false){
                    AppConstants.printLog("------> UnitClicked :: Unit has moved, but NOT attacked yet!");
                	gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTilesToAttack(out, startTile)); // highlight tiles to attack only
                	
                }else {
                    AppConstants.printLog("------> UnitClicked :: Unit has already attacked!");
                    BasicCommands.addPlayer1Notification(out, "No moves left!", 2);
                }

            } else {
                BasicCommands.addPlayer1Notification(out, "Please select a tile with a unit.", 2); // if the unit is null

            }
        } else if (startTile.getUnitFromTile().getIsPlayer() == 1 ){ // Second click moves the unit to the clicked tile

            gameState.board.clearTileHighlighting(out, gameState.board.getAdjacentTiles(out, startTile)); // clear the highlighting once move is clicked
            AppConstants.callSleep(200);
            
            if(clickedTile.getUnitFromTile()==null && gameState.summonedUnits.get(unitIdx).getMoved()==false) // Clicked an empty tile --> movement
            {
                AppConstants.printLog("------> TileClicked :: Moving unit to tile " + clickedTile.getTilex() + " " + clickedTile.getTiley());

                moveUnit(out, startTile, clickedTile, gameState); // move the unit to the clicked tile
                gameState.summonedUnits.get(unitIdx).setMoved(true);
                
            }else if(clickedTile.getUnitFromTile()!=null && gameState.summonedUnits.get(unitIdx).getAttacked()==false){ // Clicked an occupied tile --> attack
            	
                AppConstants.printLog("------> TileClicked :: Attacking unit at tile " + clickedTile.getTilex() + " " + clickedTile.getTiley());
                PerformAction.attackUnit(out,gameState.summonedUnits.get(unitIdx), clickedTile, gameState);
                gameState.summonedUnits.get(unitIdx).setAttacked(true);
            	
            }
            
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
