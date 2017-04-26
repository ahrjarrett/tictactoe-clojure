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
