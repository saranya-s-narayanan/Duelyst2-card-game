import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.BetterUnit;
import utils.AppConstants;

//Previous test commented out - see below for more details. No test in here currently.

public class AIInitialPositionTest {

	@Test
	public void AIInitialPositionTest() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// This test is to confirm that the AI's initial tile position is (7,2) once the game is initialized
	        
        assertEquals(gameState.player2.getCurrentTile(),gameState.board.returnTile(7,2)); // Set player current tile

	}

}