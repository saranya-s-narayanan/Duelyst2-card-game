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

	int health;
	int mana;
	int cardID=0;//variable to set card id
	int position = 1;//variable to set card position in hand


	// constructor to create a player with set health and mana which calls setPlayer to place the data on the front end.
	public Player(ActorRef out, BetterUnit avatar) {

		this.health = avatar.getHealth();
		this.mana = 2; // this will be set to player turn +1 once we have player turn available
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
	// Setting the player 1 health and mana on the front end.
	public void setPlayer(ActorRef out){

		BasicCommands.setPlayer1Health(out, this);
		AppConstants.callSleep(100);
		
		BasicCommands.setPlayer1Mana(out, this);
		AppConstants.callSleep(100);


	}


	//string array of player 1 deck
	String[] deck1Cards = {
		StaticConfFiles.c_azure_herald,
		StaticConfFiles.c_azurite_lion,
		StaticConfFiles.c_comodo_charger,
		StaticConfFiles.c_fire_spitter,
		StaticConfFiles.c_hailstone_golem,
		StaticConfFiles.c_ironcliff_guardian,
		StaticConfFiles.c_pureblade_enforcer,
		StaticConfFiles.c_silverguard_knight,
		StaticConfFiles.c_sundrop_elixir,
		StaticConfFiles.c_truestrike
	};
	

	
    //method to set hand
    public void setHand(ActorRef out) {
        for(int i=0;i<AppConstants.minCardsInHand;i++){
            // drawCard [i]
        Card card = BasicObjectBuilders.loadCard(deck1Cards[i], cardID, Card.class);
        BasicCommands.drawCard(out, card, position, 0);

		AppConstants.callSleep(500);

		//increment the card id and position
        cardID++;
		position++;
        }
    }

	public void drawAnotherCard(ActorRef out) {
		if(position<=AppConstants.maxCardsInHand){
			Card card = BasicObjectBuilders.loadCard(deck1Cards[cardID], cardID, Card.class);
        	BasicCommands.drawCard(out, card, position, 0);

    		AppConstants.callSleep(500);

			//increment the card id
			cardID++;
			position++;
		}
		else {
			AppConstants.printLog("------> End turn Clicked:: but the hand positions are full !");
			BasicCommands.addPlayer1Notification(out, "Hand positions are full", 2);
		}
		
	}
}
