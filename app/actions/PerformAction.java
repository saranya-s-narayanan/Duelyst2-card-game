package actions;

import java.util.ArrayList;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
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

import static structures.basic.UnitAnimationType.idle;

/** This class contains methods used for implementing logic for various actions such as
 * highlight,move,attack and so on.
 * This class can be used for both players
 *
 */

public class PerformAction {

	/*// <---- Moved below code to Appconstants - SS ---->
	 * static String[] effects = { StaticConfFiles.f1_buff,
	 * StaticConfFiles.f1_inmolation, StaticConfFiles.f1_martyrdom,
	 * StaticConfFiles.f1_summon };
	 */
	
	
	
	
	/** This method implements attack function of players
	 * @param player 
	 * @param out 
	 * @param avatar 
	 * 
	 * @param tilex
	 * @param tiley
	 * @param gameState
	 * @return 
	 */
	public static boolean attackUnit(Player player, ActorRef out, Unit unit, Tile startTile, Tile enemyTile , GameState gameState) {
		// TODO Auto-generated method stub
		
		
		// Retrieve the unit from the corresponding tile position
		Unit enemyUnit=enemyTile.getUnitFromTile();
		gameState.startTile=startTile;
		
		if(enemyUnit!=null)
		{	
				// Check whether the unit is a friendly unit or not
				if(enemyUnit.getIsPlayer()!=player.getID())
				{
					ArrayList<Tile> tilesList=gameState.board.getAdjacentTilesToAttack(player,out, startTile);
					
					// If the enemyTile is in range of the startTile, attack directly
					if(tilesList.contains(enemyTile)) 
					{
						return attackDirectly(player,out,unit,startTile,enemyTile,gameState,enemyUnit);

						
					}else {
						// Enemytile is not in range for direct attack, have to move and then attack
						
						tilesList=gameState.board.highlightTilesMoveAndAttack(0,player,out, startTile,gameState);
						
						if(tilesList.contains(enemyTile)) // have to move,then attack
						{
							tilesList= new ArrayList<>();
							tilesList=gameState.board.getAdjacentTiles(out, startTile); // Get the adjacent tiles to just move
							
							// Get the attackable tiles of the enemy tile and check whether any of those tiles comes inside the adjacenttiles of the start tile
							ArrayList<Tile> enemyAdjacentTiles=gameState.board.retrieveAdjacentTilesToAttackPosition(out, enemyTile);
	
			                AppConstants.printLog("------> TileClicked :: PerFormAction :: Move and attack :: enemyAdjacentTiles : " +enemyAdjacentTiles.size() );
	
	
							Tile tileToMove = null;
							
							for(int i=0;i<tilesList.size();i++)
							{
								tileToMove=tilesList.get(i);
								
								// If any vacant tile is in the list of tilesTomove list of startTile, return that tile
								if(enemyAdjacentTiles.contains(tileToMove) && tileToMove.getUnitFromTile()==null)
									break;
							}
	
							// Move to the adjacent tile
							if(player.getID()==1)
								moveUnit(1,out, startTile, tileToMove, gameState); // show player notifications (mode 1)
							else
								moveUnit(0,out, startTile, tileToMove, gameState);

							
							AppConstants.callSleep(1000); // To allow movement to finish before attacking
							return attackDirectly(player,out,unit,tileToMove,enemyTile,gameState,enemyUnit);
							
						}else {
							
							BasicCommands.addPlayer1Notification(out, "Enemy not in range! ", 2);
							AppConstants.callSleep(100);
						}	
		
					}
			}else {
				BasicCommands.addPlayer1Notification(out, "Please select an enemy unit to attack! ", 2);
				AppConstants.callSleep(100);

			}

		}
		return false;

		

	}
	private static boolean attackDirectly(Player player, ActorRef out, Unit unit, Tile startTile, Tile enemyTile,
			GameState gameState, Unit enemyUnit) {

		// Not a friendly unit --> attack
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack); // unit attacks enemy
	    AppConstants.callSleep(AppConstants.attackSleepTime);
	    
	    int attackVal;

	    if(enemyUnit.getSummonedID()==1) // Should update player 1 avatar health
	    {
	    	attackVal=gameState.player1.getAvatar().getHealth()-unit.getAttack();
	    	
	    	gameState.player1.getAvatar().setHealth(attackVal);  // update enemy's health
			enemyUnit.setHealth(attackVal); // update enemy's health front end
			
		    // To avoid negative values as health
		    if(gameState.player1.getAvatar().getHealth()<0)
		    	gameState.player1.getAvatar().setHealth(0);
	    	
	    }else  if(enemyUnit.getSummonedID()==2) // Should update player2 avatar health
	    {

	    	attackVal=gameState.player2.getAvatar().getHealth()-unit.getAttack();
	    	gameState.player2.getAvatar().setHealth(attackVal);  // update enemy's health
			enemyUnit.setHealth(attackVal); // update enemy's health front end
			
		    // To avoid negative values as health
		    if(gameState.player2.getAvatar().getHealth()<0)
		    	gameState.player2.getAvatar().setHealth(0);
	    	
	    }else {
	    	attackVal=enemyUnit.getHealth()-unit.getAttack();
	    	enemyUnit.setHealth(attackVal);  // update enemy's health
		
		    // To avoid negative values as health
		    if(enemyUnit.getHealth()<0)
		    	enemyUnit.setHealth(0);
	    }
	    
		// update front end
		BasicCommands.setUnitHealth(out, enemyUnit, enemyUnit.getHealth());
	    AppConstants.callSleep(100);
	        
	    BasicCommands.setUnitAttack(out, enemyUnit, enemyUnit.getAttack());
	    AppConstants.callSleep(100);
	    
	    EffectAnimation ef = BasicObjectBuilders.loadEffect(AppConstants.effects[2]);
		BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.idle); // resets to idle after attack
	    
	    if(attackVal<=0) // enemy unit dead, clear tile and update front end
	    {
            gameState.summonedUnits.remove(enemyUnit);

			BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.death);
		    AppConstants.callSleep(AppConstants.deathSleepTime);
		  
			BasicCommands.playEffectAnimation(out, ef, enemyTile);
		    AppConstants.callSleep(AppConstants.effectSleepTime);
		    enemyTile.setUnitToTile(null);
			BasicCommands.deleteUnit(out, enemyUnit);
		    AppConstants.callSleep(3000);

	    }else { //enemy survived, counter attack
	    	
	    	attackVal=-1;
	    	
	    	BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.attack); // enemy attacks avatar
		    AppConstants.callSleep(AppConstants.attackSleepTime);

		    if(unit.getSummonedID()==1) // Should update avatar health of player1
		    {

		    	attackVal=gameState.player1.getAvatar().getHealth()-enemyUnit.getAttack();
		    	gameState.player1.getAvatar().setHealth(attackVal);
//		    	gameState.player1.setHealth(attackVal);
				unit.setHealth(attackVal); // update enemy's health front end
		    	
		    	
			    // To avoid negative values as health
			    if(gameState.player1.getAvatar().getHealth()<0)
			    {
			    	gameState.player1.getAvatar().setHealth(0);
//			    	gameState.player1.setHealth(0);

			    }
		    	
		    }else if(unit.getSummonedID()==2) // Should update avatar health of player2
		    {

		    	attackVal=gameState.player2.getAvatar().getHealth()-enemyUnit.getAttack();
		    	gameState.player2.getAvatar().setHealth(attackVal);
//		    	gameState.player2.setHealth(attackVal);
				unit.setHealth(attackVal); // update enemy's health front end

		    	
		    	
			    // To avoid negative values as health
			    if(gameState.player2.getAvatar().getHealth()<0)
			    {
			    	gameState.player2.getAvatar().setHealth(0);
//			    	gameState.player2.setHealth(0);

			    }
		    	
		    }else {
		    	attackVal=unit.getHealth()-enemyUnit.getAttack();
			    unit.setHealth(attackVal); // update unit health
					
			    // To avoid negative values as health
			    if(unit.getHealth()<0)
			    	unit.setHealth(0);
		    }
		    
	    	// update front end
			BasicCommands.setUnitHealth(out, unit, unit.getHealth());
		    AppConstants.callSleep(100);
		        
		    BasicCommands.setUnitAttack(out, unit, unit.getAttack());
		    AppConstants.callSleep(100);

			BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.idle); // resets to idle after attack
		    
		    
		    if(attackVal<=0) //unit dead 
		    {
	            gameState.summonedUnits.remove(unit);

		    	BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
			    AppConstants.callSleep(AppConstants.deathSleepTime);

			    BasicCommands.playEffectAnimation(out, ef, startTile);
			    AppConstants.callSleep(AppConstants.effectSleepTime);
			    startTile.setUnitToTile(null);
				BasicCommands.deleteUnit(out, unit);
			    AppConstants.callSleep(100);
			  
		    }
	    	
	    }
	    return true;

	}
	
	/** Method to move a unit to a tile
	 * 
	 * @param mode -0 (when player notifications are needed) & 1 (when player notifications are not needed)
	 * @param out
	 * @param startTile
	 * @param endTile
	 * @param gameState
	 */
	public static void moveUnit(int mode, ActorRef out, Tile startTile, Tile endTile,GameState gameState) {

		Unit unitToMove = startTile.getUnitFromTile();

		// Check if there is a unit on the start tile
		if(unitToMove == null) {
			if(mode==1)
			{
				BasicCommands.addPlayer1Notification(out, "No unit on the starting tile", 2);
				AppConstants.callSleep(200);
			}
			return;
		}

		// Check if the end tile is empty
		if(endTile.getUnitFromTile() != null) {
			if(mode==1)
			{
				BasicCommands.addPlayer1Notification(out, "The end tile is already occupied", 2);
				AppConstants.callSleep(200);
			}
			return;
		}


		// Check if the unit can move to the end tile
		if(!gameState.board.getAdjacentTiles(out, startTile).contains(endTile)) {
			if(mode==1)
			{
				BasicCommands.addPlayer1Notification(out, "Unit cannot move to the end tile", 2);
				AppConstants.callSleep(200);
			}
			return;
		}

		startTile.setUnitToTile(null); //Update starttile unit to null
		
		// Move the unit to the end tile
		gameState.board.addUnitToBoard(endTile.getTilex(), endTile.getTiley(), unitToMove);
		AppConstants.callSleep(50);
		BasicCommands.moveUnitToTile(out, unitToMove, endTile);
		unitToMove.setPositionByTile(endTile); 
		AppConstants.callSleep(50);
	}

	/** This method implements the game end functionality
     * @param out
     * @param gameState
     */
    public static void gameEnd(ActorRef out, GameState gameState) {
        
//    	 AppConstants.printLog("------> gameEnd:: Before- gameState.isGameOver: "+gameState.isGameOver);
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
			AppConstants.callSleep(100);
	
			
        }

    }
	public static int getUnitIndexFromSummonedUnitlist(Unit selectedUnit, ArrayList<Unit> summonedUnits) {
		// TODO Auto-generated method stub
//    	AppConstants.printLog("------> getUnitIndexFromSummonedUnitlist:: BB selectedUnit id : "+selectedUnit.getId()+", summonid: "+selectedUnit.getSummonedID());

		for(int i=0;i<summonedUnits.size();i++)
		{
//	    	AppConstants.printLog("------> getUnitIndexFromSummonedUnitlist:: selectedUnit id : "+summonedUnits.get(i).getId()+", summonid: "+summonedUnits.get(i).getSummonedID());

			if(summonedUnits.get(i).getSummonedID()==selectedUnit.getSummonedID())
			{
				return i;
			}
		}
		return -1;
	}



}



