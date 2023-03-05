import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.EndTurnClicked;
import events.EventProcessor;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Player;
import utils.AppConstants;

import akka.actor.ActorRef;
//This should allow me to get the ActorRef done

public class EndTurnTest {

	@Test
	public void EndTurnTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

//		assertFalse(gameState.gameInitalised); // check we have not initalized

		//This sets up the GameState and initializes the players. See Initialize.java to confirm what is instantiated
		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);
				
		// Creating a new EndTurnClicked object to get to processEvent, which has the logic for switching the player turns
		EndTurnClicked e = new EndTurnClicked();
		
		ActorRef out;
				
		//Ends player1's turn 
		e.processEvent(out, gameState, eventmessage);
		
		//Checks that player1's turn ends once this event is processed
		assertFalse(gameState.player1Turn);
		
		//Ends player2's turn 
		e.processEvent(out, gameState, eventmessage);
		
		//Checks that player2's turn ends once this event is processed
		assertTrue(gameState.player1Turn);

		//**Need to add this test and IsGameOver to the README**
		
		//Received the error messages below for this test
		
//		[error] Test ManaTest.ManaTest failed: java.lang.AssertionError: null, took 5.597 sec
//		[error]     at ManaTest.ManaTest(ManaTest.java:45)
//		[error]     at jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//		[error]     at jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
//		[error]     at jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//		------> message type:---->null
//		------> Initialize :: creating board..
//		[error]     at java.lang.reflect.Method.invoke(Method.java:566)
//		[error]     ...
		
	}

}