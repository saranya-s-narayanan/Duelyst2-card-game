package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import scala.App;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.lang.reflect.Array;
import java.util.ArrayList;



/**
 * The board class will contain tile objects stored in a 2D array data structure and will contain
 * methods to set the constraints of the available moves and total size of the board (9x5).
 * The Board object consists of several tile objects.
 */

public class Board {

    Tile[][] tiles = null;

    public Board(ActorRef out) {
        tiles = new Tile[AppConstants.boardWidth][AppConstants.boardHeight];
        setTiles(out);
    }

    /**
     * This method creates tile objects and assign those tiles to the board object.
     *
     * @param out
     */

    public void setTiles(ActorRef out) {

        // Create a tile object
        Tile tile;

        // Iterate through the tiles array
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                // Create a new tile object
                tile = BasicObjectBuilders.loadTile(i, j);

                // Assign that tile object to the array position
                tiles[i][j] = tile;

                // Draw the tile on the front end
                BasicCommands.drawTile(out, tile, 0);


            }
        }

    }


    /**
     * Getter method to return tiles objects of a board
     *
     * @return Tile
     */

    public Tile[][] getTiles() {
        return this.tiles;
    }

    /** this method will take in an x and y parameter and return the tile object at that position
     * 
     * @param x
     * @param y
     * @return
     */
    public Tile returnTile(int x, int y) {
        return tiles[x][y];
    }

    
    /** This method will take in a tile and return an ArrayList of the two cardinal and 
     * one diagonal tiles available for a standard move in the game
     * @param out
     * @param tile
     * @return
     */
    public ArrayList<Tile> getAdjacentTiles(ActorRef out, Tile tile) {

        // arrayList to store the available tiles
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();

        // tile co-ordinates
        int x = tile.getTilex();
        int y = tile.getTiley();

        // for the cardinal tiles I will check if the tile if either 2 or 1 tiles away from the edge of the board so
        // we avoid any nullPointExceptions

        // checking for the top-most tiles
        if (y > 1) {
            adjacentTiles.add(returnTile(x, y-1));
            adjacentTiles.add(returnTile(x, y-2));
        } else if (y == 1) {
            adjacentTiles.add(returnTile(x, y-1));
        }

        // checking for the right-most tiles
        if (x < AppConstants.boardWidth - 2) {
            adjacentTiles.add(returnTile(x+1, y));
            adjacentTiles.add(returnTile(x+2, y));
        } else if (x == AppConstants.boardWidth - 2) {
            adjacentTiles.add(returnTile(x+1, y));
        }

        // checking for the bottom-most tiles
        if (y < AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x, y+1));
            adjacentTiles.add(returnTile(x, y+2));
        } else if (y == AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x, y+1));
        }

        // checking for the left-most tiles
        if (x > 1) {
            adjacentTiles.add(returnTile(x-1, y));
            adjacentTiles.add(returnTile(x-2, y));
        } else if (x == 1) {
            adjacentTiles.add(returnTile(x-1, y));
        }

        // top-right
        if (x < AppConstants.boardWidth - 1 && y > 0) {
            adjacentTiles.add(returnTile(x+1, y-1));
        }

        // bottom-right
        if (x < AppConstants.boardWidth - 1 && y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x+1, y+1));
        }

        // bottom-left
        if (x > 0 && y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x-1, y+1));
        }

        // top-left
        if (x > 0 && y > 0) {
            adjacentTiles.add(returnTile(x-1, y-1));
        }

        return adjacentTiles;
    }

    
   
    /** method to iterate through the arrayList of adjacent tiles and drawTile() with white highlighting
     * 
     * @param out
     * @param tiles
     */
    public void highlightTilesWhite(ActorRef out, ArrayList<Tile> tiles) {
        for (Tile tile : tiles) {
            BasicCommands.drawTile(out, tile, 1);
        }
    }

    /** method to iterate through the arrayList of adjacent tiles and drawTile() with red highlighting
     * 
     * @param out
     * @param tiles
     */
    public void highlightTilesRed(ActorRef out, ArrayList<Tile> tiles) {

        for (Tile tile : tiles) {
            BasicCommands.drawTile(out, tile, 2);
        }
    }
    
    
    public void addUnitToBoard(int x,int y,Unit unit) {
    	tiles[x][y].setUnitToTile(unit);
    }

	public void addDummyUnitsonBoard(ActorRef out) {
		// TODO Auto-generated method stub
		

		// Place a unit with attack:3 and health:2 at [3,2]
		Unit unit1 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 1, Unit.class);
		unit1.setAttack(3); 
		unit1.setHealth(2);
		addUnitToBoard(3, 2, unit1);
		
		unit1.setPositionByTile(tiles[3][2]); 	
		BasicCommands.drawUnit(out, unit1, tiles[3][2]);
		AppConstants.callSleep(100);
		
		BasicCommands.setUnitHealth(out, unit1, unit1.getHealth());
	    AppConstants.callSleep(100);
	        
	    BasicCommands.setUnitAttack(out, unit1, unit1.getAttack());
	    AppConstants.callSleep(100);
        AppConstants.printLog("------> addDummyUnitsonBoard :: Placed unit at [3,2]");


		
	}


}


