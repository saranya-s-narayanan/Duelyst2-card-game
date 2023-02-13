package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.BasicObjectBuilders;

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
	
	public Player(ActorRef out) {
		super();
		this.health = 20;
		this.mana = 0;
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

	public void setPlayer(ActorRef out){

		BasicCommands.setPlayer1Health(out, this);
		BasicCommands.setPlayer1Mana(out, this);
		BasicCommands.addPlayer1Notification(out, "Test", 5);
	}
	
	
}
