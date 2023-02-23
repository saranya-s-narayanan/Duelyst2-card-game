package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

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

    /**
     * this method will take in an x and y parameter and return the tile object at that position
     *
     * @param x
     * @param y
     * @return
     */
    public Tile returnTile(int x, int y) {
        return tiles[x][y];
    }


    /**
     * This method will take in a tile and return an ArrayList of the two cardinal and
     * one diagonal tiles available for a standard move in the game
     *
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
            adjacentTiles.add(returnTile(x, y - 1));
            adjacentTiles.add(returnTile(x, y - 2));
        } else if (y == 1) {
            adjacentTiles.add(returnTile(x, y - 1));
        }

        // checking for the right-most tiles
        if (x < AppConstants.boardWidth - 2) {
            adjacentTiles.add(returnTile(x + 1, y));
            adjacentTiles.add(returnTile(x + 2, y));
        } else if (x == AppConstants.boardWidth - 2) {
            adjacentTiles.add(returnTile(x + 1, y));
        }

        // checking for the bottom-most tiles
        if (y < AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x, y + 1));
            adjacentTiles.add(returnTile(x, y + 2));
        } else if (y == AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x, y + 1));
        }

        // checking for the left-most tiles
        if (x > 1) {
            adjacentTiles.add(returnTile(x - 1, y));
            adjacentTiles.add(returnTile(x - 2, y));
        } else if (x == 1) {
            adjacentTiles.add(returnTile(x - 1, y));
        }

        // top-right
        if (x < AppConstants.boardWidth - 1 && y > 0) {
            adjacentTiles.add(returnTile(x + 1, y - 1));
        }

        // bottom-right
        if (x < AppConstants.boardWidth - 1 && y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x + 1, y + 1));
        }

        // bottom-left
        if (x > 0 && y < AppConstants.boardHeight - 1) {
            adjacentTiles.add(returnTile(x - 1, y + 1));
        }

        // top-left
        if (x > 0 && y > 0) {
            adjacentTiles.add(returnTile(x - 1, y - 1));
        }

        return adjacentTiles;
    }


    /**
     * This method will take in a tile and return an ArrayList of the two cardinal and
     * one diagonal tiles available for a standard attack in the game
     *
     * @param out
     * @param tile
     * @return
     */
    public ArrayList<Tile> getAdjacentTilesToAttack(ActorRef out, Tile tile) {

        // arrayList to store the available tiles
        ArrayList<Tile> adjacentTiles = new ArrayList<Tile>();

        // tile co-ordinates
        int x = tile.getTilex();
        int y = tile.getTiley();


        // checking for the top-most tiles
        if (y > 0) {
            adjacentTiles.add(returnTile(x, y - 1));
        }

        // checking for the right-most tiles
        if (x < AppConstants.boardWidth - 2) {
            adjacentTiles.add(returnTile(x + 1, y));
        }

        // checking for the bottom-most tiles
        if (y < AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x, y + 1));
        }

        // checking for the left-most tiles
        if (x > 0) {
            adjacentTiles.add(returnTile(x - 1, y));
        }

        // top-right
        if (x < AppConstants.boardWidth - 2 && y > 0) {
            adjacentTiles.add(returnTile(x + 1, y - 1));
        }

        // bottom-right
        if (x < AppConstants.boardWidth - 2 && y < AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x + 1, y + 1));
        }

        // bottom-left
        if (x > 0 && y < AppConstants.boardHeight - 2) {
            adjacentTiles.add(returnTile(x - 1, y + 1));
        }

        // top-left
        if (x > 0 && y > 0) {
            adjacentTiles.add(returnTile(x - 1, y - 1));
        }

        return adjacentTiles;
    }


    /**
     * method to iterate through the arrayList of adjacent tiles and drawTile() with white highlighting
     *
     * @param out
     * @param tiles
     */
    public void highlightTilesWhite(ActorRef out, ArrayList<Tile> tiles) {
        for (Tile tile : tiles) {
            if (tile.getUnitFromTile() == null)  // i added this condition as tiles with units should never be highlighted in white only red
                BasicCommands.drawTile(out, tile, 1);
        }
    }

    /**
     * method to iterate through the arrayList of adjacent tiles and drawTile() with red highlighting
     *
     * @param out
     * @param tiles
     */
    public void highlightTilesRed(ActorRef out, ArrayList<Tile> tiles) {

        for (Tile tile : tiles) {
            BasicCommands.drawTile(out, tile, 2);
        }
    }

    // public void clearTileHighlighting(ActorRef out, Board board) {  // method to clear the highlighted tiles
    //     for (int i = 0; i < AppConstants.boardWidth; i++) {
    //         for (int j = 0; j < AppConstants.boardHeight; j++) {
    //             BasicCommands.drawTile(out, board.tiles[i][j], 0);
    //         }
    //     }

    // }
    public void clearTileHighlighting(ActorRef out, ArrayList<Tile> tiles) {  // method to clear the highlighted tiles changed to git rid of BufferOverflow Exception
        for (Tile tile : tiles) {
            BasicCommands.drawTile(out, tile, 0);
        }

    }

    public void addUnitToBoard(int x, int y, Unit unit) {
        tiles[x][y].setUnitToTile(unit);

    }

    public void addDummyUnitsonBoard(ActorRef out, GameState gameState) {
        // TODO Auto-generated method stub


        // Place a unit with attack:3 and health:2 at [2,2]
        int x = 2, y = 2;
        Unit unit1 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, gameState.summonedUnits.size()+1, Unit.class); // changed ID to 3, as every unit must have a unique ID
        unit1.setAttack(3);
        unit1.setHealth(2);
        unit1.setIsPlayer(1); // set to player 1
        addUnitToBoard(x, y, unit1);
        
        gameState.summonedUnits.add(unit1);

        unit1.setPositionByTile(tiles[x][y]);
        BasicCommands.drawUnit(out, unit1, tiles[x][y]);
        AppConstants.callSleep(100);

        BasicCommands.setUnitHealth(out, unit1, unit1.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, unit1, unit1.getAttack());
        AppConstants.callSleep(100);
        AppConstants.printLog("------> addDummyUnitsonBoard :: Placed unit at [2,2]");
        
     // Place a unit with attack:3 and health:2 at [2,1]
        x = 2;
        y = 1;
        unit1 = BasicObjectBuilders.loadUnit(StaticConfFiles.u_serpenti, gameState.summonedUnits.size()+1, Unit.class); // changed ID to 3, as every unit must have a unique ID
        unit1.setAttack(2);
        unit1.setHealth(1);
        unit1.setIsPlayer(1); // set to player 1
        addUnitToBoard(x, y, unit1);
        
        gameState.summonedUnits.add(unit1); //add unit to arraylist

        unit1.setPositionByTile(tiles[x][y]);
        BasicCommands.drawUnit(out, unit1, tiles[x][y]);
        AppConstants.callSleep(100);

        BasicCommands.setUnitHealth(out, unit1, unit1.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, unit1, unit1.getAttack());
        AppConstants.callSleep(100);
        AppConstants.printLog("------> addDummyUnitsonBoard :: Placed unit at [2,1]");


    }

    // check whether a tile has a unit on it and returns a list of tiles occupied by units
    // i have added the condition of unit id and player id being the same as for now at least i cannot access only the player1's units without a different method
    // so i have set player1 units and player 1 id to 1 and same for player2 to 2.
    public ArrayList<Tile> getTilesWithUnits(ActorRef out, Tile[][] tiles, Player player) {

        ArrayList<Tile> tilesWithUnits = new ArrayList<>();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                Tile tile = tiles[i][j];
                if (tile.getUnitFromTile() != null ) {
                    if(tile.getUnitFromTile().getIsPlayer() == player.getID()) {
                        tilesWithUnits.add(tile);
                    }
                }
            }
        }

        return tilesWithUnits;
    }


}


