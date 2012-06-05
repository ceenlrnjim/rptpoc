(ns perfdata.core
  (:use ring.adapter.jetty)
  (:use ring.middleware.params)
  (:use ring.middleware.keyword-params)
  (:use ring.middleware.session)
  (:require [clojure.tools.logging :as log])
  (:require [clojure.math.combinatorics :as comb])
  (:require [clojure.data.json :as json])
  (:use perfdata.perf)
  (:gen-class))

(defn- splitseq
  [s]
  (.split s ","))

(defn- make-returns
  [grpids lors currencies asof timeperiods]
  (map #(assoc {} :grpid (nth % 0) 
                  :lor (nth % 1)
                  :currency (nth % 2)
                  :asof (nth % 3)
                  :timeperiod (nth % 4))
       (comb/cartesian-product grpids lors currencies asof timeperiods)))

(defn handler [request]
  (println (:params request))
  {:status 200
   :body (json/json-str
            (returns (make-returns (splitseq (:grpids (:params request)))
                                  (splitseq (:lors (:params request)))
                                  (splitseq (:currencies (:params request)))
                                  (splitseq (:asof (:params request)))
                                   (splitseq (:tp (:params request))))))
    :headers {"Content-Type" "application/json"}})

           

(defn -main [& args]
  (let [[port & more] args]
    (run-jetty (wrap-session (wrap-params (wrap-keyword-params handler))) {:port (Integer/parseInt port)})))
