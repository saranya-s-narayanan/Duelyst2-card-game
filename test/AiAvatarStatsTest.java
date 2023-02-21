import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.BetterUnit;
import structures.basic.Board;
import structures.basic.Player;
import structures.basic.Tile;
import utils.AppConstants;
import utils.BasicObjectBuilders;

//This test does NOT work- needs revision as of 17/2/23

public class AiAvatarStatsTest {

	@Test
	public void AvatarTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

//		assertFalse(gameState.gameInitalised); // check we have not initalized

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

//		Check whether the player's initial health = maxHealth
//		Need to make sure that player1Tile is set to (1,2)
//		Need to make sure that player2Tile is set to (7,2)

//		Board board = null; // Declare a board object
//		// Call constructor method
//		board = new Board(null);

		Tile tile = BasicObjectBuilders.loadTile(1, 2); // create a tile

// 		check whether the initial position of the player1 avatar is (1,2)
//		assertEquals("** The player1 Avatar should be initialized at (1,2) **", tile, gameState.avatar.getPosition());

	}

}
