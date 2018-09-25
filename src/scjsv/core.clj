(ns scjsv.core
  "Use [[validator]], [[json-validtor]], or [[json-reader-validator]] to
  construct a validator function.

  ### Validator functions

  The first argument for the validator function is the data. The optional second
  argument is an options map with the following keys:

  | key           | default  | description |
  |---------------|----------|-------------|
  | `:deep-check` | `false`  | Check nested elements even if the parent elements are invalid.
  "
  (:require [cheshire.core :as c]
            [cheshire.factory :as cheshire-factory])
  (:import [com.fasterxml.jackson.databind JsonNode ObjectMapper]
           [com.github.fge.jackson JsonNodeReader]
           [com.github.fge.jsonschema.main JsonSchemaFactory]
           [com.github.fge.jsonschema.core.load Dereferencing]
           [com.github.fge.jsonschema.core.load.configuration LoadingConfiguration]
           [com.github.fge.jsonschema.core.report ListProcessingReport ProcessingMessage]
           [com.github.fge.jsonschema.main JsonSchema]
           [java.io Reader]))

(defn- build-reader
  "Build a node reader based on the defaults of the cheshire json-factory"
  []
  (let [mapper (ObjectMapper. cheshire-factory/json-factory)]
    (JsonNodeReader. mapper)))

(def ^JsonNodeReader reader (build-reader))


(defn- ^JsonNode reader->json-node
  "Creates a JsonNode from a Reader"
  [^Reader data-reader]
  (.fromReader reader data-reader))

(defn- ^JsonNode string->json-node
  "Creates a JsonNode from a String"
  [^String data]
  (reader->json-node (java.io.StringReader. data)))


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
  "Creates a JSONSchema instance either from a JSON string or a Clojure map."
  [schema ^JsonSchemaFactory factory]
  (let [schema-string (if (string? schema)
                        schema
                        (c/generate-string schema))
        schema-object (string->json-node schema-string)]
    (.getJsonSchema factory schema-object)))

(defn- validate
  "Validates (f json-data) against a given JSON Schema."
  ([^JsonSchema json-schema
    ^JsonNode json-data
    {:keys [deep-check] :or {deep-check false}}]
   (let [report (.validate json-schema json-data deep-check)
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

(defn- build-validator
  "Returns a validator function. Schema can be given either as a JSON String or
  a Clojure map.

  `->json-node` is the function which will be applied to datum to transform them into
  a JsonNode"
  [schema json-schema-factory ->json-node]
  (fn validator
    ([data] (validator data nil))
    ([data opts]
     (validate (->json-schema schema (->factory (or json-schema-factory
                                                    (build-factory {}))))
               (->json-node data)
               opts))))

(defn json-reader-validator
  "Returns a `java.io.Reader` validator function. Schema can be given either as
  a JSON String or a Clojure map.

  To configure the validator, you can pass a `JsonSchemaFactory` instance or a
  options map as the second parameter. See [[scjsv.core/validator]] docstring for
  the options."
  ([schema]
   (json-reader-validator schema (build-factory {})))
  ([schema json-schema-factory]
   (build-validator schema json-schema-factory reader->json-node)))


(defn json-validator
  "Returns a JSON string validator function. Schema can be given either as a
  JSON String or a Clojure map.

  To configure the validator, you can pass a `JsonSchemaFactory` instance or a
  options map as the second parameter. See [[scjsv.core/validator]] docstring for
  the options."
  ([schema]
   (json-validator schema (build-factory {})))
  ([schema json-schema-factory]
   (build-validator schema json-schema-factory string->json-node)))



(defn validator
  "Returns a Clojure data structure validator function. Schema can be given
  either as a JSON String or a Clojure map.

  To configure the validator, you can pass a `JsonSchemaFactory` instance or an
  options map as the second parameter. The options map can have the following
  keys:

  | key              | default      | description  |
  |------------------|--------------|--------------|
  | `:dereferencing` | `:canonical` | Which dereferencing mode to use. Either `:canonical` or `:inline`."
  ([schema]
   (validator schema (build-factory {})))
  ([schema json-schema-factory]
   (build-validator schema json-schema-factory
                    (comp string->json-node c/generate-string))))
