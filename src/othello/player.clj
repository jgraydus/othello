(ns othello.player
  [:use [othello board engine game]])

(defn dumb-player
  "A simple player implementation for testing."
  [board color]
  (first (find-eligible-moves board color)))

(defn random-player
  "Chooses a move at random from the possible moves."
  [board color]
  (let [moves (find-eligible-moves board color)
        index (rand-int (count moves))
        choice (first (drop index moves))]
    choice))
