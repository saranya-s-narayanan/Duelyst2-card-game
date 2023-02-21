package actions;

import akka.actor.ActorRef;
import commands.BasicCommands;
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
	
	/** This method is used to check the available actions (highlight/move/attack) upon a tile click and perform
	 * one action
	 * @param player
	 * @param out
	 * @param tilex
	 * @param tiley
	 * @param gameState
	 */

	public static void highlightTiles(Player player, ActorRef out, int tilex, int tiley, GameState gameState) {
		// TODO Auto-generated method stub
		
		if(player.getMoved()==false || player.getAttacked()==false) // player not moved or attacked yet
		{

			if(player.getHighlighted()==false) // Not highlighted yet
			{
		        AppConstants.printLog("------> PerformAction :: checkAction:: Hightlighting...");


		        if(player.getMoved()==true) // already moved, highlight only adjacent tiles to attack
			        gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTilesToAttack(out,gameState.board.returnTile(tilex ,tiley)));
		        else // not moved, highlight tiles to move and attack
		        	gameState.board.highlightTilesWhite(out, gameState.board.getAdjacentTilesToMoveAndAttack(out,gameState.board,gameState.board.returnTile(tilex ,tiley)));

				player.setHighlighted(true);
				
				
			}else { // 
		        AppConstants.printLog("------> PerformAction :: checkAction:: Resetting highlight!");
				player.setHighlighted(false);
			}
		
		}
	}
	
	
	
	/** This method implements attack function of players
	 * @param player 
	 * @param avatar 
	 * 
	 * @param out
	 * @param tilex
	 * @param tiley
	 * @param gameState
	 */
	public static void attackUnit(Player player, BetterUnit avatar, ActorRef out, int tilex, int tiley, GameState gameState) {
		// TODO Auto-generated method stub
		
		// Fetch the tile object of the clicked position
		Tile enemyTile=gameState.board.returnTile(tilex, tiley);
		
		// Retrieve the unit from the corresponding tile position
		Unit enemyUnit=enemyTile.getUnitFromTile();
		
		if(enemyUnit!=null)
		{
			BasicCommands.playUnitAnimation(out, avatar, UnitAnimationType.attack); // avatar attacks enemy
		    AppConstants.callSleep(3000);

		    enemyUnit.setHealth(enemyUnit.getHealth()-avatar.getAttack()); 
		
			// update front end
			BasicCommands.setUnitHealth(out, enemyUnit, enemyUnit.getHealth());
		    AppConstants.callSleep(100);
		        
		    BasicCommands.setUnitAttack(out, enemyUnit, enemyUnit.getAttack());
		    AppConstants.callSleep(100);
		    
		    if(enemyUnit.getHealth()<=0) // enemy unit dead, clear tile and update front end
		    {
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
			    AppConstants.callSleep(3000);

		    	avatar.setHealth(avatar.getHealth()-enemyUnit.getAttack()); 
					
		    	// update front end
				BasicCommands.setUnitHealth(out, avatar, avatar.getHealth());
			    AppConstants.callSleep(100);
			        
			    BasicCommands.setUnitAttack(out, avatar, avatar.getAttack());
			    AppConstants.callSleep(100);
			    
			    
			    if(avatar.getHealth()<=0) //avatar dead --> player is dead, game over, other player won
			    {
			    	BasicCommands.playUnitAnimation(out, avatar, UnitAnimationType.death);
				    AppConstants.callSleep(1000);
				    EffectAnimation ef = BasicObjectBuilders.loadEffect(effects[2]);
					BasicCommands.playEffectAnimation(out, ef, enemyTile);
				    AppConstants.callSleep(1000);
				    //enemyTile.setUnitToTile(null);
					BasicCommands.deleteUnit(out, avatar);
				    AppConstants.callSleep(100);
				    
				    if(player.getID()==1)
				    {
				    	BasicCommands.addPlayer1Notification(out, "Game Over. Player 2 won !", 2);
				    	AppConstants.callSleep(100);
				    	
				    	
				    }else {
				    	BasicCommands.addPlayer1Notification(out, "Game Over. Player 1 won !", 2);
				    	AppConstants.callSleep(100);
				    	
				    
				    }
				    
				    gameState.isGameOver=true; // update state variable
				  
			    
			    }
		    	
		    }
		}
		
		
		
		
		
	}

}
