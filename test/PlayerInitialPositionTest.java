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
		
		//Confirms that player1's initial tile position is (1,2)
        assertEquals(gameState.player1.getCurrentTile(),gameState.board.returnTile(1,2)); // Set player current tile

		
	}

}