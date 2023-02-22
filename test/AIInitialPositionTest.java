import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;
import utils.AppConstants;

public class AIInitialPositionTest {

	@Test
	public void AIInitialPositionTest() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// This test is to confirm that the AI's initial x and y positions are 7 and 2 once the game is initialized
		assertEquals(7, gameState.player2.getCurrentXpos());
		assertEquals(2, gameState.player2.getCurrentYpos());

	}

}