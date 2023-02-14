package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import commands.BasicCommands;
import demo.CheckMoveLogic;
import demo.CommandDemo;
import structures.GameState;
import structures.basic.*;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;
import structures.basic.Board;
import structures.basic.Player;
import structures.basic.Tile;


/**
 * Indicates that both the core game loop in the browser is starting, meaning
 * that it is ready to recieve commands from the back-end.
 * <p>
 * {
 * messageType = “initalize”
 * }
 *
 * @author Dr. Richard McCreadie
 */
public class Initalize implements EventProcessor {

    @Override
    public void processEvent(ActorRef out, GameState gameState, JsonNode message) {

        gameState.gameInitalised = true;

        AppConstants.printLog("------> Initialize :: creating board..");

		// Create a board object and assign it to the gameState board object
        gameState.board = new Board(out);


        AppConstants.printLog("------> Initialize :: Board created !");


        // creating the avatar object
		gameState.avatar = new BetterUnit(out);

        // creating the player object and passing the avatar object to allow the players health to be set to the avatars.
        gameState.player1 = new Player(out,gameState.avatar);






        // User 1 makes a change
        //CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
        //CheckMoveLogic.executeDemo(out);
    }




}


