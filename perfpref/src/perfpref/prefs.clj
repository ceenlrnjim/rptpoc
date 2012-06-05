(ns perfpref.prefs
  (:require [clojure.tools.logging :as log])
  )

(defn preferences
  [grpid]
  (log/debug "Returning preferences for id" grpid)
  {:precision 3
   :grpid grpid
   :lors ["A" "B" "C"]
   :timeperiods ["1mo" "2mo" "3mo" "6mo" "9mo" "1y" "3y" "5y" "inception"]
   :currencies ["USD"]
   :benchmarks {"1234" "B1" "1235" "B2" "1236" "B3"}})
