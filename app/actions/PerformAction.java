package actions;

import java.util.ArrayList;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.TileClicked;
import structures.GameState;
import structures.basic.BetterUnit;
import structures.basic.EffectAnimation;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import structures.basic.UnitAnimationType;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

/** This class contains methods used for implementing logic for various actions such as
 * highlight,move,attack and so on.
 * This class can be used for both players
 *
 */
public class PerformAction {

	static String[] effects = {
			StaticConfFiles.f1_buff,
			StaticConfFiles.f1_inmolation,
			StaticConfFiles.f1_martyrdom,
			StaticConfFiles.f1_summon
	};
	
	
	
	/** This method implements attack function of players
	 * @param out 
	 * @param avatar 
	 * 
	 * @param tilex
	 * @param tiley
	 * @param gameState
	 */
	public static void attackUnit(ActorRef out, Unit unit, Tile enemyTile , GameState gameState) {
		// TODO Auto-generated method stub
		
		
		// Retrieve the unit from the corresponding tile position
		Unit enemyUnit=enemyTile.getUnitFromTile();
		
		if(enemyUnit!=null)
		{
			BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack); // unit attacks enemy
		    AppConstants.callSleep(1000);

		    enemyUnit.setHealth(enemyUnit.getHealth()-unit.getAttack());  // update enemy's health
		
			// update front end
			BasicCommands.setUnitHealth(out, enemyUnit, enemyUnit.getHealth());
		    AppConstants.callSleep(100);
		        
		    BasicCommands.setUnitAttack(out, enemyUnit, enemyUnit.getAttack());
		    AppConstants.callSleep(100);
		    
		    if(enemyUnit.getHealth()<=0) // enemy unit dead, clear tile and update front end
		    {
                gameState.summonedUnits.remove(enemyUnit);

				BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.death);
			    AppConstants.callSleep(1000);
			    EffectAnimation ef = BasicObjectBuilders.loadEffect(effects[2]);
				BasicCommands.playEffectAnimation(out, ef, enemyTile);
			    AppConstants.callSleep(1000);
			    enemyTile.setUnitToTile(null);
				BasicCommands.deleteUnit(out, enemyUnit);
			    AppConstants.callSleep(3000);

		    }else { //enemy survived, counter attack
		    	
		    	BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.attack); // enemy attacks avatar
			    AppConstants.callSleep(1000);

			    unit.setHealth(unit.getHealth()-enemyUnit.getAttack()); // update unit health
					
		    	// update front end
				BasicCommands.setUnitHealth(out, unit, unit.getHealth());
			    AppConstants.callSleep(100);
			        
			    BasicCommands.setUnitAttack(out, unit, unit.getAttack());
			    AppConstants.callSleep(100);
			    
			    
			    if(unit.getHealth()<=0) //unit dead 
			    {
			    	BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
				    AppConstants.callSleep(1000);
				    EffectAnimation ef = BasicObjectBuilders.loadEffect(effects[2]);
					BasicCommands.playEffectAnimation(out, ef, enemyTile);
				    AppConstants.callSleep(1000);
				    //enemyTile.setUnitToTile(null);
					BasicCommands.deleteUnit(out, unit);
				    AppConstants.callSleep(100);
				    
				    if(unit.getId()==1 || unit.getId()==2) // if the unit is player1 or player 2 , set gameOver=true
				    {
					    gameState.isGameOver=true; // update state variable
		    	   	
				    	
				    }
				    
				  
			    
			    }
		    	
		    }
		}

		

	}
	public static void moveUnit(ActorRef out, Tile startTile, Tile endTile,GameState gameState) {

		Unit unitToMove = startTile.getUnitFromTile();

		// Check if there is a unit on the start tile
		if(unitToMove == null) {
			BasicCommands.addPlayer1Notification(out, "No unit on the starting tile", 2);
			AppConstants.callSleep(200);
			return;
		}

		// Check if the end tile is empty
		if(endTile.getUnitFromTile() != null) {
			BasicCommands.addPlayer1Notification(out, "The end tile is already occupied", 2);
			AppConstants.callSleep(200);
			return;
		}


		// Check if the unit can move to the end tile
		if(!gameState.board.getAdjacentTiles(out, startTile).contains(endTile)) {
			BasicCommands.addPlayer1Notification(out, "Unit cannot move to the end tile", 2);
			AppConstants.callSleep(200);
			return;
		}

		startTile.setUnitToTile(null); //Update starttile unit to null
		
		// Move the unit to the end tile
		gameState.board.addUnitToBoard(endTile.getTilex(), endTile.getTiley(), unitToMove);
		AppConstants.callSleep(50);
		BasicCommands.moveUnitToTile(out, unitToMove, endTile);
		AppConstants.callSleep(50);
	}

	/** This method implements the game end functionality
     * @param out
     * @param gameState
     */
    public static void gameEnd(ActorRef out, GameState gameState) {
        
    	// AppConstants.printLog("------> gameEnd:: Before- gameState.isGameOver: "+gameState.isGameOver);
        if(gameState.isGameActive==true){
			//check if player 1 health is 0 or not
			if(gameState.player1.getHealth()<=0 || (gameState.player1.getCardInDeck()==0 && gameState.player1.getCardInHand()==0)){
				gameState.isGameOver=true;//whichever of these are used to represent game end
				// gameState.isGameActive=false;//whichever of these are used to represent game end
				BasicCommands.addPlayer1Notification(out, "Game Over! You Lost", 5);
			}
			else if (gameState.player2.getHealth()<=0 || (gameState.player2.getCardInDeck()==0 &&
				gameState.player2.getCardInHand()==0)){//if AI reaches 0
				gameState.isGameOver=true;//whichever of these are used to represent game end
				// gameState.isGameActive=false;//whichever of these are used to represent game end
				BasicCommands.addPlayer1Notification(out, "Game Over! Won", 5);
			}
			
        }

    }
	public static int getUnitIndexFromSummonedUnitlist(Unit selectedUnit, ArrayList<Unit> summonedUnits) {
		// TODO Auto-generated method stub
    
		for(int i=0;i<summonedUnits.size();i++)
		{
			if(summonedUnits.get(i).getId()==selectedUnit.getId())
			{
				return i;
			}
		}
		return -1;
	}



}



