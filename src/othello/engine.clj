(ns othello.engine
  [:use [othello board]])

(def deltas [[-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1]])

(defn opposite
  "returns :black if given :white and vice versa"
  [color]
  (cond (= :white color) :black
        (= :black color) :white
        :else (throw (IllegalArgumentException.
                      (str (if nil? "nil" color) " is not a valid color value")))))

(defn- shift
  "adds delta to the position"
  [position [dx dy]]
  (make-position (+ (get-x position) dx)
                 (+ (get-y position) dy)))

(defn- shifts
  "returns a lazy-seq of the result of shifting position by delta repeatedly"
  [position delta]
  (let [shifted-position (shift position delta)]
    (cons shifted-position (lazy-seq (shifts shifted-position delta)))))

(defn- get-empty-neighbors
  "returns a sequence of positions adjacent to the given position which
   are not occupied by a game piece"
  [board position]
  (let [raw-positions (map #(shift position %) deltas)
        valid-positions (filter valid-position? raw-positions)]
    (filter #(nil? (get-position board %)) valid-positions)))

(defn- find-potential-move-positions
  "returns a sequence of positions which correspond to empty board positions
   adjacent to a piece opposite of the given color"
  [board color]
  (let [opposite-color (opposite color)
        positions (filter #(= opposite-color (get-position board %))
                          (get-occupied-positions board))]
    (set (mapcat #(get-empty-neighbors board %) positions))))

(defn- explore
  "if a piece placed at position can cause other pieces to flip in the direction
   indicated by delta, then returns a sequence of all pieces that will flip.
   otherwise returns nil."
  [board position delta color]
  (let [opposite-color (opposite color)
        line (take-while valid-position? (shifts position delta))
        target-positions (take-while
                          #(= opposite-color (get-position board %))
                          line)
        occupants (map #(get-position board %) line)
        others (drop-while #(= opposite-color %) occupants)]
    (cond (empty? occupants) nil
          (empty? others) nil
          (not= opposite-color (first occupants)) nil
          (not= color (first others)) nil
          :else target-positions)))

(defn eligible-move?
  "returns true if position is a legal move for the color"
  [board position color]
  (cond (nil? (get-position board position))
        (some (comp not nil?) (for [delta deltas]
                      (explore board position delta color)))
        :else (throw (IllegalArgumentException.
                      (str position " is already occupied")))))

(defn find-eligible-moves
  "returns a sequence of positions corresponding to places where the color
   may place a piece on the board"
  [board color]
  (filter #(eligible-move? board % color)
       (find-potential-move-positions board color)))

(defn execute-move
  "returns a board which is the result of placing a piece of the given color
   at the indicated position on the given board"
  [board position color]
  (let [positions-to-flip (mapcat #(explore board position % color) deltas)]
    (cond (empty? positions-to-flip) (throw (IllegalArgumentException.
                                             (str position
                                                  " is not a legal move")))
          :else (set-positions board (conj positions-to-flip position) color))))
