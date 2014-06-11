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

(defn game-stream
  "generates a lazy sequence of boards representing the state of the game.
   if a player cannot move, the corresponding list item is :pass"
  [board players color]
  (when-let [board board]
    (cons board
          (lazy-seq
           (game-stream
            (let [eligible-moves (find-eligible-moves board color)]
              (if (empty? eligible-moves) (if (= board :pass) nil :pass)
                  (let [player (color players)
                        move (player board color)]
                    (execute-move board move color))))
            players
            (opposite color))))))


(defn othello-game
  "Starts a new game.  white and black (the 'players') are functions that
   should accept a board and color and return a position which must be a
   valid move for that color.  the game ends when neither player can move"
  [white black]
  (play-game (new-board) {:white white :black black} :black false))
