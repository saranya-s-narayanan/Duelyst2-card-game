package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import structures.GameState;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

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

    // basic representation of the spells and unfinished logic as to how they will effect the game
    public void playSpell(ActorRef out, Card card, GameState gameState, Tile tile, BetterUnit avatar) {

        Spell truestrike = new Spell(out, 1, 4);

        Spell sundropElixir = new Spell(out, 1, 8);

        Spell staffOfYKir = new Spell(out, 2, 22);

        Spell entropicDecay = new Spell(out, 5, 27);

        // if truestrike is played, deal 2 damage to the unit
        if (card.getId() == truestrike.getId() || card.getId() + 10 == truestrike.getId()) {

            Unit unitToAttack = tile.getUnitFromTile();

            if (unitToAttack != null) {

                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_inmolation), tile);
                unitToAttack.setHealth(unitToAttack.getHealth() - 2);
                BasicCommands.setUnitHealth(out, unitToAttack, unitToAttack.getHealth());
            }
        }

        // if sundropElixir is played, heal 5 to a unit (this must not take the unit over its starting health value(not implemented yet))
        if (card.getId() == sundropElixir.getId() || card.getId() + 10 == sundropElixir.getId()) {

            Unit unitToHeal = tile.getUnitFromTile();

            if (unitToHeal!= null) {

                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
                unitToHeal.setHealth(unitToHeal.getHealth() + 5);
                BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getHealth());
            }
        }

        // if staffOfYKir is played, add 2 attack to your avatar
        if (card.getId() == staffOfYKir.getId() || card.getId() + 10 == staffOfYKir.getId()) {

            BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_buff), tile);
            avatar.setAttack(avatar.getAttack() + 2);
            BasicCommands.setUnitAttack(out, avatar, avatar.getAttack());
        }

        // if entropicDecay is played, reduce a non-avatar unit to 0 health (KILL THEM)
        if (card.getId() == entropicDecay.getId() || card.getId() + 10 == entropicDecay.getId()) {

            Unit unitToHeal = tile.getUnitFromTile();

            if (unitToHeal!= null || !unitToHeal.equals(avatar)) {

                BasicCommands.playEffectAnimation(out, BasicObjectBuilders.loadEffect(StaticConfFiles.f1_martyrdom), tile);
                BasicCommands.setUnitHealth(out, unitToHeal, unitToHeal.getHealth());
                unitToHeal.setHealth(0);

            }
        }


    }
}
