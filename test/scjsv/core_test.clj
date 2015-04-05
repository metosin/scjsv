(ns scjsv.core-test
  (:require [midje.sweet :refer :all]
            [scjsv.core :refer :all]
            [clojure.java.io :as io]))

(def schema-string (slurp (io/resource "scjsv/schema.json")))

(fact "validate-json"
  (let [valid (slurp (io/resource "scjsv/valid.json"))
        invalid (slurp (io/resource "scjsv/invalid.json"))]
    (validate-json schema-string valid) => nil
    (validate-json schema-string invalid) =not=> nil))

(fact "validate"
  (let [valid {:shipping_address
              {:street_address "1600 Pennsylvania Avenue NW"
               :city "Washington"
               :state "DC"}
              :billing_address
              {:street_address "1st Street SE"
               :city "Washington"
               :state "DC"}}
        invalid (update-in valid [:shipping_address] dissoc :state)]
    (validate schema-string valid) => nil
    (validate schema-string invalid) =not=> nil

    (fact "validation errors are lovey clojure maps"
      (validate schema-string invalid)
      => [{:domain "validation"
           :instance {:pointer "/shipping_address"}
           :keyword "required"
           :level "error"
           :message "object has missing required properties ([\"state\"])"
           :missing ["state"]
           :required ["city" "state" "street_address"]
           :schema {:loadingURI "#"
                    :pointer "/definitions/address"}}])))
