(ns scjsv.core
  (:require [cheshire.core :as c])
  (:import [com.fasterxml.jackson.databind ObjectMapper JsonNode]
           [com.github.fge.jsonschema.main JsonSchemaFactory]
           [com.github.fge.jackson JsonLoader]
           [com.github.fge.jsonschema.core.report ListProcessingReport ProcessingMessage]
           [com.github.fge.jsonschema.main JsonSchema]))

(defn schema-object
  "Creates a JSON Schema Object either from a JSON String or a Clojure Map."
  [schema]
  (let [schema-string (if (string? schema)
                        schema
                        (c/generate-string schema))
        mapper (ObjectMapper.)
        schema-object (.readTree ^ObjectMapper mapper ^String schema-string)
        factory (JsonSchemaFactory/byDefault)
        schema-object (.getJsonSchema ^JsonSchemaFactory factory schema-object)]
    schema-object))

(defn validate-json
  "Validates a JSON string against a JSON Schema."
  [schema-object, json-string]
  (time (let [json-data (JsonLoader/fromString json-string)
              report (.validate ^JsonSchema schema-object ^JsonNode json-data)
              lp (doto (ListProcessingReport.) (.mergeWith report))
              errors (iterator-seq (.iterator lp))
              ->clj #(-> (.asJson ^ProcessingMessage %) str (c/parse-string true))]
          (if (seq errors)
            (map ->clj errors)))))

(defn validate
  "Validates a Clojure data structure against a JSON Schema."
  [schema, data]
  (validate-json schema (c/generate-string data)))
