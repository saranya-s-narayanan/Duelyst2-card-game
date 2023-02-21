import static org.junit.Assert.assertFalse;

//This file was used to draft the other tests, please ignore (will delete later) 17/2/23

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import events.Initalize;
import play.libs.Json;
import structures.GameState;

public class InitializationTest2 {
	
	@Test
	public void checkInitialized2() {
		
		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();
		
		assertFalse(gameState.gameInitalised);
		
		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);
		
		assertTrue(gameState.gameInitalised);
		
	}

}