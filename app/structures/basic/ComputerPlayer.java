package structures.basic;

import java.util.List;

import com.fasterxml.jackson.databind.node.ObjectNode;

import actions.PerformAction;
import akka.actor.ActorRef;
import commands.BasicCommands;
import events.CardClicked;
import events.TileClicked;
import play.libs.Json;
import structures.GameState;
import utils.AppConstants;

/** creating a computerPlayer class so we can add extra functionalities down the line
 * 
 *
 */
public class ComputerPlayer extends Player{

	Tile currentTile; // To keep track of avatar's tile
	List <Card> cardInHand;//to keep track of card in the hand
	List <Tile> tileWithMyUnit;//to keep track of tiles occupied by AI units
	List <Tile> tileWithPlayerUnits;//to keep track of human player units
	List <Tile> possibleSummonList;
    /** constructor to create a player with set health and mana which calls 
     * setPlayer to place the data on the front end.
     * 
     * @param playerID
     * @param out
     * @param avatar
     * @param deck2Cards
     */
    public ComputerPlayer(int playerID, ActorRef out, BetterUnit avatar, String[] unitFiles) {
        super(playerID, out, avatar, unitFiles);
       
    }
    
    public void setCurrentTile(Tile currentTile) {
    	this.currentTile=currentTile;
    }
    
    public Tile getCurrentTile() {
    	return currentTile;
    }

	public void startAILogic(ActorRef out, GameState gameState) {

		// Logic to decide what action to perform can be written here
		
//		checkMovement(out,gameState); //to check movement possibilities
		
//		checkAttack(out,gameState); // To check attack possibilities
		checkHand();//checking the cards in the hand
		checkUnitTiles(out,gameState);
		Boolean movesLeft=true;
		if(movesLeft){
			movesLeft=listPossibleMove(out,gameState);
		}
		
		drawCard(out,gameState); // To check drawcard possibilities
		
		
	}

	private void drawCard(ActorRef out, GameState gameState) {


		// ------------ Logic to check and finalize draw card ----------
		
		AppConstants.printLog("<-------- AI :: drawCard():: hand size: "+hand.size());
		
		for(Card card:hand)
			AppConstants.printLog("<-------- AI :: drawCard():: hand card: "+card.getCardname());

		Tile tileToSummon=new Tile(); // The tile to summon 

		int handIdx=1;
		if(hand.size()<4)
		{
	        BasicCommands.addPlayer1Notification(out, "AI summoning a unit [7,1] test ", 2);

		// To test unit summon
			tileToSummon.tilex=7;
			tileToSummon.tiley=1;
		}else if(hand.size()==5)
		{
	        BasicCommands.addPlayer1Notification(out, "AI summoning a unit [6,3] test ", 2);

			handIdx=1;
			// To test unit summon
			tileToSummon.tilex=5;
			tileToSummon.tiley=3;
		}else {
	        BasicCommands.addPlayer1Notification(out, "AI summoning a unit [3,2] test ", 2);

			handIdx=3;
			// To test unit summon
			tileToSummon.tilex=6;
			tileToSummon.tiley=3;
		}
		
		
		drawCardAI(handIdx,out,gameState,currentTile,tileToSummon);
		
//		checkMovement(out,gameState);
		
		// checkAttack(out,gameState);
	}

	//method the check the cards in the hand
	//use map or dict to store these in order to utilize later when deciding which card to summon
	public void checkHand(){
		cardInHand=super.hand;
		for (Card card : cardInHand) {
			System.out.println("card in AI's hand: "+ card.getCardname()+ " with Mana cost: "+ card.getManacost());
		}
	}

	// method to get the tiles with the units on the board
	public void checkUnitTiles(ActorRef out,GameState gameState) {
		//AI unit's tile
		tileWithMyUnit=gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player2);
		for (Tile tile : tileWithMyUnit) {
			if(tile.getUnitFromTile().getId()==41) System.out.println("Tiles with AI units: "+tile.getTilex()+" "+ tile.getTiley()+ " with unit AI_Aviatar and id: " + tile.getUnitFromTile().getId());
			else System.out.println("Tiles with AI units: "+tile.getTilex()+" "+ tile.getTiley()+ " with unit: "+ tile.getUnitFromTile().getName()+ " and id: " + tile.getUnitFromTile().getId());
		}
		//player unit's tile
		tileWithPlayerUnits = gameState.board.getTilesWithUnits(out, gameState.board.getTiles(), gameState.player1);
		for (Tile tile : tileWithPlayerUnits) {
			if(tile.getUnitFromTile().getId()==40) System.out.println("Tiles with Player units: "+tile.getTilex()+" "+ tile.getTiley()+ " with unit Human_Avatar and id: " + tile.getUnitFromTile().getId());
			else System.out.println("Tiles with Player units: "+tile.getTilex()+" "+ tile.getTiley()+ " with unit: "+ tile.getUnitFromTile().getName()+ " and id: " + tile.getUnitFromTile().getId());
		}
	}

	

	private void checkAttack(ActorRef out, GameState gameState) {
		
        BasicCommands.addPlayer1Notification(out, "AI attack", 2);

		Tile tileToAttack=new Tile(); // The tile to move 
		
		//-------------- Logic to identify a tile to move---------
		
		
		// To test direct attack
//		tileToAttack.tilex=7;
//		tileToAttack.tiley=1;
		
		//To test move and attack
		tileToAttack.tilex=5;
		tileToAttack.tiley=4;
				
		// If attack is finalized, and move and atack or direct attacks
		attackAIUnit(out, gameState, currentTile, tileToAttack);
		
		
	}

	/** This method will check for an optimal movement to perform and updates front end
	 * 
	 * @param out
	 * @param gameState
	 */
	private void checkMovement(ActorRef out, GameState gameState) {

        BasicCommands.addPlayer1Notification(out, "AI movement", 2);

		Tile tileToMove=new Tile(); // The tile to move 
		
		//-------------- Logic to identify a tile to move---------
		
		
		
		// To test
		tileToMove.tilex=5;
		tileToMove.tiley=2;
		
		// If movement is finalized, and move 
		moveAIUnit(out, gameState, currentTile, tileToMove);
	
	}

	/**
	 * This method will programmatically perform move for a player 2's unit 
	 * @param out
	 * @param gameState
	 */
	private void moveAIUnit(ActorRef out, GameState gameState,Tile startTile,Tile tileToMove) {
		ObjectNode eventMessage = Json.newObject();
		eventMessage.put("messagetype", "tileclicked");
		eventMessage.put("start_tilex",""+startTile.getTilex());
		eventMessage.put("start_tiley", ""+startTile.getTiley());
		eventMessage.put("tilex",""+tileToMove.getTilex());
		eventMessage.put("tiley", ""+tileToMove.getTiley());
		eventMessage.put("action", AppConstants.move);

		TileClicked tc=new TileClicked();
		tc.processEvent(out, gameState, eventMessage); // send it to the Tileclicked event processor

	}

	/**
	 * This method will programmatically perform move for a player 2's unit 
	 * @param out
	 * @param gameState
	 */
	private void attackAIUnit(ActorRef out, GameState gameState,Tile startTile,Tile tileToAttack) {
		ObjectNode eventMessage = Json.newObject();
		eventMessage.put("messagetype", "tileclicked");
		eventMessage.put("start_tilex",""+startTile.getTilex());
		eventMessage.put("start_tiley", ""+startTile.getTiley());
		eventMessage.put("tilex",""+tileToAttack.getTilex());
		eventMessage.put("tiley", ""+tileToAttack.getTiley());
		eventMessage.put("action", AppConstants.attack);

		TileClicked tc=new TileClicked();
		tc.processEvent(out, gameState, eventMessage); // send it to the Tileclicked event processor

	}

	
	private void drawCardAI(int handIdx, ActorRef out, GameState gameState,Tile startTile,Tile tileToSummon) {
		ObjectNode eventMessage = Json.newObject();
		eventMessage.put("messagetype", "cardclicked");
		eventMessage.put("position",""+handIdx);
		eventMessage.put("start_tilex",""+startTile.getTilex());
		eventMessage.put("start_tiley", ""+startTile.getTiley());
		eventMessage.put("tilex",""+tileToSummon.getTilex());
		eventMessage.put("tiley", ""+tileToSummon.getTiley());
		eventMessage.put("action", AppConstants.drawCardSummon);


		TileClicked tc=new TileClicked();
		tc.processEvent(out, gameState, eventMessage); // send it to the Tileclicked event processor
	}
	
	public Boolean listPossibleMove(ActorRef out, GameState gameState) {
		//need to list all possible moves for the AI player
		Boolean done =false;//boolean to send back
		int handindex=-1;
		//first check which cards can be played
		for (Card card : cardInHand) {
			handindex =cardInHand.indexOf(card)+1;
			//check if mana cost of card is more than the mana of the AI
			if(super.getMana()>=card.getManacost()){
				System.out.println("card with hand position: "+ handindex+" name: "+ card.getCardname()+" can be played");
				//get summonable tiles
				possibleSummonList= PerformAction.getSummonableTiles(out, gameState, gameState.player2);
				Tile mostForwardTile= new Tile();
				for (Tile tile : possibleSummonList) {
					// System.out.println("Possible summon tiles: ["+ tile.getTilex()+","+tile.getTiley()+"]");
					int maxTileX=9;
					int maxTileY=9;
					if(tile.getTilex()<=maxTileX){
						if(tile.getTiley()<=maxTileY) mostForwardTile=tile;
					}
				}
				System.out.println("Most forward Tile: ["+ mostForwardTile.getTilex()+","+mostForwardTile.getTiley()+"]");
			}
		}
		//possible moves if the unit has not moved or attacked
		for (Unit u : gameState.summonedUnits) {
			if(u.getId()>19 && u.getId()!=40){//checking if they are AI units or not
				if(u.getMoved()==false || u.getAttacked()==false){//checking if the unit has not moved or attacked
					System.out.println("unit: "+u.getName()+" with id: "+u.getId()+" has not attacked or moved");
				}
			}
		}
		return done;
	}
	
	
}
