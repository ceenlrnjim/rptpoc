(ns perfpref.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler])
  (:use ring.adapter.jetty)
  (:use ring.middleware.params)
  (:use ring.middleware.keyword-params)
  (:use ring.middleware.session)
  (:require [clojure.tools.logging :as log])
  (:require [clojure.data.json :as json])
  (:require [perfpref.prefs :as prefs])
  (:gen-class))

(defroutes main-routes
  (GET "/preference/:clientid" [id] 
       {:status 200
        :body (json/json-str (prefs/preferences id))
        :headers {"Content-Type" "application/json"}})
  (route/not-found "Page not found"))

(def app
  (handler/site main-routes))

(defn -main [& args]
  (let [[port & more] args]
    (run-jetty (wrap-session (wrap-params (wrap-keyword-params app))) {:port (Integer/parseInt port)})))
