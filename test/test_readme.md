# MSCTeamProject Test ReadMe

This file is to store data about the unit tests.

# AIAvatarNotNullTest

Tests that `aiAvatar` is not null when the game is initialized 

# PlayerAvatarNotNullTest

Tests that `avatar` is not null when the game is initialized 

# AIInitialPositionTest

Tests that the AI's (`player2`) initial x position (`gameState.player2.getCurrentXpos()`) is 7 and its y position (`gameState.player2.getCurrentXpos()`) is 2 once the game is initialized

**Test no longer works as of 2/23 since tile position is now passed into constructor - need to come up with another solution**

# PlayerInitialPositionTest

Tests that the human player's (`player1`) initial x position (`gameState.player1.getCurrentXpos()`) is 1 and its y position (`gameState.player1.getCurrentXpos()`) is 2 once the game is initialized

**Test no longer works as of 2/23 since tile position is now passed into constructor - need to come up with another solution**

# InitializedAIHealthTest

This test is to confirm that the AI's initial health (`gameState.player2.getHealth()`)equals 20, the maximum health constant defined in AppConstants(`AppConstants.playerMaxHealth`)

# InitializedPlayerHealthTest

This test is to confirm that the human player's initial health (`gameState.player1.getHealth()`)equals 20, the maximum health constant defined in AppConstants(`AppConstants.playerMaxHealth`)

# InitalizationTest

This test was written by the Professor and was included in our source code. It creates a new `GameState` and a new `Initialize` event processor. Then it verifies that `gameState.gameInitalised` returns true (the game state is updated) once an initialize message is received. 