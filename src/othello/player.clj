(ns othello.player
  [:use [othello board engine game]
        [clojure.data.finger-tree]])

(declare min-max-value)
(declare min-max)

(defn dumb-player
  "A simple player implementation for testing."
  [board color]
  (first (find-eligible-moves board color)))

(defn random-player
  "Chooses a move at random from the possible moves."
  [board color]
  (rand-nth (find-eligible-moves board color)))

(defn count-whites [board]
  (count (filter #(= :white %) (vals (:data board)))))

(defn score-1 [board]
  (let [black-positions (filter #(= :black %) (:data board))
        white-positions (filter #(= :white %) (:data board))
        blacks (count black-positions)
        whites (count white-positions)
        ratio-score (int (* 100 (/  (Math/abs (- blacks whites)) (+ blacks whites 1))))
        corners (filter #{[0 0] [0 7] [7 0] [7 7]} white-positions)
        worst-positions (filter #{[1 0] [0 1] [1 1]
                                  [6 0] [6 1] [7 1]
                                  [0 6] [1 6] [1 7]
                                  [6 6] [7 6] [6 7]} white-positions)]
    (+ ratio-score (* 200 (count corners)) (* -200 (count worst-positions)))))

(defn min-max [board color score-function depth]
  (:move (min-max-value {:score 0 :board board} color score-function depth)))


(defn smart-player [board color]
  (min-max board color score-1 3))


(defn apply-score [original-board new-move color score-function]
  (let [new-board (execute-move original-board new-move color)]
    {:score (* (score-function new-board) (color {:white 1 :black -1}))
     :move new-move
     :board new-board}))

(defn min-max-value [{:keys [score move board] :as all} color score-function depth]
  (if (= 0 depth) all
      (let [apply-score (fn [original-board new-move]
                          (let [new-board (execute-move original-board new-move color)]
                            {:score ((fnil + 0 0)
                                     (* (score-function new-board) (color {:white 1 :black -1}))
                                     (:score (min-max-value {:score 0 :board new-board}
                                                            (color {:white :black :black :white})
                                                            score-function
                                                            (dec depth)))
                                       score 0)
                               :move new-move
                               :board new-board}))
            potential-moves (find-eligible-moves board color)
            scored-moves (pmap #(apply-score board %) potential-moves)
            comp (color {:white > :black <})]
        (first (sort-by :score comp scored-moves)))))
