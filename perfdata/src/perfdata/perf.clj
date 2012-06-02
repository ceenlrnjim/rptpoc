(ns perfdata.perf)

(defn returns
  "Returns a sequence of returns, takes a map of returns
  (keys :grpid, :lor, :date, :currency) and adds :return with the value"
  [returnseq]
  ; adding a random return
  (map #(assoc % :return (rand)) returnseq))
