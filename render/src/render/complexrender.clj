(ns render.complexrender
  (:require [clojure.data.json :as json])
  (:require [clojure.tools.logging :as log])
  (:require [clojure.xml :as xml])
  (:import [org.stringtemplate.v4 ST STGroupString]))


(defn- addattrs
  [template var-file-pairs]
  (if (empty? var-file-pairs) template
    (recur (.add template 
                 (first var-file-pairs) 
                 (json/read-json (second var-file-pairs) false)) ; don't keywordize
           (rest (rest var-file-pairs)))))

(defn- render
  [template-def entry-point var-file-pairs]
  (let [stg (STGroupString. "<string>" template-def \$ \$) ; TODO: make delimiter argument
        st (.getInstanceOf stg entry-point)]
    (addattrs st var-file-pairs)
    (.render st)))

(defn- run-on-tag
  [tree tagname thunk]
  (for [x (xml-seq tree) :when (= tagname (:tag x))] (thunk x)))

(defn- process-data-element
  [data-element-tag]
  ;(println "Processing tag:" data-element-tag)
  (let [preprocess (first (run-on-tag data-element-tag :dataPreProcess #(first (:content %))))
        data (first (run-on-tag data-element-tag :data #(first (:content %))))
        varname (:name (:attrs data-element-tag))]
    ;(println "Preprocess: " preprocess)
    ;(println "data: " data)
   ; (println "varname: " varname)
    [varname (if (nil? preprocess) data ((load-string preprocess) data))]))

(defn -main [& args]
  (let [[filename & more] args
        xmltree (xml/parse filename)
        template-def (first (for [x (xml-seq xmltree) :when (= :reportTemplate (:tag x))] (first (:content x))))
        entry-point (first (run-on-tag xmltree :reportTemplate #(:entry (:attrs %))))
        template-args (for [x (xml-seq xmltree) :when (= :dataElement (:tag x))] (process-data-element x))]
    (println (render template-def entry-point (reduce concat [] template-args)))))

