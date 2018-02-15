(ns scjsv.core-test
  (:require [clojure.java.io :as io]
            [clojure.test :refer [deftest is testing]]
            [scjsv.core :as v])
  (:import [com.github.fge.jsonschema.core.exceptions ProcessingException]
           [com.github.fge.jsonschema.main JsonSchemaFactory]))

(deftest json-string-validation-test
  (testing "Validating JSON string against JSON Schema (as string)"
    (let [schema (slurp (io/resource "scjsv/schema.json"))
          validate (v/json-validator schema)
          valid (slurp (io/resource "scjsv/valid.json"))
          invalid (slurp (io/resource "scjsv/invalid.json"))]
      (is (nil? (validate valid)))
      (is (some? (validate invalid))))))

(deftest clojure-data-validation-test
  (testing "Validating Clojure data against JSON Schema (as Clojure)"
    (let [schema {:$schema "http://json-schema.org/draft-04/schema#"
                  :type "object"
                  :properties {:billing_address {:$ref "#/definitions/address"}
                               :shipping_address {:$ref "#/definitions/address"}}
                  :definitions {:address {:type "object"
                                          :properties {:street_address {:type "string"}
                                                       :city {:type "string"}
                                                       :state {:type "string"}}
                                          :required ["street_address", "city", "state"]}}}
          validate (v/validator schema)
          validate-with-explicit-factory (v/validator schema (JsonSchemaFactory/byDefault))
          valid {:shipping_address {:street_address "1600 Pennsylvania Avenue NW"
                                    :city "Washington"
                                    :state "DC"}
                 :billing_address {:street_address "1st Street SE"
                                   :city "Washington"
                                   :state "DC"}}
          invalid (update-in valid [:shipping_address] dissoc :state)]

      (is (nil? (validate valid)))
      (is (some? (validate invalid)))

      (is (nil? (validate-with-explicit-factory valid)))
      (is (some? (validate-with-explicit-factory invalid)))

      (testing "validation errors are lovely clojure maps"
        (is (= (validate invalid)
               [{:domain "validation"
                 :instance {:pointer "/shipping_address"}
                 :keyword "required"
                 :level "error"
                 :message "object has missing required properties ([\"state\"])"
                 :missing ["state"]
                 :required ["city" "state" "street_address"]
                 :schema {:loadingURI "#"
                          :pointer "/definitions/address"}}]))))))

(deftest self-referential-schema-test
  (testing "Validating a schema that refers to itself"
    (let [schema (slurp (io/resource "scjsv/with_id.json"))
          default-validate (v/validator schema)
          inline-validate (v/validator schema {:dereferencing :inline})
          valid {:foo "foo"}]
      (is (thrown? ProcessingException (default-validate valid)))
      (is (nil? (inline-validate valid))))))
