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
    public static void truestike(ActorRef out, Card card, Tile tile, GameState gameState) { // if truestrike is played, deal 2 damage to the unit

        Spell truestrike = new Spell(out, 1, 4);
        int hp;

        if (card.getCardname().equals("Truestrike")) {

            Unit unitToAttack = tile.getUnitFromTile();

            if (unitToAttack.getIsPlayer()==2 ) {
                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), tile);
                if (unitToAttack.getSummonedID()==2) {

                    hp = gameState.player2.getAvatar().getHealth()-2;
                    gameState.player2.getAvatar().setHealth(hp);
                    unitToAttack.setHealth(hp);
                    BasicCommands.setUnitHealth(out, unitToAttack, hp);
                    AppConstants.callSleep(100);

                    if(gameState.player2.getAvatar().getHealth()<0)
                        gameState.player2.getAvatar().setHealth(0);

                }
                else  {
                    hp = unitToAttack.getHealth()-2;
                    unitToAttack.setHealth(hp);
                    BasicCommands.setUnitHealth(out, unitToAttack, hp);

                    if(unitToAttack.getHealth()<0)
                        unitToAttack.setHealth(0);
                }
                if(hp<=0) // enemy unit dead, clear tile and update front end
                {
                    gameState.summonedUnits.remove(unitToAttack);

                    BasicCommands.playUnitAnimation(out, unitToAttack, UnitAnimationType.death);
                    AppConstants.callSleep(AppConstants.deathSleepTime);
                    EffectAnimation ef = BasicObjectBuilders.loadEffect(AppConstants.effects[2]);
                    BasicCommands.playEffectAnimation(out, ef, tile);
                    AppConstants.callSleep(AppConstants.effectSleepTime);
                    tile.setUnitToTile(null);
                    BasicCommands.deleteUnit(out, unitToAttack);
                    AppConstants.callSleep(3000);

                }
            }
            gameState.player1.setMana(gameState.player1.getMana()-card.getManacost());//decrease the mana
            gameState.player1.setPlayer(out);//reflecting the mana on board

            gameState.player1.deleteCardInHand(out, gameState.player1.getID(), gameState);//delete the card in hand
            AppConstants.callSleep(200);
            gameState.board.clearTileHighlighting(out, gameState.board.allTiles());

        }
    }

    public static void sundropElixir(ActorRef out, Card card, Tile tile, GameState gameState) {  // if sundropElixir is played, heal 5 to a unit (this must not take the unit over its starting health value)

        Spell sundropElixir = new Spell(out, 1, 8);

        if (card.getCardname().equals("Sundrop Elixir")) {

            Unit unitToHeal = tile.getUnitFromTile();

            if (unitToHeal != null && unitToHeal.getIsPlayer()==1) {

                if (unitToHeal.getHealth() + 5 > unitToHeal.getMaxHealth()){

                    BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                    unitToHeal.setHealth(unitToHeal.getMaxHealth());
                    AppConstants.callSleep(100);
                    BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getMaxHealth());
                }

                else {
                    BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                    unitToHeal.setHealth(unitToHeal.getHealth() + 5);
                    AppConstants.callSleep(100);
                    BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getHealth());
                }

                gameState.player1.setMana(gameState.player1.getMana()-card.getManacost());//decrease the mana
                gameState.player1.setPlayer(out);//reflecting the mana on board

                gameState.player1.deleteCardInHand(out, gameState.player1.getID(), gameState);//delete the card in hand
                AppConstants.callSleep(200);
                gameState.board.clearTileHighlighting(out, gameState.board.allTiles());
            }
        }
    }

    public static void staffOfYKir(ActorRef out, Card card, Tile tile, BetterUnit avatar) { // if staffOfYKir is played, add 2 attack to your avatar

        Spell staffOfYKir = new Spell(out, 2, 22);

        if (card.getCardname().equals("Staff of Y'Kir'")) {

            BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
            avatar.setAttack(avatar.getAttack() + 2);
            AppConstants.callSleep(100);
            BasicCommands.setUnitAttack(out, avatar, avatar.getAttack());
        }
    }

    public static void entropicDecay(ActorRef out, Card card, Tile tile, BetterUnit avatar) { // if entropicDecay is played, reduce a non-avatar unit to 0 health (KILL THEM)

        Spell entropicDecay = new Spell(out, 5, 27);

        if (card.getCardname().equals("Entropic Decay")) {

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


