package structures.basic;

import akka.actor.ActorRef;
import events.CardClicked;
import events.TileClicked;
import structures.GameState;

import java.util.ArrayList;

public class ComputerTiles {


    // as only one ai unit has a different summoning type, this is just for that card. (Planar scout)
    public static ArrayList<Tile> planarScoutSummonableTiles(int cardId, GameState gameState, ActorRef out) {
        ArrayList<Tile> planarScoutTiles = new ArrayList<Tile>();
        if (cardId == 28 || cardId == 38) {
            planarScoutTiles = gameState.board.getTilesWithoutUnits(out, gameState.board.getTiles(), gameState.player2);
            return planarScoutTiles;
        }
        return planarScoutTiles;
    }

    // only attack tile list needed that is different
    public static ArrayList<Tile> pyromancerAttackTiles(int cardId, GameState gameState, ActorRef out) {
        ArrayList<Tile> pyromancerTiles = new ArrayList<Tile>();
        if (cardId == 25 || cardId == 35) {
            pyromancerTiles = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), TileClicked.opposingPlayer(gameState, gameState.player2));
            return pyromancerTiles;
        }
        return pyromancerTiles;
    }

    // only movement difference
    public static ArrayList<Tile> windshrikeMovementTiles(int cardId, ActorRef out, GameState gameSate) {
        ArrayList<Tile> windshrikeTiles = new ArrayList<Tile>();
        if (cardId == 24 || cardId == 34) {
            windshrikeTiles = gameSate.board.getTilesWithoutUnits(out, gameSate.board.getTiles(), gameSate.player2);
            return windshrikeTiles;
        }
        return windshrikeTiles;
    }
}