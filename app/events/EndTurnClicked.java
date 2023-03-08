package events;

import com.fasterxml.jackson.databind.JsonNode;

import akka.actor.ActorRef;
import structures.GameState;
import utils.AppConstants;
import commands.BasicCommands;

/**
 * Indicates that the user has clicked an object on the game canvas, in this case
 * the end-turn button.
 * 
 * { 
 *   messageType = “endTurnClicked”
 * }
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class EndTurnClicked implements EventProcessor{

	public JsonNode cardClick;//variable to hold the Json message that comes in when a click is made

	@Override
	public void processEvent(ActorRef out, GameState gameState, JsonNode message) {
		if(gameState.isGameActive){ // if the frontend connection is active
			cardClick=message.get("messagetype");//message to keep track of previous click on front-end
			AppConstants.printLog("------> previous message type:---->"+gameState.clickMessage);
			AppConstants.printLog("------> End turn:---->"+message.toPrettyString());

			if(gameState.clickMessage.asText().equals("cardclicked")){
				if(gameState.SummonTileList != null){//to check if cardClick happened
					if(gameState.player1Turn) OtherClicked.clearCardClicked(out, gameState, gameState.player1);//clear for player1
				}
			}
			else if(gameState.clickMessage.asText().equals("tileclicked")){//check if tile click happened
				gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
			}
			else if(gameState.clickMessage.asText().equals("initalize")){//check if initialise state but still clear highlight
				gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
			}
			
			if(gameState.player1Turn==true){//if it was player's turn then only they can use end turn
				
				endPlayer1Turn(out,gameState);
				//startPlayer2Turn(); // To do
			}
			else{//for when AI ends its turn
				
				endPlayer2Turn(out,gameState);
				//startPlayer1Turn(); // To do
			}
			
			// Update setmoved and setattacked property of summoned to false
			for(int i=0;i<gameState.summonedUnits.size();i++)
			{
				gameState.summonedUnits.get(i).setMoved(false);
				gameState.summonedUnits.get(i).setAttacked(false);

				if(gameState.summonedUnits.get(i).getId() == 7 || gameState.summonedUnits.get(i).getId() == 17 || gameState.summonedUnits.get(i).getId() == 26 || gameState.summonedUnits.get(i).getId() == 36) {
					gameState.summonedUnits.get(i).setAttackedOnce(false);
					gameState.summonedUnits.get(i).setAttackedTwice(false);
				}
			}
			
		}
	}

	private void endPlayer2Turn(ActorRef out, GameState gameState) {
		// TODO Auto-generated method stub
		AppConstants.printLog("------> End turn Clicked by the AI !");
		
		gameState.playerTurnNumber++;//incrementing the turn number the player will have
//		AppConstants.printLog("------> End turn Clicked by the AI :: player turn incremented !");
		
		gameState.player2.drawAnotherCard(out,2);//draw a card from the deck

		gameState.player2.setMana(0);//flushing the mana
//		AppConstants.printLog("------> End turn Clicked by the AI :: mana set to 0 !");
		
		gameState.player1.setMana(gameState.playerTurnNumber+1);//setting the mana to turn+1
//		AppConstants.printLog("------> End turn Clicked by the AI :: player mana set to Turn+1 !");
		
		gameState.player1Turn=true;//passing the turn to the player
//		AppConstants.printLog("------> End turn Clicked by the AI :: turn passed to player !");
		
		BasicCommands.addPlayer1Notification(out, "Your Turn", 2);
		gameState.player1.setPlayerMana(out);//updating mana on the front end
		gameState.player2.setPlayerMana(out);//updating mana on the front end
	}

	private void endPlayer1Turn(ActorRef out, GameState gameState) {
		// TODO Auto-generated method stub
		AppConstants.printLog("------> End turn Clicked by the player !");
		
		gameState.player2.setMana(gameState.compTurnNumber+1);//setting the mana to turn+1
//		AppConstants.printLog("------> End turn Clicked by the player :: AI mana set to Turn+1 !");
		
		gameState.compTurnNumber++;//incrementing the turn number the AI opponent will have
//		AppConstants.printLog("------> End turn Clicked by the player :: AI turn incremented !");
		
		gameState.player1.drawAnotherCard(out,1);//draw a card from the deck
//		AppConstants.printLog("------> End turn Clicked by the player :: Draw new card complete !");
		
		gameState.player1.setMana(0);//flushing the mana
//		AppConstants.printLog("------> End turn Clicked by the player :: mana set to 0 !");
		
		gameState.player1Turn=false;//passing the turn to AI
//		AppConstants.printLog("------> End turn Clicked by the player :: turn passed to AI !");
		
		BasicCommands.addPlayer1Notification(out, "Passing Turn Over", 2);
		
		gameState.player1.setPlayerMana(out);//updating mana on the front end
		gameState.player2.setPlayerMana(out);//updating mana on the front end 
		
		
		//<------------ DO AI LOGIC --------------->
		
		gameState.player2.startAIThread(out,gameState); // comment this for testing below code
		
//		gameState.player2.testMovement(out, gameState);
//		gameState.player2.testAttack(out, gameState);
		
//		gameState.player2.testCardSummon(out, gameState);

		
		
	}

}
