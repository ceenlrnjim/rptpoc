(ns svctemplate.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler])
  (:use ring.adapter.jetty)
  (:use ring.middleware.params)
  (:use ring.middleware.keyword-params)
  (:use ring.middleware.session)
  (:require [clojure.tools.logging :as log])
  (:require [clojure.data.json :as json])
  (:gen-class))

(defroutes main-routes
  (GET "/" [] 
       {:status 200
        :body "<h1>Hello World</h1>"
        :headers {"Content-Type" "text/html"}})
  (route/not-found "Page not found"))

(def app (handler/site main-routes))

(defn -main [& args]
  (let [[port & more] args]
    (run-jetty (wrap-session 
                 (wrap-params 
                   (wrap-keyword-params app))) 
               {:port (Integer/parseInt port)})))
