(ns othello.board)

(defrecord Position [x y])

(defn make-position
  "abstracts the implementation of board positions"
  [x y]
  (Position. x y))

(defn get-x
  "returns the x coordinate of the given position"
  [position]
  (:x position))

(defn get-y
  "returns the y coordinate of the given position"
  [position]
  (:y position))

(defn get-position
  "returns :white or :black corresponding to the color of piece
   occupying the given position, or nil if there is no piece there."
  [board position]
  (get-in board [:data position]))

(defn set-position
  "returns a board with the given position set to the indicated color"
  [board position color]
  (assoc-in board [:data position] color))

(defn set-positions
  "returns a board with all given positions set to the given color"
  [board positions color]
  (let [replacements (apply merge (for [pos positions] {pos color}))]
    (update-in board [:data] merge replacements)))

(defn- in-range?
  "returns true if the given number is greater than or equal to lower
   and less than or equal to upper, false otherwise"
  [number lower upper]
  (and (>= number lower)
       (<= number upper)))

(defn valid-position?
  "returns true if the given position is on the board, false otherwise"
  [position]
  (and (in-range? (get-x position) 0 7)
       (in-range? (get-y position) 0 7)))

(defn new-board
  "returns a board in initial position"
  []
  {:data {(make-position 3 3) :white
          (make-position 3 4) :black
          (make-position 4 3) :black
          (make-position 4 4) :white}})

(defn get-occupied-positions
  "returns a sequence of positions corresponding to places occupied
   by either a white or black piece"
  [board]
  (keys (:data board)))

(defn- board-to-string
  "generates a string representation of a board"
  [board]
  (let [characters {nil " " :white "O" :black "X"}
        column-numbers " 01234567\n"
        rng (range 0 8)
        rows (for [y rng]
               (str y
                (apply str (for [x rng]
                             (characters (get-position board (make-position x y)))))
                "\n"))]
    (str column-numbers (apply str rows))))

(defn print-board
  "pretty printer for game board"
  [board]
  (println (board-to-string board)))


