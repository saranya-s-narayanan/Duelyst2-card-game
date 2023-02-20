package events;


import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Tile;
import utils.AppConstants;

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
            if (message.hasNonNull("cardClicked")) {
                int handPosition = message.get("position").asInt();
                AppConstants.printLog("------> CardClicked:: Game is active !");

                //  i have tried different ways but essentially this is the logic i am trying to implement
                highlightSummonableTiles(out, gameState);

            }
        }
    }

    public void highlightSummonableTiles(ActorRef out, GameState gameState){
        ArrayList<Tile> list = new ArrayList<>();

        // list of the tiles with units
        list = gameState.board.getTilesWithUnits(out, gameState.board.getTiles());

        for (int i = 0; i < list.size(); i++) {
            gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTilesToAttack(out,list.get(i)));
        }
    }


}
