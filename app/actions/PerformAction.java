package actions;

import akka.actor.ActorRef;
import structures.GameState;
import structures.basic.Player;
import structures.basic.Tile;
import structures.basic.Unit;
import utils.AppConstants;

public class PerformAction {

	
	
	/** This method is used to check the available actions (highlight/move/attack) upon a tile click and perform
	 * one action
	 * @param player
	 * @param out
	 * @param tilex
	 * @param tiley
	 * @param gameState
	 */

	public static void checkAction(Player player, ActorRef out, int tilex, int tiley, GameState gameState) {
		// TODO Auto-generated method stub
		
		if(player.getMoved()==false) // player not moved yet
		{
	        AppConstants.printLog("------> PerformAction :: checkAction:: Not moved yet!");

			if(player.getHighlighted()==false) // Not highlighted yet
			{
		        AppConstants.printLog("------> PerformAction :: checkAction:: Not highlighted yet! Hightlighting...");

				// Add logic to highlight the movable tiles 
				
				
				player.setHighlighted(true);
				
				
			}else { // 
		        AppConstants.printLog("------> PerformAction :: checkAction:: Player moving !");

				// Add player movement logic
				
				
				
				player.setMoved(true);
				player.setHighlighted(false);
			}
		}else {
	        AppConstants.printLog("------> PerformAction :: checkAction:: Player already moved! Can attack...");

			if(player.getHighlighted()==false) // Not highlighted yet
			{
		        AppConstants.printLog("------> PerformAction :: checkAction:: Attack tiles not highlighted yet! Highlighting...");

				// Add logic to highlight the tiles to attack 
				
				
				
				player.setHighlighted(true);

			}else
			{
		        AppConstants.printLog("------> PerformAction :: checkAction:: Player attacking...");

				// Add player attack logic
				
				
				
				player.setAttacked(true);
				player.setMoved(true);
				player.setHighlighted(false);
			}
		}
	}
	
	
	
	/** This method implements attack function of players
	 * 
	 * @param out
	 * @param tilex
	 * @param tiley
	 * @param gameState
	 */
	public static void attackUnit(ActorRef out, int tilex, int tiley, GameState gameState) {
		// TODO Auto-generated method stub
		
		// Fetch the tile object of the clicked position
		Tile enemyTile=gameState.board.returnTile(tilex, tiley);
		
		// Retrieve the unit from the corresponding tile position
		Unit enemyUnit=enemyTile.getUnitFromTile();
		
		
		
		
		
	}

}
