(ns render.core
  (:require [clojure.data.json :as json])
  (:require [clojure.tools.logging :as log])
  (:import [org.stringtemplate.v4 ST STGroupFile]))

(defn- addattrs
  [template var-file-pairs]
  (if (empty? var-file-pairs) template
    (recur (.add template 
                 (first var-file-pairs) 
                 (json/read-json (slurp (second var-file-pairs)) false)) ; don't keywordize
           (rest (rest var-file-pairs)))))

(defn- render
  [template entry-point var-file-pairs]
  (let [stg (STGroupFile. template \$ \$) ; TODO: make delimiter argument
        st (.getInstanceOf stg entry-point)]
    (addattrs st var-file-pairs)
    (.render st)))

(defn -main [& args]
  (let [[template entry-point & var-file-pairs] args]
    (println (render template entry-point var-file-pairs))))
