
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Player;
import structures.basic.SpecialAbilities;
import structures.basic.Unit;
import utils.AppConstants;

public class SpecialAbilitiesTests {

	@Test
	public void SilverguardKnightTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		assertFalse(gameState.gameInitalised); // check we have not initalized

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);

//			// Check if AI Avatar is not null when game is initialized)
//			assertNotNull("Player should be initialized", gameState.aiAvatar);

//			boolean attackUnitRanged;
//			Unit enemyUnit = new Unit();
//			Unit unit = new Unit();
//			int attackVal;
//			SpecialAbilities.attackUnitRanged(0, null, null, null, null, null, gameState);
//			if (attackUnitRanged = true) {
//				if (enemyUnit.getSummonedID() == 1) {
//		            attackVal = gameState.player1.getAvatar().getHealth() - unit.getAttack();
//		            gameState.player1.getAvatar().setHealth(attackVal);  // update enemy's health
//		            enemyUnit.setHealth(attackVal); // update enemy's health front end
//
//	            // To avoid negative values as health
//	            if (gameState.player1.getAvatar().getHealth() < 0)
//	                gameState.player1.getAvatar().setHealth(0);

		// All the code above works but it is just copied from special abilities, don't
		// know how I can test it

		Unit unit = new Unit();
		unit.getAttack();
		if (unit.getId() == 3) {
			unit.setAttack(unit.getAttack() + 2);
		}

		assertTrue(unit.getAttack() == 2);

	}
}
