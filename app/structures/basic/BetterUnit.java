package structures.basic;

import java.util.EventListener;
import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class BetterUnit extends Unit {

    Set<String> keywords;

    int health;
    int attack;


    public BetterUnit(ActorRef out,Unit unit, Tile tile) {
        //avatar object
        this.health = AppConstants.playerMaxHealth;
        this.attack = 2;

        setAvatar(out,unit, tile);


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

    public void setAvatar(ActorRef out, Unit unit, Tile tile) {
        // creates the player1 avatar object
//        Unit avatar = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);



        // Draw the avatar
        unit.setPositionByTile(tile);
        BasicCommands.drawUnit(out, unit, tile);

       AppConstants.callSleep(100);

        BasicCommands.setUnitHealth(out, unit, getHealth());
        AppConstants.callSleep(100);
        
        BasicCommands.setUnitAttack(out, unit, getAttack());
        AppConstants.callSleep(100);


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
