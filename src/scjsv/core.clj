(ns scjsv.core
  (:import [com.fasterxml.jackson.databind ObjectMapper]
           [com.github.fge.jsonschema.main JsonSchemaFactory]
           [com.github.fge.jackson JsonLoader]
           [com.github.fge.jsonschema.core.report ListProcessingReport]
           [com.github.fge.jsonschema.main JsonSchema]))

(defn validate
  "validates a json-data against a json-schema"
  [^:String schema-string, ^:String data]
  (let [mapper             (ObjectMapper.)
        schema-object      (.readTree mapper schema-string)
        factory            (JsonSchemaFactory/byDefault)
        ^JsonSchema schema (.getJsonSchema factory schema-object)
        report             (.validate schema (JsonLoader/fromString data))
        lp                 (ListProcessingReport.)
        _                  (.mergeWith lp report)
        errors             (iterator-seq (.iterator lp))]
    (if (seq errors)
      (map str errors))))
