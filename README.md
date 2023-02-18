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

