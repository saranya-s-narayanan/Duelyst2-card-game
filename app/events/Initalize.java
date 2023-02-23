package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
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
import structures.basic.BetterUnit;


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

        

        
        
        
        
        // ******************************* TESTS ******************************************
        // testing if the correct tiles are highlighted
       
        // AppConstants.printLog("------> Initialize :: Test:: Red Higlight..");
        // gameState.board.highlightTilesRed(out, gameState.board.getAdjacentTiles(out,gameState.board.returnTile(8 ,4)));
        
        // AppConstants.printLog("------> Initialize :: Test:: Adding dummy units on  board..");
		// gameState.board.addDummyUnitsonBoard(out);

		//************************************// HUMAN PLAYER //******************************************
		

        // creating the avatar object
        Unit avatar = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 1, Unit.class);

        // Change by using the returnTile method instead of creating tile objects here
        // placing avatar on board and setting stats
        gameState.avatar =  new BetterUnit(out,avatar, gameState.board.returnTile(1,2), gameState.board);
        avatar.setIsPlayer(1);
        // creating the player object and passing the avatar object to allow the players health to be set to the avatars.
        gameState.player1 = new Player(1,out,gameState.avatar,AppConstants.deck1Cards);
        gameState.player1.setCurrentXpos(1);
        gameState.player1.setCurrentYpos(2);
        AppConstants.callSleep(200);
        // AppConstants.printLog("------> Initialize :: Player 1 created!");

        gameState.player1Turn=true;
        // AppConstants.printLog("------> Initialize :: Set player 1 as activePlayer!");
		
        //print message to the terminal notifying the start of the draw card method
        // AppConstants.printLog("------> Initialize :: creating deck for player 1");
        //setting the deck as an ArrayList
        gameState.player1.createDeck();
        AppConstants.callSleep(200);
        // AppConstants.printLog("------> Initialize :: deck created");
        
        // AppConstants.printLog("------> Initialize :: Drawing 3 cards from the deck for player 1");
        //Setting the hand as an ArrayList
        gameState.player1.setHand(out,1);
        AppConstants.callSleep(200);
        // AppConstants.printLog("------> Initialize :: Card draw complete");

        gameState.board.addDummyUnitsonBoard(out);
        AppConstants.callSleep(200);



        //************************************// COMPUTER PLAYER //******************************************
		
		// creating ai avatar object
        Unit aiAvatar = BasicObjectBuilders.loadUnit(StaticConfFiles.aiAvatar, 2, Unit.class);

        // Change by using the returnTile method instead of creating tile objects here
        // placing avatar on board and setting stats
        gameState.aiAvatar = new BetterUnit(out, aiAvatar, gameState.board.returnTile(7,2), gameState.board);
        aiAvatar.setIsPlayer(2);
        // creating the player object and passing the avatar object to allow the players health to be set to the avatars.
        gameState.player2 = new ComputerPlayer(2,out, gameState.aiAvatar,AppConstants.deck2Cards);
        gameState.player2.setCurrentXpos(7);
        gameState.player2.setCurrentYpos(2);
        // AppConstants.printLog("------> Initialize :: Player 2 created!");
        AppConstants.callSleep(200);


        //print message to the terminal notifying the start of the draw card method
        // AppConstants.printLog("------> Initialize :: creating deck for AI");
        //setting the deck as an ArrayList
        gameState.player2.createDeck();
        AppConstants.callSleep(200);
        // AppConstants.printLog("------> Initialize :: deck created");
        
        // AppConstants.printLog("------> Initialize :: Drawing 3 cards from the deck for AI");
        //Setting the hand as an ArrayList
        gameState.player2.setHand(out,2);
        AppConstants.callSleep(200);
        // AppConstants.printLog("------> Initialize :: Card draw complete");
        // User 1 makes a change
        //CommandDemo.executeDemo(out); // this executes the command demo, comment out this when implementing your solution
        //CheckMoveLogic.executeDemo(out);
    }




}

