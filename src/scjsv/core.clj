(ns scjsv.core
  (:require [cheshire.core :as c])
  (:import [com.fasterxml.jackson.databind JsonNode]
           [com.github.fge.jackson JsonLoader]
           [com.github.fge.jsonschema.main JsonSchemaFactory]
           [com.github.fge.jsonschema.core.load Dereferencing]
           [com.github.fge.jsonschema.core.load.configuration LoadingConfiguration]
           [com.github.fge.jsonschema.core.report ListProcessingReport ProcessingMessage]
           [com.github.fge.jsonschema.main JsonSchema]))

(defn- build-factory
  "Creates a JsonSchemaFactory based on the options map."
  [{:keys [dereferencing] :or {dereferencing :canonical}}]
  (let [dereferencing-mode (case dereferencing
                             :inline (Dereferencing/INLINE)
                             :canonical (Dereferencing/CANONICAL))
        loading-config (-> (LoadingConfiguration/newBuilder)
                           (.dereferencing dereferencing-mode)
                           (.freeze))]
    (-> (JsonSchemaFactory/newBuilder)
        (.setLoadingConfiguration loading-config)
        (.freeze))))

(defn- ->json-schema
  "Creates a JSONSchema instance either from a JSON string or a Clojure Map."
  [schema ^JsonSchemaFactory factory]
  (let [schema-string (if (string? schema)
                        schema
                        (c/generate-string schema))
        schema-object (JsonLoader/fromString schema-string)]
    (.getJsonSchema factory schema-object)))

(defn- validate
  "Validates (f data) against a given JSON Schema."
  ([json-schema data]
   (validate json-schema data false))
  ([json-schema data deep-check]
   (let [json-data (JsonLoader/fromString data)
         report (.validate ^JsonSchema json-schema ^JsonNode json-data deep-check)
         lp (doto (ListProcessingReport.) (.mergeWith report))
         errors (iterator-seq (.iterator lp))
         ->clj #(-> (.asJson ^ProcessingMessage %) str (c/parse-string true))]
     (if (seq errors)
       (map ->clj errors)))))

(defn- ->factory
  "Converts value to a JsonSchemaFactory if it isn't one."
  [value]
  (cond
    (instance? JsonSchemaFactory value) value
    (map? value) (build-factory value)
    :else (throw (Exception. (str "Don't know how to convert " (pr-str value)
                                  " into a JsonSchemaFactory.")))))

;;
;; Public API
;;

(defn json-validator
  "Returns a JSON string validator (a single arity fn).
  Schema can be given either as a JSON String or a Clojure Map.

  To configure the validator, you can pass a JsonSchemaFactory instance or a
  options map as the second parameter. See scjsv.core/validator docstring for
  the options."
  ([schema]
   (json-validator schema (build-factory {})))
  ([schema json-schema-factory]
   (partial validate (->json-schema schema (->factory json-schema-factory)))))

(defn validator
  "Returns a Clojure data structure validator (a single arity fn).
  Schema can be given either as a JSON String or a Clojure Map.

  To configure the validator, you can pass a JsonSchemaFactory instance or an
  options map as the second parameter. The options map can have the following
  keys:

  :dereferencing -- Which dereferencing mode to use. Either :canonical (default)
                    or :inline."
  ([schema]
   (validator schema (build-factory {})))
  ([schema json-schema-factory]
   (comp (partial validate (->json-schema schema (->factory json-schema-factory)))
         c/generate-string)))
