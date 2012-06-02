(ns perfpref.prefs)

(defn preferences
  [grpid]
  {:precision 3
   :grpid grpid
   :lors ["A" "B" "C"]
   :currencies ["USD"]
   :benchmarks {1234 "B1" 1235 "B2" 1236 "B3"}})
