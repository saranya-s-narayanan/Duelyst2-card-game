package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import structures.basic.Card;
import structures.basic.Player;
import structures.basic.Tile;
import utils.AppConstants;
import structures.basic.Unit;

import java.util.ArrayList;

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

    public int handPosition;

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        if (gameState.isGameActive) // if the frontend connection is active
        {
            if (gameState.player1Turn) { // for the first player

                handPosition = message.get("position").asInt();//get hand position
                AppConstants.printLog("------> CardClicked:: Game is active !");
                
                //method call to highlight card
                highlightMiniCard(out, handPosition, gameState);
                //method to highlight tiles on which card can be summoned
                highlightSummonableTiles(out, gameState, gameState.player1);

            }
            else {
                highlightSummonableTiles(out, gameState, gameState.player2);
            }
        }

    }

    public void highlightSummonableTiles(ActorRef out, GameState gameState, Player player) {
        ArrayList<Tile> list = new ArrayList<>();


        // list of the tiles with units
        list = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), player);

        // iteration through the list and highlight adjacent tiles
        for (int i = 0; i < list.size(); i++) {

            gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTilesToAttack(out, list.get(i)));
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
	}


}
