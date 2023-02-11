package structures;

import structures.basic.Board;

/**
 * This class can be used to hold information about the on-going game.
 * Its created with the GameActor.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class GameState {

	
	public boolean gameInitalised = false;
	public boolean isGameActive = false; // Variable used for checking active front end connection
	
	public Board board=null;
	public long lastHeartbeatTime=0; // The time of the latest heartbeat message reception
	
	
	
	
	
	
	
	
	
	
	
/** This method resets the state variable values to the default ones
 * 
 */
	public void clearStateVariables() {
		this.gameInitalised=false;
		this.isGameActive = false;
		this.board=null;
		this.lastHeartbeatTime=0;
		
	}
	
}



