package structures;

import structures.basic.BetterUnit;
import structures.basic.Board;
import structures.basic.ComputerPlayer;
import structures.basic.Player;
import structures.basic.Tile;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 *
 * @author Dr. Richard McCreadie
 */
public class GameState {


    public boolean gameInitalised = false;
    public boolean isGameActive = false; // Variable used for checking active front end connection

    public Board board = null;
    public Player player1 = null;

    public ComputerPlayer player2 = null;

    public BetterUnit avatar = null;

    public BetterUnit aiAvatar = null;
    public long lastHeartbeatTime = 0; // The time of the latest heartbeat message reception

    public boolean player1Turn=true;//varibale to check whose turn is it, true for player, false for AI
    public int playerTurnNumber =1;//variable to see how many turns had the player had
    public int compTurnNumber =1;//varibale to see how many turns had the AI had

    public boolean isGameOver = false; // Variable used for checking game ending
    public Tile startTile=null;
    public boolean startTrue=false;//boolean to know if we need to move or not
    
  
	/*
	 * public int getPlayerTurnNumber() {//for getting the player turn number return
	 * playerTurnNumber; } public void incrementPlayerTurn() {//for incrementing the
	 * player turn number this.playerTurnNumber++; } public void
	 * incrementCompTurn(){//for incrementing the AI turn number
	 * this.compTurnNumber++; } public int getCompTurnNumber() {//for getting the AI
	 * turn number return compTurnNumber; }
	 */


    /**
     * This method resets the state variable values to the default ones
     */
    public void clearStateVariables() {
        this.gameInitalised = false;
        this.isGameActive = false;
        this.board = null;
        this.player1 = null;
        this.player2 = null;
        this.avatar = null;
        this.aiAvatar = null;
        this.lastHeartbeatTime = 0;
        this.startTile=null;

    }

}



