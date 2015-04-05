(ns scjsv.core-test
  (:require [midje.sweet :refer :all]
            [scjsv.core :refer :all]
            [clojure.java.io :as io]))

(def schema-string (slurp (io/resource "scjsv/schema.json")))
(def valid (slurp (io/resource "scjsv/valid.json")))
(def invalid (slurp (io/resource "scjsv/invalid.json")))

(fact "validate-json"
  (validate-json schema-string valid) => nil
  (validate-json schema-string invalid) =not=> nil)

(fact "validate"
  (validate schema-string
            {:shipping_address
             {:street_address "1600 Pennsylvania Avenue NW"
              :city "Washington"
              :state "DC"}
             :billing_address
             {:street_address "1st Street SE"
              :city "Washington"
              :state "DC"}}) => nil
  (validate schema-string
            {:shipping_address
             {:street_address "1600 Pennsylvania Avenue NW",
              :state "DC"}
             :evil true,
             :billing_address
             {:street_address "1st Street SE",
              :city "Washington",
              :state "DC"}}) =not=> nil)
