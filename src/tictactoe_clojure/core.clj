(ns tictactoe-clojure.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))


; EVERY? will invoke it’s predicate function once with every?
; element from the sequence, and only if all of the calls to
; the predicate function return true, will the every? function
; return true. otherwise if any of the calls to the pred
; return false, every? returns false.
; SETS: here the pred fn is a set, and a set can be invoked like a
; function, and when you call a set as a function you pass in a value,
; and if the value is contained in the set, the set will return that value.
(defn triple-winner? [triple]
  (if (every? #{:x} triple) :x
      (if (every? #{:o} triple) :o)))

; DECLARES is preferred in this scenario b/c it better establishes our intent.
(declare triples)

; WINNER? returns :x if x’s win, :o if o’s win,
; otherwise nil if there’s no winner (not necessarily a draw)
; here we’re mapping the triple-winner? function over the set
; returned by triples, and filtering out any values that are
; not :x or :o (in other words, nil values).
; NOTE: not 100% sure why we’re using first here though.
; Ohh: the map function returns either :x or :o if there is already
; a winner, so filter removed nil and then first takes the one and
; only value that is contained in the set returned by filter (which is the winner).
(defn winner? [board]
  (first (filter #{:x :o} (map triple-winner? (triples board)))))

; TRIPLES returns a sequence of ‘triples’
; (the rows, columns, and diagonals of the board)
(defn triples [board]
  (concat
    (partition-all 3 board)                           ; the rows
    (list
      (take-nth 3 board)                              ; column 1
      (take-nth 3 (drop 1 board))                     ; column 2
      (take-nth 3 (drop 2 board))                     ; column 3
      (take-nth 4 board)                              ; top-left to bottom-right
      (take-nth 3 (drop-last 2 (drop 2 board))))))    ; top-right to bottom-left

; FULL-BOARD just checks to see if all the values of the board
; are found in the set, returns true, otherwise false if there
; are any nils.
(defn full-board? [board]
  (every? #{:x :o} board))

; PRINT-BOARD is our first impure function.
; If the argument to the lambda function is a keyword,
; the argument is converted to a string, otherwise
; if just returns the argument.
; SUBS is returning a substring of all the characters starting
; at index 1 of the str, so everything from the 2nd char to the end.
; (Removing the ":" from ":x" or ":o"
(defn print-board [board]
  (let [board (map #(if (keyword? %) (subs (str %) 1) %) board)]
    (println (nth board 0) (nth board 1) (nth board 2))
    (println (nth board 3) (nth board 4) (nth board 5))
    (println (nth board 6) (nth board 7) (nth board 8))))

; PLAYER-NAME translates :x or :o into "x" or "o"
(defn player-name [player]
  (subs (str play) 1))

; STARTING-BOARD initializes the playing board with values
; that stand in (actually not NIL as said before, but numbers
; to be more user-friendly.
(def starting-board [1 2 3 4 5 6 7 8 9])

; PLAYER-SEQUENCE: the cycle function returns an infinitely lazy sequence,
; made up of its sequence argument elements repeated ad infinitum.
(def player-sequence (cycle [:x :o]))

; GET-MOVE: uses Java static method parseInt to parse to string returned from read-line as an Integer.
; READ-LINE is a function that will block the program until the user enters something into the command-line.
; We have this function in a try block b/c if the input is not an integer, it will throw an exception.
(def get-move [board]
  (let [input (try
          (. Integer parseInt (read-line))
          (catch Exception e nil))]
    (if (some #{input} board)
        input
        nil)))

; TAKE-TURN: (impure)
; ‘player’ as identified by their mark (:x or :o).
; If the move is valid, the call to ASSOC will take the current state
; of the board, and at the index (which is 1 less than MOVE),
; at that index we want to replace the slot number representing the player
; who made the move. ASSOC returns a modification to the board.
; The new vector returned by ASSOC gets returned by the IF,
; and returned the LOOP, which is returned by the function.
; TAKE-TURN then returns the new state of the board.
(defn take-turn [player board]
  (println "Select your move, player" (player-name player) " (press 1-9 and hit enter):")
  (loop [move (get-move board)]
    (if move
      (assoc board (dec move) player)
      (do
        (println "Move was invalid. Select your move, player."
        (recur get-move board))))))

; PLAY-GAME: This is the kick-off function.
; For the first iteration of loop we bind board to STARTING-BOARD,
; and player-sequence to PLAYER-SEQUENCE.
; In each iteration, we are rebnding the board a new state of
; the board in which case one of the players has taken a turn,
; and so for each iteration we are using first and rest to cycle
; through, alternating both players.
(defn play-game []
  (loop [board starting-board player-sequence player-sequence]
    (let [winner (winner? board)]
      (println "Current board:")
      (print-board board)
      (cond
        winner (println "Player" (player-name winner) "wins!")
        (full-board? board) (println "Game is a draw.")
        :else
          (recur
            (take-turn (first player-sequence) board)
            (rest player-sequence))))))

(play-game)
