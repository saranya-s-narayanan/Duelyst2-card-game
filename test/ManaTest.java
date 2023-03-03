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
import structures.basic.Board;
import structures.basic.Player;
import utils.AppConstants;

public class ManaTest {

	@Test
	public void ManaTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initalized

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);
		
		//Initializing a dummy player and dummy AI player with maxHealth and playerTurnNumber/compTurnNumber from gameState
		Player x = new Player(AppConstants.playerMaxHealth, gameState.playerTurnNumber);
		Player y = new Player(AppConstants.playerMaxHealth, gameState.compTurnNumber);
		
		//Checks that getMana equals the turn number + 1
		assertTrue(x.getMana() == gameState.playerTurnNumber+1);
		assertTrue(y.getMana() == gameState.compTurnNumber+1);
		
		//This test is failing - got this error: Test ManaTest.ManaTest failed: java.lang.AssertionError: null, took 5.529 sec
		//https://community.pivotal.io/s/question/0D50e00006jq8eCCAQ/what-causes-javalangassertionerror-null-at-orgapachecatalinamappermapperinternalmapmapperjava744?language=en_US

	}

}