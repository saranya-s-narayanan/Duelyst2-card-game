package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;

// creating a computerPlayer class so we can add extra functionalities down the line
public class ComputerPlayer extends Player{

    int health;
    int mana;

    // constructor to create a player with set health and mana which calls setPlayer to place the data on the front end.
    public ComputerPlayer(ActorRef out, BetterUnit avatar) {
        super(out, avatar);
        this.health = avatar.getHealth();
        this.mana = 2; // this will be set to player turn +1 once we have player turn available
        setPlayer2(out);
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
    // Setting the player 2 health and mana on the front end.
    public void setPlayer2(ActorRef out){

        BasicCommands.setPlayer2Health(out, this);
        BasicCommands.setPlayer2Mana(out, this);

    }
}
