# othello

The game Othello.  Completely functional implementation in Clojure.

This isn't a typical game of Othello.  In order to play, you must program a player: a function that takes as arguments the board representation and the current player's color, and returns a move.

See the implementation of dumb-player and random-player for simple examples.

## Usage
 
user> (use '[othello board engine game player])
nil
user> (othello-game random-player dumb-player)
 01234567
0        
1        
2        
3   OX   
4   XO   
5        
6        
7        

 01234567
0        
1        
2   X    
3   XX   
4   XO   
5        
6        
7        

 01234567
0        
1        
2  OX    
3   OX   
4   XO   
5        
6        
7        

.
.
.


 01234567
0XXXXXXXO
1XXXXXXXO
2XXXXXOXO
3XXXOOOOO
4XXOOOXOO
5XXOOXOOO
6XOOXOXOO
7OOOOOOOO

game over  {:white 32, :black 32}
nil
user> 