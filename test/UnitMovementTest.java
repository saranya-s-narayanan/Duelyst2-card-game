
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.PerformAction;
import commands.BasicCommands;
import commands.CheckMessageIsNotNullOnTell;
import events.Initalize;
import events.TileClicked;
import play.libs.Json;
import structures.GameState;
import structures.basic.Board;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.AppConstants;
import structures.basic.SpecialAbilities;

import static actions.PerformAction.moveUnit;


public class UnitMovementTest {

	@Test
	public void UnitMovementTest() {

		CheckMessageIsNotNullOnTell altTell = new CheckMessageIsNotNullOnTell(); // create an alternative tell
		BasicCommands.altTell = altTell; // specify that the alternative tell should be used

		GameState gameState = new GameState();
		Initalize initializeProcessor = new Initalize();

		ObjectNode eventmessage = Json.newObject();
		initializeProcessor.processEvent(null, gameState, eventmessage);
		
//		Unit enemyUnit = null;
//		
//		if (enemyUnit.getHealth() < 0) {
//        enemyUnit.setHealth(0);
//		}

		// Check whether AI Avatar is initialized
//		assertNotNull("Player should be initialized", gameState.aiAvatar);
		
//	    public static Tile startTile; // start tile
//		int unitIdx=PerformAction.getUnitIndexFromSummonedUnitlist(startTile.getUnitFromTile(),gameState.summonedUnits);
		
		//Was trying to test items in TileClicked but everything has references to out
		//Try computerPlayer again next
		//actually try SpecialAbilities then ComputerPlayer
		
	}

}
