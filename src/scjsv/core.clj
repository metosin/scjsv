(ns scjsv.core
  (:require [cheshire.core :as c])
  (:import [com.fasterxml.jackson.databind ObjectMapper]
           [com.github.fge.jsonschema.main JsonSchemaFactory]
           [com.github.fge.jackson JsonLoader]
           [com.github.fge.jsonschema.core.report ListProcessingReport]
           [com.github.fge.jsonschema.main JsonSchema]))

(defn- get-schema
  "returns JsonSchema-object"
  [schema-string]
  (let [mapper             (ObjectMapper.)
        schema-object      (.readTree mapper schema-string)
        factory            (JsonSchemaFactory/byDefault)
        ^JsonSchema schema (.getJsonSchema factory schema-object)]
    schema))

;;
;; public api
;;

(defn validate-json
  "validates a json-string against a json-schema-string"
  [schema-string, json-string]
  (let [schema (get-schema schema-string)
        report (.validate schema (JsonLoader/fromString json-string) true)
        lp     (doto (ListProcessingReport.) (.mergeWith report))
        errors (iterator-seq (.iterator lp))
        ->clj  #(-> (.asJson %) str (c/parse-string true))]
    (if (seq errors)
      (map ->clj errors))))

(defn validate
  "validates a clojure data structure against a json-schema-string"
  [schema-string, data]
  (validate-json schema-string (c/generate-string data)))
