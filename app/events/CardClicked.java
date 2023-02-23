package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;
import utils.AppConstants;
import structures.basic.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case a card.
 * The event returns the position in the player's hand the card resides within.
 * <p>
 * {
 * messageType = “cardClicked”
 * position = <hand index position [1-6]>
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class CardClicked implements EventProcessor {

    public int handPosition;//variable to hold hand position
    public JsonNode cardClick;//variable to hold the Json message that comes in when a click is made
    
    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        if (gameState.isGameActive){ // if the frontend connection is active
            cardClick=message.get("messagetype");//message to keep track of previous click on front-end
            AppConstants.printLog("------> message type:---->"+gameState.clickMessage);
            if(gameState.clickMessage != cardClick){
                gameState.clickMessage=cardClick;
            }
            if (gameState.player1Turn) { // for the first player
                
                handPosition = message.get("position").asInt();//get hand position
                AppConstants.printLog("------> CardClicked:: Game is active !");

                highlightSummonableTiles(out, gameState, gameState.player1);

            }
            else {
                highlightSummonableTiles(out, gameState, gameState.player2);
            }
        }

    }

    public void highlightSummonableTiles(ActorRef out, GameState gameState, Player player) {
        
        if(gameState.SummonTileList==null){
            // list of the tiles with units
            ArrayList<Tile> list = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), player);

            // iteration through the list and highlight adjacent tiles
            for (Tile items: list) {
                gameState.SummonTileList=gameState.board.getAdjacentTiles(out, items);
                gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTilesToAttack(out, items));
            }
        }
        
    }

    //method to heighlight MiniCards
    public void highlightMiniCard(ActorRef out, int position, GameState gameState) {
        if(gameState.handPosClicked<0){//check if its the first click
            gameState.handPosClicked=position;//set the gamestate variable to new position clicked
            Card card1 = gameState.player1.getCardByHandPos(position-1);//get the card at the hand position
            BasicCommands.drawCard(out, card1, gameState.handPosClicked, 1);//highlight the card
        }
        else if(position != gameState.handPosClicked){//check if another card is clicked
            
            Card card2 = gameState.player1.getCardByHandPos(position-1);//get the card at the new position
            Card card1 = gameState.player1.getCardByHandPos(gameState.handPosClicked-1);//get the card at earlier position
            BasicCommands.drawCard(out, card1, gameState.handPosClicked, 0);//dehighlight the previous position
            BasicCommands.drawCard(out, card2, position, 1);//highlight the new postion
            gameState.handPosClicked=position;//set the new position to gameState
        }
        else{//this is not done yet
            clearHighlightMiniCard(out, gameState);
            gameState.board.clearTileHighlighting(out, gameState.SummonTileList);
        }
	}

    //method to clear all highlights
    public static void clearHighlightMiniCard(ActorRef out, GameState gameState) {
        Card card1 = gameState.player1.getCardByHandPos(gameState.handPosClicked-1);//get the card at earlier position
        BasicCommands.drawCard(out, card1, gameState.handPosClicked, 0);//dehighlight the previous position
        gameState.handPosClicked=-1;//set the gameState hand position to -1
    }


}
