package structures.basic;

import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class BetterUnit extends Unit {

    Set<String> keywords;

    int health;
    int attack;


    public BetterUnit(ActorRef out) {
        //avatar object
        this.health = 20;
        this.attack = 2;

        setAvatar(out);

    }

    public BetterUnit(Set<String> keywords) {
        super();
        this.keywords = keywords;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    public void setAvatar(ActorRef out) {
        // creates the player1 avatar object
        Unit avatar = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);

        // load tile for avatar placement
        Tile tile = BasicObjectBuilders.loadTile(1, 2);

        // Draw the avatar
        avatar.setPositionByTile(tile);
        BasicCommands.drawUnit(out, avatar, tile);

        //seems to not set health and attack without a sleep
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BasicCommands.setUnitHealth(out, avatar, getHealth());
        BasicCommands.setUnitAttack(out, avatar, getAttack());

    }
    
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public static void main(String[] args) {
        // not sure why this is all here in main??

//        BetterUnit unit = (BetterUnit) BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, BetterUnit.class);
//        Set<String> keywords = new HashSet<String>();
//        keywords.add("MyKeyword");
//        unit.setKeywords(keywords);
//
//        System.err.println(unit.getClass());

    }
}
