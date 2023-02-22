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

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        if (gameState.isGameActive) // if the frontend connection is active
        {
            if (gameState.player1Turn) { // for the first player

                int handPosition = message.get("position").asInt();
                AppConstants.printLog("------> CardClicked:: Game is active !");
                //getting the card at the handPosition of the card
                heighlightMiniCard(out, handPosition, gameState);

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
    public void heighlightMiniCard(ActorRef out, int position, GameState gameState) {
		Card card = gameState.player1.getCardByHandPos(position-1);
        BasicCommands.drawCard(out, card, position, 1);
	}


}
