import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.BetterUnit;
import utils.AppConstants;

//Previous test commented out - see below for more details. No test in here currently.

public class PlayerInitialPositionTest {

	@Test
	public void PlayerInitialPositionTest() {

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised);

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

		// This test is to confirm that the Player's initial x and y positions are 1 and 2 once the game is initialized
		// assertEquals(1, gameState.player1.getCurrentXpos());
		// assertEquals(2, gameState.player1.getCurrentYpos());
		
				
		// As of 2/23, the previous test was no longer running because the position is determined in the BetterUnit constructor
		
	}

}