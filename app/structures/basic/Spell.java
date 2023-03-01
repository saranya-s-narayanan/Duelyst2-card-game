package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

public class Spell {

    int manaCost;
    int id;

    public Spell(ActorRef out, int manaCost, int id) {
        this.manaCost = manaCost;
        this.id = id;
    }

    public int getManaCost() {
        return manaCost;
    }

    public void setManaCost(int manaCost) {
        this.manaCost = manaCost;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // methods forming a basic representation of the spells and unfinished logic as to how they will effect the game
    public void truestike(ActorRef out, Card card, Tile tile) { // if truestrike is played, deal 2 damage to the unit

        Spell truestrike = new Spell(out, 1, 4);

        if (card.getId() == truestrike.getId() || card.getId() + 10 == truestrike.getId()) {

            Unit unitToAttack = tile.getUnitFromTile();

            if (unitToAttack != null) {

                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), tile);
                unitToAttack.setHealth(unitToAttack.getHealth() - 2);
                AppConstants.callSleep(100);
                BasicCommands.setUnitHealth(out, unitToAttack, unitToAttack.getHealth());
            }
        }
    }

    public void sundropElixir(ActorRef out, Card card, Tile tile) {  // if sundropElixir is played, heal 5 to a unit (this must not take the unit over its starting health value)

        Spell sundropElixir = new Spell(out, 1, 8);

        if (card.getId() == sundropElixir.getId() || card.getId() + 10 == sundropElixir.getId()) {

            Unit unitToHeal = tile.getUnitFromTile();

            if (unitToHeal != null) {
                if (unitToHeal.getHealth() + 5 > card.getBigCard().getHealth()) { // need to test that big card contains the starting values for the health, otherwise I can create a
                    // variable to store the max (initial) health of a unit.

                    BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                    unitToHeal.setHealth(card.getBigCard().getHealth());
                    AppConstants.callSleep(100);
                    BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getHealth());
                } else {

                    BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                    unitToHeal.setHealth(unitToHeal.getHealth() + 5);
                    AppConstants.callSleep(100);
                    BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getHealth());
                }

            }
        }
    }

    public void staffOfYKir(ActorRef out, Card card, Tile tile, BetterUnit avatar) { // if staffOfYKir is played, add 2 attack to your avatar

        Spell staffOfYKir = new Spell(out, 2, 22);

        if (card.getId() == staffOfYKir.getId() || card.getId() + 10 == staffOfYKir.getId()) {

            BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
            avatar.setAttack(avatar.getAttack() + 2);
            AppConstants.callSleep(100);
            BasicCommands.setUnitAttack(out, avatar, avatar.getAttack());
        }
    }

    public void entropicDecay(ActorRef out, Card card, Tile tile, BetterUnit avatar) { // if entropicDecay is played, reduce a non-avatar unit to 0 health (KILL THEM)

        Spell entropicDecay = new Spell(out, 5, 27);

        if (card.getId() == entropicDecay.getId() || card.getId() + 10 == entropicDecay.getId()) {

            Unit unitToHeal = tile.getUnitFromTile();

            if (unitToHeal != null || !unitToHeal.equals(avatar)) {

                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom), tile);
                unitToHeal.setHealth(0);
                AppConstants.callSleep(100);
                BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getHealth());

            }
        }
    }

    // this method will highlight the enemy units on the board. the player parameter must take in the enemy player (the player whose turn it is NOT)
    public void highlightEnemyUnits(ActorRef out, GameState gameSate, Player player) {

        ArrayList<Tile> tileList = gameSate.board.getTilesWithUnits(out, gameSate.board.getTiles(), player);

        gameSate.board.highlightTilesRed(out, tileList);

    }

    //this method will highlight all friendly units white. player paramet must take in the current player (the player whose turn it IS)
    public void highlighFriendlyUnits(ActorRef out, GameState gameSate, Player player) {

        ArrayList<Tile> tileList = gameSate.board.getTilesWithUnits(out, gameSate.board.getTiles(), player);

        gameSate.board.highlightTilesWhite(out, tileList);
    }
}


