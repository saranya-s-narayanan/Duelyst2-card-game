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

            if (gameState.player1Turn == true) // Player 1 clicked the tile

            {

                highlightAndMove(out, gameState, clickedTile, gameState.player1); // add turns
            }
            else {
                highlightAndMove(out, gameState, clickedTile, gameState.player2);

            }
            

        }


    }

    /** Method checks if the tile click is for the first time or second
     * if it is first time click, 
     * 			- get the click unit object
     * 			- if the unit is not moved and attacked yet, highlight movable and attackable tiles
     * 			- if the unit has already moved, highlight attackable tiles only
     * 			- if the unit has already attacked, no valid moves left
     * if it is second time click,
     * 			- Move the unit,if the clicked tile is empty
     * 			- if the clicked tile isnot empty, attack the unit given that's not a firendly unit
     * @param out
     * @param gameState
     * @param clickedTile
     * @param player
     */

    private void highlightAndMove(ActorRef out, GameState gameState, Tile clickedTile, Player player) {

        if (startTile == null) { // if the start tile hasn't been set yet
            Unit selectedUnit = clickedTile.getUnitFromTile(); // get the unit from the clicked tile
            AppConstants.printLog("------> UnitClicked :: On tile " + clickedTile.getTilex() + " " + clickedTile.getTiley() + " by player 1");
            AppConstants.callSleep(100);

            if (selectedUnit != null) { // if the unit is not null
                startTile = clickedTile; // set the start tile to the clicked tile
                
                // Get the unit index from the summoned arraylist position
                int unitIdx=PerformAction.getUnitIndexFromSummonedUnitlist(startTile.getUnitFromTile(),gameState.summonedUnits);
        		
                // Check whether that unit has moved or attacked in the turn
                if(gameState.summonedUnits.get(unitIdx).getMoved()==false && gameState.summonedUnits.get(unitIdx).getAttacked()==false) // Unit hasn't moved or attacked yet
                {
                	// Unit not moved or attacked yet
                    AppConstants.printLog("------> UnitClicked :: Unit has NOT moved yet!");
                	gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTiles(out, startTile)); // highlight tiles to move and attack
                	
                }else if(gameState.summonedUnits.get(unitIdx).getAttacked()==false){
                	// Unit has moved,but not attacked yet
                    AppConstants.printLog("------> UnitClicked :: Unit has moved, but NOT attacked yet!");
                	gameState.board.highlightTilesRed(out, gameState.board.getAdjacentTilesToAttack(player,out, startTile)); // highlight tiles to attack only
                	
                }else {
                	//Unit has already moved or attacked
                    AppConstants.printLog("------> UnitClicked :: Unit has already attacked!");
                    BasicCommands.addPlayer1Notification(out, "No moves left!", 2);
                }

            } else {
                BasicCommands.addPlayer1Notification(out, "Please select a unit to move or attack! ", 2); // if the unit is null

            }
            
            AppConstants.callSleep(100);

        } else if (startTile.getUnitFromTile().getIsPlayer() == player.getID()){ // Second click moves the unit to the clicked tile

        	// Get the unit index from the summoned arraylist position
            int unitIdx=PerformAction.getUnitIndexFromSummonedUnitlist(startTile.getUnitFromTile(),gameState.summonedUnits);
   		
            // clear the highlighting once move is clicked
            gameState.board.clearTileHighlighting(out, gameState.board.getAdjacentTiles(out, startTile)); 
            AppConstants.callSleep(200);
            
            if(clickedTile.getUnitFromTile()==null && gameState.summonedUnits.get(unitIdx).getMoved()==false) // Clicked an empty tile --> movement
            {
                AppConstants.printLog("------> TileClicked :: Moving unit to tile " + clickedTile.getTilex() + " " + clickedTile.getTiley());

                moveUnit(out, startTile, clickedTile, gameState); // move the unit to the clicked tile
                gameState.summonedUnits.get(unitIdx).setMoved(true);
                
            }else if(clickedTile.getUnitFromTile()!=null && gameState.summonedUnits.get(unitIdx).getAttacked()==false){ // Clicked an occupied tile --> attack
            	
                AppConstants.printLog("------> TileClicked :: Attacking unit at tile " + clickedTile.getTilex() + " " + clickedTile.getTiley());
                boolean attackStatus=false;
                
                attackStatus=PerformAction.attackUnit(player,out,gameState.summonedUnits.get(unitIdx),startTile,clickedTile, gameState);
                
                gameState.summonedUnits.get(unitIdx).setAttacked(attackStatus);
            	
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
