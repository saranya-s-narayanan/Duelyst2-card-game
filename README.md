# MSCTeamProject

This is a backend developed for a tactical card game application in Java 

# Board creation

A Board class has been created to accomodate different tile objects and its methods ,which constitutes the board

# Player creation

The constructor method has been modified to differentiate both players' attributes and methods


# Draw Card

This was changed to ArrayList `Deck` and `Hand` in the player class for better handling of deck and hand.
the `Deck` has 2 sets of card for the players and is created through the `createDeck()` method in player class, `Hand` gets 3 cards from `Deck` at initialization using the `setHand(ActorRef out, int playerID)` method. the cards are removed from the deck whenever a card is drawn using the `drawAnotherCard(ActorRef out, int playerID)` method.

The end turn class was modified to include `drawAnotherCard(out, playerID)` to facilitate the drawing of card after endTurn is clicked.

# OverDraw

if the hand is full of cards, i.e from position 1-6 then the 7th position will not be drawn on the board, the 
corresponding card will be deleted from the deck. The method is part of the `drawAnotherCard(out, playerID)` in the player class.


# Player attack ability

An arraylist 'summonedUnits' will be used to keep track of movements and attacks of all the units on board in a turn

A 'PerformAction' class has been introduced to include the methods for the implementation of available
actions (highlight/move/attack) and perfrom appropriate tasks in order
	
	- Added highlightTiles() to highlight or unhighlight the available tiles to move or attack upon clicking
	on avatar.
	
	- Added attackUnit() to implement the avatar attack logic on an enemy unit and handling counter attack



# Get tiles containing units

`getTilesWithUnits(ActorRef,Tile[][], Player)` is a method in the board class that takes in the 2d array of 
tiles(board) and player and returns an ArrayList of tiles that contain a unit belonging to the input player.

# Card Clicked Highlighting

I have added a method called `highlightSummonableTiles(ActorRef, GameState)` in the `CardClicked` class. When a card
is clicked in the front end this method is called and it will call `getTilesWithUnits()`, which will be fed into the method
`getTilesToAttack()` method to return a list of all adjacent tiles which will then be passed though the `highlightTilesWhite()`
method to highlight all the available summoning tiles for the given player.

# BetterUnit creation

I have modified the constructor so the board is passed into it. This is so the `addUnitToBoard(tilex,tiley)` can be used in the
creation of the avatar. The avatar needs to be treated as any other unit for the most part so needs to be accessed 
through the tiles in the same way.

# Move units

The `highlightAndMove(ActorRef, GameState, Tile)` method is called in the TileClicked class and is used to first highlight the
available units moves and then on the second click (if a friendly unit), will execute the movement. If it is an enemy unit, 
it will just clear the highlighting and the player will be able to click another unit again.

# Linking players to units

A variable `int isPlayer` is created in the Unit class to store either the value 1 or 2 which will the used to check that the 
isPlayer is the same as the current playerID. There may be a better way to do this but for now it gives us all the functionality
we need to execute core game logic.

# Clearing highlighting with otherClicked

I have created a method `clearHighlighting(ActorRef, Board)` which is called in the otherClicked class as well as in the tileClicked class
int places which simply reloads the tiles with the mode '0'. 

# GameEnd

A method to check if the game has ended, when the players reach 0 health or have no cards left to play, was 
created in the PerformAction class `gameEnd(ActorRef out, GameState gameState)`. the method returns a boolean
and also sets `gameState.isGameOver`, it is being called in the Heartbeat.java to periodically check the state.

