# MSCTeamProject Test ReadMe

This file is to store data about the unit tests.

# AIAvatarNotNullTest

Tests that `aiAvatar` is not null when the game is initialized 

# PlayerAvatarNotNullTest

Tests that `avatar` is not null when the game is initialized 

# AIInitialPositionTest

Tests that the AI's (`player2`) initial x position (`gameState.player2.getCurrentXpos()`) is 7 and its y position (`gameState.player2.getCurrentXpos()`) is 2 once the game is initialized

# PlayerInitialPositionTest

Tests that the human player's (`player1`) initial x position (`gameState.player1.getCurrentXpos()`) is 1 and its y position (`gameState.player1.getCurrentXpos()`) is 2 once the game is initialized

# InitializedAIHealthTest

This test is to confirm that the AI's initial health (`gameState.player2.getHealth()`)equals 20, the maximum health constant defined in AppConstants(`AppConstants.playerMaxHealth`)

# InitializedPlayerHealthTest

This test is to confirm that the human player's initial health (`gameState.player1.getHealth()`)equals 20, the maximum health constant defined in AppConstants(`AppConstants.playerMaxHealth`)

# InitalizationTest

This test was written by the Professor and was included in our source code. It creates a new `GameState` and a new `Initialize` event processor. Then it verifies that `gameState.gameInitalised` returns true (the game state is updated) once an initialize message is received. 

#HandAndDeckConstantsTests:

Contains 2 Tests -> maxHandSize, maxDeckSize

# maxHandSize:

This test confirms that the max hand size for both players (`AppConstants.maxCardsInHand`) is 6.

# maxDeckSize: 

This test confirms that the max hand size for both players (`AppConstants.maxCardsInDeck`) is 20.

# InitialHandSizes:

Contains 2 Tests -> PlayerInitialHandSize and AIInitialHandSize 

# PlayerInitialHandSize

This test confirms that the initial number of cards in the human player's hand (`gameState.player1.hand.size()`) is 3 (`AppConstants.minCardsInHand`). 

# AIInitialHandSize

This test confirms that the initial number of cards in the AI player's hand (`gameState.player2.hand.size()`) is 3 (`AppConstants.minCardsInHand`).

# Player1TurnTest

This test confirms that it is player1's turn when (`gameState.player1Turn`) is set to true and that it is the AI's turn when (`gameState.player1Turn`) is set to false.

# IsGameOverTest

This test confirms that the game ends when the human or computer player's health reaches 0, or either player runs out of cards. 

# IncrementManaTest

This test confirms that the mana for both players is equal to turn + 1 with each new turn.  

# PlayerInitialIAttackValue

This test confirms that the initial attack for the human avatar is set to 2

# AIInitialIAttackValue

This test confirms that the initial attack for the computer avatar is set to 2