(ns scjsv.core
  (:require [cheshire.core :as c])
  (:import [com.fasterxml.jackson.databind ObjectMapper JsonNode]
           [com.github.fge.jsonschema.main JsonSchemaFactory]
           [com.github.fge.jackson JsonLoader]
           [com.github.fge.jsonschema.core.report ListProcessingReport ProcessingMessage]
           [com.github.fge.jsonschema.main JsonSchema]))

(defn- ->json-schema
  "Creates a JSONSchema instance either from a JSON string or a Clojure Map."
  [schema factory]
  (let [schema-string (if (string? schema)
                        schema
                        (c/generate-string schema))
        mapper (ObjectMapper.)
        schema-object (.readTree ^ObjectMapper mapper ^String schema-string)
        json-schema (.getJsonSchema ^JsonSchemaFactory factory ^JsonNode schema-object)]
    json-schema))

(defn- validate
  "Validates (f data) against a given JSON Schema."
  [json-schema data]
  (let [json-data (JsonLoader/fromString data)
        report (.validate ^JsonSchema json-schema ^JsonNode json-data)
        lp (doto (ListProcessingReport.) (.mergeWith report))
        errors (iterator-seq (.iterator lp))
        ->clj #(-> (.asJson ^ProcessingMessage %) str (c/parse-string true))]
    (if (seq errors)
      (map ->clj errors))))

;;
;; Public API
;;

(defn json-validator
  "Returns a Clojure data structure validator (a single arity fn).
  Schema can be given either as a JSON String or a Clojure Map."
  ([schema]
   (json-validator schema (JsonSchemaFactory/byDefault)))
  ([schema json-schema-factory]
   (partial validate (->json-schema schema json-schema-factory))))

(defn validator
  "Returns a JSON string validator (a single arity fn).
  Schema can be given either as a JSON String or a Clojure Map."
  ([schema]
   (validator schema (JsonSchemaFactory/byDefault)))
  ([schema json-schema-factory]
   (comp (partial validate (->json-schema schema json-schema-factory))
         c/generate-string)))
