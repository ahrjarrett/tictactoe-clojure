(ns tictactoe-clojure.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))



; every? will invoke it’s predicate function once with every?
; element from the sequence, and only if all of the calls to
; the predicate function return true, will the every? function
; return true. otherwise if any of the calls to the pred
; return false, every? returns false.

; sets: here the pred fn is a set, and a set can be invoked like a
; function, and when you call a set as a function you pass in a value,
; and if the value is contained in the set, the set will return that value.
(defn triple-winner? [triple]
  (if (every? #{:x} triple) :x
      (if (every? #{:o} triple) :o)))

(declare triples)

; returns :x if x’s win, :o if o’s win,
; otherwise nil if there’s no winner (not necessarily a draw)
(defn winner? [board]
  (first (filter #{:x :o} (map triple-winner? (triples board)))))

; returns a sequence of ‘triples’
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
