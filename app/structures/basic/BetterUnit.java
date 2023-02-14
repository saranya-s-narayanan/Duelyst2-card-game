package structures.basic;

import java.util.HashSet;
import java.util.Set;

import akka.actor.ActorRef;
import commands.BasicCommands;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

public class BetterUnit extends Unit {

    Set<String> keywords;


    public BetterUnit(ActorRef out) {
        //avatar object
        Unit avatar = BasicObjectBuilders.loadUnit(StaticConfFiles.humanAvatar, 0, Unit.class);
        setAvatar(out, avatar);
        setHealth(out, avatar);
        setAttack(out, avatar);
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

    public void setAvatar(ActorRef out, Unit unit) {
        // load tile for avatar placement
        Tile tile = BasicObjectBuilders.loadTile(1, 2);

        // Draw the avatar
        unit.setPositionByTile(tile);
        BasicCommands.drawUnit(out, unit, tile);

        //seems to not set health and attack without a sleep ( no idea why )
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void setHealth(ActorRef out, Unit unit) {
        BasicCommands.setUnitHealth(out, unit, 20);
    }

    public void setAttack(ActorRef out, Unit unit) {

        BasicCommands.setUnitAttack(out, unit, 2);
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
