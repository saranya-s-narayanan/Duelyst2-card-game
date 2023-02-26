package events;


import com.fasterxml.jackson.databind.JsonNode;


import actions.PerformAction;

import akka.actor.ActorRef;

import commands.BasicCommands;

import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.AppConstants;

import static actions.PerformAction.moveUnit;

import java.util.logging.Handler;


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
            cardClick=message.get("messagetype");//message to keep track of previous click on front-end
			AppConstants.printLog("------> message type:---->"+cardClick.asText());
            int tilex = message.get("tilex").asInt();
            int tiley = message.get("tiley").asInt();
            Tile clickedTile = gameState.board.returnTile(tilex, tiley); // clicked tile object

            if (gameState.player1Turn == true) // Player 1 clicked the tile

            {
                if(gameState.clickMessage.asText().equals("cardclicked") ){//summoning
                    System.out.println("summon");
                    
                    summonCard(out,gameState,clickedTile,gameState.player1);
                }
                // highlightAndMove(out, gameState, clickedTile, gameState.player1); // add turns
            }
            else {
                highlightAndMove(out, gameState, clickedTile, gameState.player2);

            }
            

        }


    }


    private void highlightAndMove(ActorRef out, GameState gameState, Tile clickedTile, Player player) {

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
            AppConstants.callSleep(100);

        } else if (startTile.getUnitFromTile().getIsPlayer() == player.getID()){ // Second click moves the unit to the clicked tile

        	// Get the unit index from the summoned arraylist position
            int unitIdx=PerformAction.getUnitIndexFromSummonedUnitlist(startTile.getUnitFromTile(),gameState.summonedUnits);
   		
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


    /** This method summons the card to the board
     * 
     * @param out
     * @param gameState
     * @param clciked
     * @param player
     * 
     */
    public void summonCard(ActorRef out, GameState gameState, Tile clicked, Player player) {
        // System.out.println("inside summon function");
        Card handCard = player.getCardByHandPos(gameState.handPosClicked-1);//getting the card by hand position
        // System.out.println("Card name: "+handCard.getCardname());
        Unit unitSummon = player.getUnitbyCard(gameState.handPosClicked-1, player);//getting the unit by hand position
        // System.out.println("Id of the unit to summoned: "+unitSummon.getId());
        // System.out.println("player mana: "+ player.getMana());
        // System.out.println("mana cost: "+ handCard.getManacost());
        if(player.getMana()>=handCard.getManacost()){//checking mana cost
            
            player.setMana(player.getMana()-handCard.getManacost());//decrease the mana
            player.setPlayer(out);//reflecting the mana on board
            OtherClicked.clearCardClicked(out, gameState, player);//clear highlighting
            AppConstants.callSleep(200);
            player.drawUnitToBoard(out, unitSummon, clicked, handCard, player);//draw unit on board
            // System.out.println("Draw unit done!");
        }
        else {//if not enough mana then a notification is given to the player
            BasicCommands.addPlayer1Notification(out, "Not enough Mana", 2);
            OtherClicked.clearCardClicked(out, gameState, player);//clear highlighting
        }
    }


}
