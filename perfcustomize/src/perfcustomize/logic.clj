(ns perfcustomize.logic
  (:require [clojure.tools.logging :as log])
  )

(defn- round
  [value precision]
  (log/trace "Rounding " value " to " precision)
  (let [bd (BigDecimal. value)]
    (.toString (.setScale bd precision BigDecimal/ROUND_HALF_UP))))

(defn customize-performance
  "Expects a map with a key data that maps to the output of the data service,
  and a key preferences that maps to the output of the preference service.
  Returns the modified data"
  [{data :data, prefs :preferences}]
  ; TODO: what to customize - just the precision?
  (let [precision (:precision prefs)]
    (map #(assoc % :return (round (:return %) precision)) data)))


