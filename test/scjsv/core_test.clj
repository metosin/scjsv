(ns scjsv.core-test
  (:require [midje.sweet :refer :all]
            [scjsv.core :refer :all]
            [clojure.java.io :as io]))

(def schema-string (slurp (io/resource "scjsv/schema.json")))
(def valid (slurp (io/resource "scjsv/valid.json")))
(def invalid (slurp (io/resource "scjsv/invalid.json")))

(fact "validate"
  (validate schema-string valid) => nil
  (validate schema-string invalid) =not=> nil)
