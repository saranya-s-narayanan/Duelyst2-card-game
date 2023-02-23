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
<<<<<<< app/structures/basic/Player.java
=======
	boolean moved=false; // variable to check whether the player has already moved or not
	boolean attacked=false; // variable to check whether the player has already attacked other units or not
>>>>>>> app/structures/basic/Player.java

	String[] cardsFiles; //  of cards 
	
	int currentXpos=0,currentYpos=0;

	public List<Card> deck;// deck of cards
	public List<Card> hand;// hand containing cards

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

	// Two lists to store the Loaded units and set their health and attack
		public List<Unit> createPlayer1Units(ActorRef out) {
			List <Unit> player1Units = new ArrayList<Unit>();

			Unit comodoCharger = BasicObjectBuilders.loadUnit(StaticConfFiles.u_comodo_charger, 0, Unit.class);
			comodoCharger.setIsPlayer(1);
			comodoCharger.setHealth(3);
			comodoCharger.setAttack(1);
			player1Units.add(comodoCharger);
			Unit pureBladeEnforcer = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pureblade_enforcer, 1, Unit.class);
			pureBladeEnforcer.setIsPlayer(1);
			pureBladeEnforcer.setHealth(4);
			pureBladeEnforcer.setAttack(1);
			player1Units.add(pureBladeEnforcer);
			Unit fireSpitter = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 2, Unit.class);
			fireSpitter.setIsPlayer(1);
			fireSpitter.setHealth(2);
			fireSpitter.setAttack(3);
			player1Units.add(fireSpitter);
			Unit silverguardKnight = BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight, 3, Unit.class);
			silverguardKnight.setIsPlayer(1);
			silverguardKnight.setHealth(5);
			silverguardKnight.setAttack(1);
			player1Units.add(silverguardKnight);
			Unit azureHerald = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azure_herald, 5, Unit.class);
			azureHerald.setIsPlayer(1);
			azureHerald.setHealth(4);
			azureHerald.setAttack(1);
			player1Units.add(azureHerald);
			Unit ironcliffGuardian = BasicObjectBuilders.loadUnit(StaticConfFiles.u_ironcliff_guardian, 6, Unit.class);
			ironcliffGuardian.setIsPlayer(1);
			ironcliffGuardian.setHealth(10);
			ironcliffGuardian.setAttack(3);
			player1Units.add(ironcliffGuardian);
			Unit azuriteLion = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azurite_lion, 7, Unit.class);
			azuriteLion.setIsPlayer(1);
			azuriteLion.setHealth(3);
			azuriteLion.setAttack(2);
			player1Units.add(azuriteLion);
			Unit hailstoneGolem = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem, 9, Unit.class);
			hailstoneGolem.setIsPlayer(1);
			hailstoneGolem.setHealth(6);
			hailstoneGolem.setAttack(4);
			player1Units.add(hailstoneGolem);

			return player1Units;
		}
		public List<Unit> createPlayer2Units(ActorRef out) {

			List <Unit> player2Units = new ArrayList<Unit>();

			Unit rockPulveriser = BasicObjectBuilders.loadUnit(StaticConfFiles.u_rock_pulveriser, 20, Unit.class);
			rockPulveriser.setIsPlayer(2);
			rockPulveriser.setHealth(4);
			rockPulveriser.setAttack(1);
			player2Units.add(rockPulveriser);
			Unit bloodshardGolem = BasicObjectBuilders.loadUnit(StaticConfFiles.u_bloodshard_golem, 21, Unit.class);
			bloodshardGolem.setIsPlayer(2);
			bloodshardGolem.setHealth(3);
			bloodshardGolem.setAttack(4);
			player2Units.add(bloodshardGolem);
			Unit blazeHound = BasicObjectBuilders.loadUnit(StaticConfFiles.u_blaze_hound, 23, Unit.class);
			blazeHound.setIsPlayer(2);
			blazeHound.setHealth(3);
			blazeHound.setAttack(4);
			player2Units.add(blazeHound);
			Unit windshrike = BasicObjectBuilders.loadUnit(StaticConfFiles.u_windshrike, 24, Unit.class);
			windshrike.setIsPlayer(2);
			windshrike.setHealth(3);
			windshrike.setAttack(4);
			player2Units.add(windshrike);
			Unit pyromancer = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 25, Unit.class);
			pyromancer.setIsPlayer(2);
			pyromancer.setHealth(1);
			pyromancer.setAttack(2);
			player2Units.add(pyromancer);
			Unit serpenti = BasicObjectBuilders.loadUnit(StaticConfFiles.u_serpenti, 26, Unit.class);
			serpenti.setIsPlayer(2);
			serpenti.setHealth(4);
			serpenti.setAttack(7);
			player2Units.add(serpenti);
			Unit planarScout = BasicObjectBuilders.loadUnit(StaticConfFiles.u_planar_scout, 28, Unit.class);
			planarScout.setIsPlayer(2);
			planarScout.setHealth(1);
			planarScout.setAttack(2);
			player2Units.add(planarScout);
			Unit hailstoneGolemR = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golemR, 29, Unit.class);
			hailstoneGolemR.setIsPlayer(2);
			hailstoneGolemR.setHealth(6);
			hailstoneGolemR.setAttack(4);
			player2Units.add(hailstoneGolemR);

			return player2Units;

		}

		// method to draw the unit to the board and set the front end attack and health
		public void drawUnitToBoard(ActorRef out, Unit unit,Tile tile) {

			BasicCommands.drawUnit(out, unit, tile);
			AppConstants.callSleep(100);
			BasicCommands.setUnitHealth(out, unit, unit.getHealth());
			AppConstants.callSleep(100);
			BasicCommands.setUnitAttack(out, unit, unit.getAttack());
			AppConstants.callSleep(100);

		}
		public Card getCardByHandPos(int i) {
			return hand.get(i);
		}
	
	/** This method check whether the player's avatar occupies the given tile or not
	 * 
	 * @param tilex
	 * @param tiley
	 * @return
	 */
	public boolean isAvatarOnTile(int tilex, int tiley) {
		// TODO Auto-generated method stub
		AppConstants.printLog("------> isAvatarOnTile:: POS: "+currentXpos+","+currentYpos);

		if (currentXpos==tilex && currentYpos==tiley) // occupied by avatar
			return true;
		else
			return false;
	}

	// Two lists to store the Loaded units and set their health and attack
	public List<Unit> createPlayer1Units(ActorRef out) {
		List <Unit> player1Units = new ArrayList<Unit>();

		Unit comodoCharger = BasicObjectBuilders.loadUnit(StaticConfFiles.u_comodo_charger, 0, Unit.class);
		comodoCharger.setIsPlayer(1);
		comodoCharger.setHealth(3);
		comodoCharger.setAttack(1);
		player1Units.add(comodoCharger);
		Unit pureBladeEnforcer = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pureblade_enforcer, 1, Unit.class);
		pureBladeEnforcer.setIsPlayer(1);
		pureBladeEnforcer.setHealth(4);
		pureBladeEnforcer.setAttack(1);
		player1Units.add(pureBladeEnforcer);
		Unit fireSpitter = BasicObjectBuilders.loadUnit(StaticConfFiles.u_fire_spitter, 2, Unit.class);
		fireSpitter.setIsPlayer(1);
		fireSpitter.setHealth(2);
		fireSpitter.setAttack(3);
		player1Units.add(fireSpitter);
		Unit silverguardKnight = BasicObjectBuilders.loadUnit(StaticConfFiles.u_silverguard_knight, 3, Unit.class);
		silverguardKnight.setIsPlayer(1);
		silverguardKnight.setHealth(5);
		silverguardKnight.setAttack(1);
		player1Units.add(silverguardKnight);
		Unit azureHerald = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azure_herald, 5, Unit.class);
		azureHerald.setIsPlayer(1);
		azureHerald.setHealth(4);
		azureHerald.setAttack(1);
		player1Units.add(azureHerald);
		Unit ironcliffGuardian = BasicObjectBuilders.loadUnit(StaticConfFiles.u_ironcliff_guardian, 6, Unit.class);
		ironcliffGuardian.setIsPlayer(1);
		ironcliffGuardian.setHealth(10);
		ironcliffGuardian.setAttack(3);
		player1Units.add(ironcliffGuardian);
		Unit azuriteLion = BasicObjectBuilders.loadUnit(StaticConfFiles.u_azurite_lion, 7, Unit.class);
		azuriteLion.setIsPlayer(1);
		azuriteLion.setHealth(3);
		azuriteLion.setAttack(2);
		player1Units.add(azuriteLion);
		Unit hailstoneGolem = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golem, 9, Unit.class);
		hailstoneGolem.setIsPlayer(1);
		hailstoneGolem.setHealth(6);
		hailstoneGolem.setAttack(4);
		player1Units.add(hailstoneGolem);

		return player1Units;
	}
	public List<Unit> createPlayer2Units(ActorRef out) {

		List <Unit> player2Units = new ArrayList<Unit>();

		Unit rockPulveriser = BasicObjectBuilders.loadUnit(StaticConfFiles.u_rock_pulveriser, 20, Unit.class);
		rockPulveriser.setIsPlayer(2);
		rockPulveriser.setHealth(4);
		rockPulveriser.setAttack(1);
		player2Units.add(rockPulveriser);
		Unit bloodshardGolem = BasicObjectBuilders.loadUnit(StaticConfFiles.u_bloodshard_golem, 21, Unit.class);
		bloodshardGolem.setIsPlayer(2);
		bloodshardGolem.setHealth(3);
		bloodshardGolem.setAttack(4);
		player2Units.add(bloodshardGolem);
		Unit blazeHound = BasicObjectBuilders.loadUnit(StaticConfFiles.u_blaze_hound, 23, Unit.class);
		blazeHound.setIsPlayer(2);
		blazeHound.setHealth(3);
		blazeHound.setAttack(4);
		player2Units.add(blazeHound);
		Unit windshrike = BasicObjectBuilders.loadUnit(StaticConfFiles.u_windshrike, 24, Unit.class);
		windshrike.setIsPlayer(2);
		windshrike.setHealth(3);
		windshrike.setAttack(4);
		player2Units.add(windshrike);
		Unit pyromancer = BasicObjectBuilders.loadUnit(StaticConfFiles.u_pyromancer, 25, Unit.class);
		pyromancer.setIsPlayer(2);
		pyromancer.setHealth(1);
		pyromancer.setAttack(2);
		player2Units.add(pyromancer);
		Unit serpenti = BasicObjectBuilders.loadUnit(StaticConfFiles.u_serpenti, 26, Unit.class);
		serpenti.setIsPlayer(2);
		serpenti.setHealth(4);
		serpenti.setAttack(7);
		player2Units.add(serpenti);
		Unit planarScout = BasicObjectBuilders.loadUnit(StaticConfFiles.u_planar_scout, 28, Unit.class);
		planarScout.setIsPlayer(2);
		planarScout.setHealth(1);
		planarScout.setAttack(2);
		player2Units.add(planarScout);
		Unit hailstoneGolemR = BasicObjectBuilders.loadUnit(StaticConfFiles.u_hailstone_golemR, 29, Unit.class);
		hailstoneGolemR.setIsPlayer(2);
		hailstoneGolemR.setHealth(6);
		hailstoneGolemR.setAttack(4);
		player2Units.add(hailstoneGolemR);

		return player2Units;

	}

	// method to draw the unit to the board and set the front end attack and health
	public void drawUnitToBoard(ActorRef out, Unit unit,Tile tile) {

		BasicCommands.drawUnit(out, unit, tile);
		AppConstants.callSleep(100);
		BasicCommands.setUnitHealth(out, unit, unit.getHealth());
		AppConstants.callSleep(100);
		BasicCommands.setUnitAttack(out, unit, unit.getAttack());
		AppConstants.callSleep(100);

	}
	
	
}
