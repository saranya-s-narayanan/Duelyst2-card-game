package structures;

import structures.basic.BetterUnit;
import structures.basic.Board;
import structures.basic.ComputerPlayer;
import structures.basic.Player;

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

    public int activePlayer=1; // Variable to keep track of the actove player
    
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

    }

}



