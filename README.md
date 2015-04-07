# scjsv [![Build Status](https://travis-ci.org/metosin/scjsv.png?branch=master)](https://travis-ci.org/metosin/scjsv) [![Dependencies Status](http://jarkeeper.com/metosin/scjsv/status.png)](http://jarkeeper.com/metosin/scjsv)

Simple Clojure JSON-Schema Validator - on top of [com.github.fge/json-schema-validator](https://github.com/fge/json-schema-validator).

## Latest version

[![Clojars Project](http://clojars.org/metosin/scjsv/latest-version.svg)](http://clojars.org/metosin/scjsv)

## Usage

* `schema-object` to create JSON Schema objects from JSON Strings or Clojure Maps.
* `validate-json` to validate JSON strings agains a JSON Schema
* `validate` to validate Clojure data against a JSON Schema

```clojure
(require '[scjsv.core :as v])

(def schema {:$schema "http://json-schema.org/draft-04/schema#"
             :type "object"
             :properties {:id {:type "integer"}}
             :required [:id]})

; internally cached schema 
(def validate (partial v/validate (v/schema-object schema)))

(validate {:id 1})
; nil

(validate {})
; ({:domain "validation"
;   :instance {:pointer ""}
;   :keyword "required"
;   :level "error"
;   :message "object has missing required properties ([\"id\"])"
;   :missing ["id"]
;   :required ["id"]
;   :schema {:loadingURI "#" :pointer ""}})
```

## License

Copyright © 2015 [Metosin Oy](http://www.metosin.fi)

Distributed under the Eclipse Public License, the same as Clojure.
