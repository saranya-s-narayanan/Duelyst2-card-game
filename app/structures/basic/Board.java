package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.AppConstants;
import utils.BasicObjectBuilders;


/**
* The board class will contain tile objects stored in a 2D array data structure and will contain 
* methods to set the constraints of the available moves and total size of the board (9x5). 
* The Board object consists of several tile objects.
* 
* */

public class Board {

	Tile[][] tiles = null;
	
	public Board(ActorRef out) {
		tiles =new Tile[AppConstants.boardWidth][AppConstants.boardHeight];
		setTiles(out);
	}

/**	This method creates tile objects and assign those tiles to the board object.
 * 
 * @param out
 */
	
	public void setTiles(ActorRef out) {
		
		// Create a tile object
		Tile tile;
		
		// Iterate through the tiles array
		for (int i=0;i<tiles.length;i++)
		{
			for(int j=0;j<tiles[i].length;j++)
			{
				// Create a new tile object 
				tile = BasicObjectBuilders.loadTile(i, j);
				
				// Assign that tile object to the array position
				tiles[i][j]=tile;
				
				// Draw the tile on the front end
				BasicCommands.drawTile(out, tile, 0);	

				
			}
		}
		
	}
	
	
/** Getter method to return tiles objects of a board
 * 
 * @return Tile
 */
	
	public Tile[][] getTiles(){
		return this.tiles;
	}
}
