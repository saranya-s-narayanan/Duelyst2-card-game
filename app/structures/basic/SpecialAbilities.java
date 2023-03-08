package structures.basic;

import akka.actor.ActorRef;
import commands.BasicCommands;
import events.TileClicked;
import structures.GameState;
import utils.AppConstants;
import utils.BasicObjectBuilders;
import utils.StaticConfFiles;

import java.util.ArrayList;

public class SpecialAbilities {


    // changed attackDirectly method to accommodate ranged attack
    public static boolean rangedAttack(ActorRef out, GameState gameState, Unit unit, Unit enemyUnit, Tile enemyTile, Tile startTile) {

        // Not a friendly unit --> attack
        EffectAnimation projectile = BasicObjectBuilders.loadEffect(StaticConfFiles.f1_projectiles);
        BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.attack);
        try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
        BasicCommands.playProjectileAnimation(out, projectile, 0, startTile, enemyTile);
        AppConstants.callSleep(AppConstants.attackSleepTime);

        int attackVal;

        if (enemyUnit.getSummonedID() == 1) // Should update player 1 avatar health
        {
            attackVal = gameState.player1.getAvatar().getHealth() - unit.getAttack();

            gameState.player1.getAvatar().setHealth(attackVal);  // update enemy's health
            enemyUnit.setHealth(attackVal); // update enemy's health front end

            // To avoid negative values as health
            if (gameState.player1.getAvatar().getHealth() < 0)
                gameState.player1.getAvatar().setHealth(0);

        } else if (enemyUnit.getSummonedID() == 2) // Should update player2 avatar health
        {

            attackVal = gameState.player2.getAvatar().getHealth() - unit.getAttack();
            gameState.player2.getAvatar().setHealth(attackVal);  // update enemy's health
            enemyUnit.setHealth(attackVal); // update enemy's health front end

            // To avoid negative values as health
            if (gameState.player2.getAvatar().getHealth() < 0)
                gameState.player2.getAvatar().setHealth(0);

        } else {
            attackVal = enemyUnit.getHealth() - unit.getAttack();
            enemyUnit.setHealth(attackVal);  // update enemy's health

            // To avoid negative values as health
            if (enemyUnit.getHealth() < 0)
                enemyUnit.setHealth(0);
        }

        // update front end
        BasicCommands.setUnitHealth(out, enemyUnit, enemyUnit.getHealth());
        AppConstants.callSleep(100);

        BasicCommands.setUnitAttack(out, enemyUnit, enemyUnit.getAttack());
        AppConstants.callSleep(100);

        EffectAnimation ef = BasicObjectBuilders.loadEffect(AppConstants.effects[2]);
        BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.idle); // resets to idle after attack

        if (attackVal <= 0) // enemy unit dead, clear tile and update front end
        {
            gameState.summonedUnits.remove(enemyUnit);

            BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.death);
            AppConstants.callSleep(AppConstants.deathSleepTime);

            BasicCommands.playEffectAnimation(out, ef, enemyTile);
            AppConstants.callSleep(AppConstants.effectSleepTime);
            enemyTile.setUnitToTile(null);
            BasicCommands.deleteUnit(out, enemyUnit);
            AppConstants.callSleep(3000);

        } else if (gameState.board.summonableTiles(out,startTile).contains(enemyTile)){ //enemy survived and is in range, counter attack

            attackVal = -1;

            BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.attack); // enemy attacks avatar
            AppConstants.callSleep(AppConstants.attackSleepTime);

            if (unit.getSummonedID() == 1) // Should update avatar health of player1
            {

                attackVal = gameState.player1.getAvatar().getHealth() - enemyUnit.getAttack();
                gameState.player1.getAvatar().setHealth(attackVal);
//		    	gameState.player1.setHealth(attackVal);
                unit.setHealth(attackVal); // update enemy's health front end


                // To avoid negative values as health
                if (gameState.player1.getAvatar().getHealth() < 0) {
                    gameState.player1.getAvatar().setHealth(0);
//			    	gameState.player1.setHealth(0);

                }

            } else if (unit.getSummonedID() == 2) // Should update avatar health of player2
            {

                attackVal = gameState.player2.getAvatar().getHealth() - enemyUnit.getAttack();
                gameState.player2.getAvatar().setHealth(attackVal);
//		    	gameState.player2.setHealth(attackVal);
                unit.setHealth(attackVal); // update enemy's health front end


                // To avoid negative values as health
                if (gameState.player2.getAvatar().getHealth() < 0) {
                    gameState.player2.getAvatar().setHealth(0);
//			    	gameState.player2.setHealth(0);

                }

            } else {
                attackVal = unit.getHealth() - enemyUnit.getAttack();
                unit.setHealth(attackVal); // update unit health

                // To avoid negative values as health
                if (unit.getHealth() < 0)
                    unit.setHealth(0);
            }

            // update front end
            BasicCommands.setUnitHealth(out, unit, unit.getHealth());
            AppConstants.callSleep(100);

            BasicCommands.setUnitAttack(out, unit, unit.getAttack());
            AppConstants.callSleep(100);

            BasicCommands.playUnitAnimation(out, enemyUnit, UnitAnimationType.idle); // resets to idle after attack


            if (attackVal <= 0) //unit dead
            {
                gameState.summonedUnits.remove(unit);

                BasicCommands.playUnitAnimation(out, unit, UnitAnimationType.death);
                AppConstants.callSleep(AppConstants.deathSleepTime);

                BasicCommands.playEffectAnimation(out, ef, startTile);
                AppConstants.callSleep(AppConstants.effectSleepTime);
                startTile.setUnitToTile(null);
                BasicCommands.deleteUnit(out, unit);
                AppConstants.callSleep(100);

            }

        }

        return true;
    }

    // checks the board for any enemy units to attack
    public static boolean attackUnitRanged(int mode, Player player, ActorRef out, Unit unit, Tile startTile, Tile enemyTile , GameState gameState) {
        // TODO Auto-generated method stub

        // Retrieve the unit from the corresponding tile position
        Unit enemyUnit=enemyTile.getUnitFromTile();
        gameState.startTile=startTile;

        if(enemyUnit!=null)
        {
            // Check whether the unit is a friendly unit or not
            if(enemyUnit.getIsPlayer()!=player.getID())
            {

                ArrayList<Tile> tilesList=gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), TileClicked.opposingPlayer(gameState,player));

                // If the enemyTile is in range of the startTile, attack
                if(tilesList.contains(enemyTile))
                {
                    return rangedAttack(out, gameState,unit,enemyUnit,enemyTile,startTile);


                }

            }else {

                if(mode==1)
                {
                    BasicCommands.addPlayer1Notification(out, "Please select an enemy unit to attack! ", 2);
                    AppConstants.callSleep(100);
                }

            }

        }
        return false;

    }

    // this method checks if one of two instances of the silverguard knight is on the board, and if so increase attack by 2. this will be called after the avatar takes damage
    public static void SilverguardKnightAbility(ActorRef out, GameState gameState){

        for (Unit unit: gameState.summonedUnits) {

            if (unit.getId()==3){
                BasicCommands.addPlayer1Notification(out, "Silverguard Knight's attack +2", 2);
                unit.setAttack(unit.getAttack() + 2);
                AppConstants.callSleep(50);
                BasicCommands.setUnitAttack(out, unit, unit.getAttack());
            }
            if (unit.getId()==10){
                BasicCommands.addPlayer1Notification(out, "Silverguard Knight's attack +2", 2);
                unit.setAttack(unit.getAttack() + 2);
                AppConstants.callSleep(50);
                BasicCommands.setUnitAttack(out, unit, unit.getAttack());
            }

        }
    }


}


