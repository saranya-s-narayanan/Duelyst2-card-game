package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;//importing for cards in deck and hand
import java.util.*;

import java.util.ArrayList;

/**
 * A basic representation of of the Player. A player
 * has health and mana.
 * 
 * @author Dr. Richard McCreadie
 *
 */
public class Player {

	BetterUnit avatar;
	int playerID; // 1=player1, 2= computerPlayer
	int health;
	int mana;
	int cardID;//variable to set card id
	int position = 1;//variable to set card position in hand
	
	boolean highlighted=false; 
	

	String[] cardsFiles; //  of cards 
	
//	int currentXpos=0,currentYpos=0;

	public List<Card> deck;// deck of card
	public List<Card> hand;// hand containing card

	/** constructor to create a player with set health and mana which calls setPlayer to place the data on the front end.
	 * 
	 * @param playerID
	 * @param out
	 * @param avatar
	 * @param cardsdeck
	 */
	public Player(int playerID, ActorRef out, BetterUnit avatar, String[] cardsFiles) {
		this.avatar = avatar;
		this.playerID=playerID;
		this.health = avatar.getHealth();
		this.mana = 2; // this will be set to player turn +1 once we have player turn available
		this.cardsFiles=cardsFiles;
		this.cardID=0;
		this.hand= new ArrayList<Card>();
		this.deck = new ArrayList<Card>();
		setPlayer(out);
	}
	public Player(int health, int mana) {
		super();
		this.health = health;
		this.mana = mana;
	}
	
	public BetterUnit getAvatar() {
		return avatar;
	}
	public void setAvatar(BetterUnit avatar) {
		this.avatar = avatar;
	}
	public int getID() {
		return playerID;
	}
	public void setID(int playerID) {
		this.playerID = playerID;
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
	
	public boolean getHighlighted() {
		return highlighted;
	}
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	
	
	
		
//	public int getCurrentXpos() {
//		return currentXpos;
//	}
//	public void setCurrentXpos(int currentXpos) {
//		this.currentXpos = currentXpos;
//	}
//	public int getCurrentYpos() {
//		return currentYpos;
//	}
//	public void setCurrentYpos(int currentYpos) {
//		this.currentYpos = currentYpos;
//	}

	// This method syncs up the Player health with the health of their Avatar
	public void syncHealth() {
		this.health = this.avatar.getHealth();
	}

	//method to get card by hand position
	public Card getCardByHandPos(int pos) {
        return hand.get(pos);
    }
	
	/** Setting the player health on the front end
	 * 
	 * @param out
	 */
	public void setPlayerHealth(ActorRef out) {
		if(playerID==1)
		{
			BasicCommands.setPlayer1Health(out, this);
			AppConstants.callSleep(100);
		
		}else {
			 BasicCommands.setPlayer2Health(out, this);
			 AppConstants.callSleep(100);
		}
	}
	
	/** Setting the player mana on the front end
	 * 
	 * @param out
	 */
	public void setPlayerMana(ActorRef out){
		if(playerID==1)
		{	
			BasicCommands.setPlayer1Mana(out, this);
			AppConstants.callSleep(100);
		}else {
		     BasicCommands.setPlayer2Mana(out, this);
			 AppConstants.callSleep(100);
		}
	}
	
	/** Setting the player health and mana on the front end
	 * 
	 * @param out
	 */
	public void setPlayer(ActorRef out){
		if(playerID==1)
		{
			BasicCommands.setPlayer1Mana(out, this);
			AppConstants.callSleep(100);
			
			BasicCommands.setPlayer1Health(out, this);
			AppConstants.callSleep(100);
		}else {
		     BasicCommands.setPlayer2Mana(out, this);
			 AppConstants.callSleep(100);
			 
			 BasicCommands.setPlayer2Health(out, this);
			 AppConstants.callSleep(100);
		}
	}

	//method to create the deck of card for player 1
	public void createDeck() {
		for(int j=0;j<cardsFiles.length;j++){
			Card card = BasicObjectBuilders.loadCard(cardsFiles[j], cardID, Card.class);
			cardID++;
			deck.add(j, card);
			// AppConstants.printLog("Card " + deck.get(j).getCardname() + " added to deck" + "at position "+ j);
		}
		for(int j=0;j<cardsFiles.length;j++){
			Card card = BasicObjectBuilders.loadCard(cardsFiles[j], cardID, Card.class);
			cardID++;
			deck.add((10+j), card);
			// AppConstants.printLog("Card " + deck.get((10+j)).getCardname() + " added to deck"+ "at position "+ (10+j));
		}
	}

	//method to get total cards in the deck
	public int getCardInDeck(){
		return deck.size();
	}

	//method to get total cards in hand
	public int getCardInHand() {
		return hand.size();
	}
	
	/** This method sets the hand of the corresponding player object
	 * 
	 * @param out
	 */
    public void setHand(ActorRef out, int playerID) {
        for(int i=0;i<AppConstants.minCardsInHand;i++){
			//move the top card from deck to hand
			hand.add(i, deck.get(0));
			// System.out.println("Card " + deck.get(0).getCardname() + " removing from deck");
			deck.remove(0);
			// System.out.println("Card " + hand.get(i).getCardname() + " added to hand");
			if(playerID==1){
				// drawCard [i]
				BasicCommands.drawCard(out, hand.get(i), position, 0);
				AppConstants.callSleep(500);
				// increment the position
				position++;
			}
            
        }
    }


    /** This method draws a card from the deck and adds that card to the hand
     * of the corresponding player object
     * 
     * @param out
     */
    
	public void drawAnotherCard(ActorRef out, int playerID) {
		if(position<=AppConstants.maxCardsInHand){
			//move the top card from deck to hand
			hand.add(position-1, deck.get(0));
			deck.remove(0);
			if(playerID==1){
				//draw the card
				BasicCommands.drawCard(out, hand.get(position-1) , position, 0);
				AppConstants.callSleep(500);
				//increment the position
				position++;
			}
			
		}
		else {
			// AppConstants.printLog("------> drawAnotherCard P1:: but the hand positions are full !, deck size: "+deck.size());
			if(deck.size()>0)
			{
				if(playerID==1){
					BasicCommands.addPlayer1Notification(out, "Hand positions are full", 2);
					// AppConstants.printLog("------> drawAnotherCard P1:: card to be burned at position: "+ position);
					//deck.remove(position); //--> was creating outOfbound exception
					deck.remove(0);
					// AppConstants.printLog("------> drawAnotherCard P1:: card burn complted!");
					AppConstants.callSleep(500);
				}
				else{
					// AppConstants.printLog("------> drawAnotherCard AI:: card to be burn at position: "+ position);
					//deck.remove(position);
					deck.remove(0);
					// AppConstants.printLog("------> drawAnotherCard AI:: card burn complted!");
					AppConstants.callSleep(500);
				}
			}else {
				// To do deck empty scenario
			}
				
		}
		
	}

	
	
	
}
