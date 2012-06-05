(ns perfpref.prefs
  (:require [clojure.tools.logging :as log])
  )

(defn preferences
  [grpid]
  (log/debug "Returning preferences for id" grpid)
  {:precision 3
   :grpid grpid
   :lors ["A" "B" "C"]
   :currencies ["USD"]
   :benchmarks {"1234" "B1" "1235" "B2" "1236" "B3"}})
