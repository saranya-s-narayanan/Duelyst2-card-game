package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;//importing for cards in deck and hand

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Player {

	int playerID=1; // 1=player1, 2= computerPlayer
	int health;
	int mana;
	int cardID=0;//variable to set card id
	int position = 1;//variable to set card position in hand
	
	boolean moved=false; // variable to check whether the player has already moved or not
	boolean attacked=false; // variable to check whether the player has already attacked other units or not

	String[] cardsDeck; // deck of cards 
	
	int currentXpos=0,currentYpos=0;

	/** constructor to create a player with set health and mana which calls setPlayer to place the data on the front end.
	 * 
	 * @param playerID
	 * @param out
	 * @param avatar
	 * @param cardsDeck
	 */
	public Player(int playerID, ActorRef out, BetterUnit avatar, String[] cardsDeck) {
		this.playerID=playerID;
		this.health = avatar.getHealth();
		this.mana = 2; // this will be set to player turn +1 once we have player turn available
		this.cardsDeck=cardsDeck;
		setPlayer(out);
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		this.health = health;
	}
	public int getMana() {
		return mana;
	}
	public void setMana(int mana) {
		this.mana = mana;
	}
	
	public boolean getMoved() {
		return moved;
	}
	public void setMoved(boolean moved) {
		this.moved = moved;
	}
	
	public boolean getAttacked() {
		return attacked;
	}
	public void setAttacked(boolean attacked) {
		this.attacked = attacked;
	}
		
	public int getCurrentXpos() {
		return currentXpos;
	}
	public void setCurrentXpos(int currentXpos) {
		this.currentXpos = currentXpos;
	}
	public int getCurrentYpos() {
		return currentYpos;
	}
	public void setCurrentYpos(int currentYpos) {
		this.currentYpos = currentYpos;
	}
	
	
	/** Setting the player health and mana on the front end
	 * 
	 * @param out
	 */
	public void setPlayer(ActorRef out){

		if(playerID==1)
		{
			BasicCommands.setPlayer1Health(out, this);
			AppConstants.callSleep(100);
		
			BasicCommands.setPlayer1Mana(out, this);
			AppConstants.callSleep(100);
		}else {
			 BasicCommands.setPlayer2Health(out, this);
			 AppConstants.callSleep(100);

		     BasicCommands.setPlayer2Mana(out, this);
			 AppConstants.callSleep(100);
		}


	}


	
	/** This method sets the hand of the corresponding player object
	 * 
	 * @param out
	 */
    public void setHand(ActorRef out) {
        for(int i=0;i<AppConstants.minCardsInHand;i++){
            // drawCard [i]
        Card card = BasicObjectBuilders.loadCard(cardsDeck[i], cardID, Card.class);
        BasicCommands.drawCard(out, card, position, 0);

		AppConstants.callSleep(500);

		//increment the card id and position
        cardID++;
		position++;
        }
    }

    /** This method draws a card from the deck and adds that card to the hand
     * of the corresponding player object
     * 
     * @param out
     */
    
	public void drawAnotherCard(ActorRef out) {
		if(position<=AppConstants.maxCardsInHand){
			Card card = BasicObjectBuilders.loadCard(cardsDeck[cardID], cardID, Card.class);
        	BasicCommands.drawCard(out, card, position, 0);

    		AppConstants.callSleep(500);

			//increment the card id
			cardID++;
			position++;
		}
		else {
			AppConstants.printLog("------> drawAnotherCard:: but the hand positions are full !");
			if(playerID==1)
				BasicCommands.addPlayer1Notification(out, "Hand positions are full", 2);
		}
		
	}
}
