(ns scjsv.core
  (:require [cheshire.core :as c])
  (:import [com.fasterxml.jackson.databind ObjectMapper]
           [com.github.fge.jsonschema.main JsonSchemaFactory]
           [com.github.fge.jackson JsonLoader]
           [com.github.fge.jsonschema.core.report ListProcessingReport]
           [com.github.fge.jsonschema.main JsonSchema]))

(defn validate-json
  "Validates a JSON string against a JSON Schema.
  JSON Schema can be represented as a JSON string or a Clojure Map."
  [schema, json-string]
  (let [schema-string (if (string? schema)
                        schema
                        (c/generate-string schema))
        mapper (ObjectMapper.)
        schema-object (.readTree mapper schema-string)
        factory (JsonSchemaFactory/byDefault)
        ^JsonSchema schema-object (.getJsonSchema factory schema-object)
        report (.validate schema-object (JsonLoader/fromString json-string) true)
        lp (doto (ListProcessingReport.) (.mergeWith report))
        errors (iterator-seq (.iterator lp))
        ->clj #(-> (.asJson %) str (c/parse-string true))]
    (if (seq errors)
      (map ->clj errors))))

(defn validate
  "Validates a Clojure data structure against a JSON Schema.
  JSON Schema can be represented as a JSON string or a Clojure Map."
  [schema, data]
  (validate-json schema (c/generate-string data)))
