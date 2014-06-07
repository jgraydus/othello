(ns othello.game
  [:use [othello board engine]])

(defn tally-results
  "returns a map of colors to number of positions occupied by the color"
  [board]
  (let [occupied-positions (get-occupied-positions board)
        tally (group-by #(get-position board %) occupied-positions)]
    (apply merge (for [k (keys tally)] {k (count (k tally))}))))

(defn play-game
  ""
  [board players color pass]
  (let [possible-moves (find-eligible-moves board color)
        should-pass (empty? possible-moves)
        game-over (and pass should-pass)]
    (comment (print-board board) (flush))
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
