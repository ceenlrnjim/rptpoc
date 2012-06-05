(ns orch.core
  (:import [org.stringtemplate.v4 ST])
  (:require [clojure.tools.logging :as log])
  (:require [clojure.data.json :as json])
  (:require [clj-http.client :as client]))

(defn- addall
  [template a s]
  (if (empty? s) 
    template
    (recur (.add template a (first s)) a (rest s))))

(defn- fill-template
  [template attr-seq-pairs]
  {:pre [(even? (count attr-seq-pairs))]}
  (if (empty? attr-seq-pairs) template
    (recur
      (addall template (first attr-seq-pairs) (second attr-seq-pairs))
      (rest (rest attr-seq-pairs)))))

(defn- build-data-url
  [prefs date]
  (let [st (fill-template (ST. "http://localhost:8080/?grpids=<grpids;separator=\",\">&lors=<lors;separator=\",\">&currencies=<currencies;separator=\",\">&tp=<tp;separator=\",\">&asof=<asof;separator=\",\">")
                           ["grpids" (map name (conj (keys (:benchmarks prefs)) (:grpid prefs)))
                           "lors" (:lors prefs)
                           "currencies" (:currencies prefs)
                           "asof" [date]
                           "tp" (:timeperiods prefs)])]
    (.render st)))

(defn -main [& args]
  (let [prefsjson (:body (client/get "http://localhost:8081/preference/AAAA"))
        prefs (json/read-json prefsjson)
        url (build-data-url prefs "20120131")
        datajson (:body (client/get url))
        data (json/read-json datajson)]
    (println (client/post "http://localhost:8082/customize"
                          {:body (json/json-str {:data data :preferences prefs})
                           :content-type "application/json"}))))
    

