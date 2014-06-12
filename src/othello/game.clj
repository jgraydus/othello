(ns othello.game
  [:use [othello board engine]])

(defn- tally-results
  "returns a map from color to number of board positions occupied by
   that color"
  [board]
  (let [occupied-positions (get-occupied-positions board)
        tally (group-by #(get-position board %) occupied-positions)]
    (apply merge (for [k (keys tally)] {k (count (k tally))}))))

(defn- play-game
  "main game loop.  players is a map where the keys :white and :black
   correspond to the functions that should be called to generate that
   player's next move. pass is a boolean that indicates whether or not
   the last turn was passed, i.e. the player had no move.  the game ends
   when neither player can move"
  [board players color pass]
  (let [possible-moves (find-eligible-moves board color)
        should-pass (empty? possible-moves)
        game-over (and pass should-pass)]
    (print-board board)
    (cond game-over (println "game over " (str (tally-results board)))
          should-pass (recur board players (opposite color) true)
          :else (let [move ((color players) board color)
                      updated-board (execute-move board move color)]
                  (recur updated-board players (opposite color) false)))))

(defn othello-game
  "Starts a new game.  white and black (the 'players') are functions that
   should accept a board and color and return a position which must be a
   valid move for that color.  the game ends when neither player can move"
  [white black]
  (play-game (new-board) {:white white :black black} :black false))
