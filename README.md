# othello

The game Othello.  Completely functional implementation in Clojure.

This isn't a typical game of Othello.  In order to play, you must program a player: a function that takes as arguments the board representation and the current player's color, and returns a move.

See the implementation of dumb-player and random-player for simple examples.

## Usage

othello.player> (othello-game dumb-player random-player)

