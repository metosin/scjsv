# scjsv

Simple Clojure JSON-Schema Validator - a lightweight clojure wrapper on top of [com.github.fge/json-schema-validator](https://github.com/fge/json-schema-validator).

## Usage

* `validate-json` to validate JSON strings agains a JSON Schema
* `validate` to validate Clojure data against a JSON Schema
* JSON Schemas can be represented either as JSON strings or as Clojure Maps

```clojure
(require '[scjsv.core :as v])

(def schema {:$schema "http://json-schema.org/draft-04/schema#",
             :type "object",
             :properties {:id {:type "integer"}},
             :required [:id]})

(v/validate schema {:id 1})
; nil

(v/validate schema {})
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
