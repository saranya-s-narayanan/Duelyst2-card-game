import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import javax.validation.constraints.AssertTrue;

import org.assertj.core.api.Assertions;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Heartbeat;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Tile;
import utils.AppConstants;
import utils.BasicObjectBuilders;

public class HeartbeatTest {

	GameState gameState;
	CheckMessageIsNotNullOnTell altTell;
	Heartbeat heartbeatProcessor ;
	@Before                                         
    public void setUp() {

		// First override the alt tell variable so we can issue commands without a running front-end
		altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used
		
		// As we are not starting the front-end, we have no GameActor, so lets manually create
		// the components we want to test
		gameState = new GameState(); // create state storage
		heartbeatProcessor = new Heartbeat();

    }

/**
 *  This test checks whether the values  Game state 
 * variables - 'lastHeartbeatTime','isGameActive' are not zero and true respectively
 * after the reception of first heartbeat message.
 * 
 */
	
	@Test
	public void checkHeartbeatVariableUpdation() {
		
		// Check whether 'lastHeartbeatTime' in GameState is zero before the first heartbeat message reception
		assertEquals(0,gameState.lastHeartbeatTime);
		
		// Check whether 'isGameActive' in GameState is false before the first heartbeat message reception
		assertFalse(gameState.isGameActive);
		
		// lets simulate recieveing an heartbeat message
		ObjectNode eventMessage = Json.newObject(); // create a dummy message
		heartbeatProcessor.processEvent(null, gameState, eventMessage); // send it to the heartbeat event processor
		
		// Check whether 'lastHeartbeatTime' in GameState is NOT equal to zero after the first heartbeat message reception
		assertNotEquals(0,gameState.lastHeartbeatTime);
				
		// Check whether 'isGameActive' in GameState is true after the first heartbeat message reception
		assertTrue(gameState.isGameActive);
			
		
	}
	
	

/**
 *  This test checks whether the values of objects 'heartbeatTimer'and 'heartbeatTimerTask' 
 *  are not null after the reception of first heartbeat message.
 * 
 */
	
	@Test
	public void checkHeartbeatTimerStart() {
		
		//Check whether the 'heartbeatTimer' object is null
		assertNull(heartbeatProcessor.heartbeatTimer);
		
		//Check whether the 'heartbeatTimerTask' object is null
		assertNull(heartbeatProcessor.heartbeatTimer);

		
		// lets simulate recieveing an heartbeat message
		ObjectNode eventMessage = Json.newObject(); // create a dummy message
		heartbeatProcessor.processEvent(null, gameState, eventMessage); // send it to the heartbeat event processor
		
			
		//Check whether the 'heartbeatTimer' object is not null
		assertNotNull(heartbeatProcessor.heartbeatTimer);
				
		//Check whether the 'heartbeatTimerTask' object is not null
		assertNotNull(heartbeatProcessor.heartbeatTimer);
		
		
	}
	
	/**
	 *  This test checks whether the values of objects 'heartbeatTimer'and 'heartbeatTimerTask' 
	 *  are not null after the reception of first heartbeat message.
	 * 
	 */
		
		@Test
		public void checkHeartbeatTimerStop() {
			
			//Call the event few times, so that timer keep runs for some time
			for (int i=0;i<5;i++)
			{
				// lets simulate recieveing an heartbeat message
				ObjectNode eventMessage = Json.newObject(); // create a dummy message
				heartbeatProcessor.processEvent(null, gameState, eventMessage); // send it to the heartbeat event processor
			}
			
			// Sets 'allowedHeartbeatTimeGap' in AppConstants to zero so that 'timeDifference>AppConstants.allowedHeartbeatTimeGap'
			// will be satisfied to check whether the timer stops 
			AppConstants.allowedHeartbeatTimeGap=0;
			
			// wait for few seconds so that the timertask will execute
			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Check whether the 'heartbeatTimer' object is null
			assertNull(heartbeatProcessor.heartbeatTimer);
			
			//Check whether the 'heartbeatTimerTask' object is null
			assertNull(heartbeatProcessor.heartbeatTimer);
			
			
		}
}
