# MSCTeamProject

This is a backend developed for a tactical card game application in Java 

# Board creation

A Board class has been created to accomodate different tile objects and its methods ,which constitutes the board

# Player creation

The constructor method has been modified to differentiate both players' attributes and methods


# Draw Card

String array of card created for deck in player class corresponding to the player cards, `public void setHand
(ActorRef out)` was created which draws 3 cards from the deck in the player hand.

The end turn class was modified to include `drawAnotherCard(out)` which is defined in the player class.


# Player attack ability


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