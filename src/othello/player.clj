(ns othello.player
  [:use [othello board engine game]
        [clojure.data.finger-tree]])

(defn dumb-player
  "A simple player implementation for testing."
  [board color]
  (first (find-eligible-moves board color)))

(defn random-player
  "Chooses a move at random from the possible moves."
  [board color]
  (rand-nth (find-eligible-moves board color)))


(defn smart-player [depth score]
  (fn [board color]

    ))

(defn count-whites [board]
  (count (filter #(= :white %) (vals (:data board)))))

(defn min-max [board color score-function depth]
  (:move (min-max-value {:score 0 :board board} color score-function depth)))

(defn apply-score [original-board new-move color score-function]
  (let [new-board (execute-move original-board new-move color)]
    {:score (* (score-function new-board) (color {:white 1 :black -1}))
     :move new-move
     :board new-board}))

(defn min-max-value [{:keys [score move board] :as all} color score-function depth]
  (if (= 0 depth) all
      (let [apply-score (fn [original-board new-move]
                          (let [new-board (execute-move original-board new-move color)]
                              {:score (+ (* (score-function new-board) (color {:white 1 :black -1}))
                                         (:score (min-max-value {:score 0 :board new-board}
                                                                (color {:white :black :black :white})
                                                                score-function
                                                                (dec depth))))
                               :move new-move
                               :board new-board}))
            potential-moves (find-eligible-moves board color)
            scored-moves (map #(apply-score board %) potential-moves)
            comp (color {:white > :black <})]
        (first (sort-by :score comp scored-moves)))))
