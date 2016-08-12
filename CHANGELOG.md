## Unreleased

- Default to [inline dereferencing][inline-deref]. According to the JSON Schema Core specification draft v4, this may break some schemas:

  > Inline dereferencing can produce canonical URIs which differ from the canonical URI of the root schema. Schema authors SHOULD ensure that implementations using canonical dereferencing obtain the same content as implementations using inline dereferencing.

  To use canonical dereferencing (the old behavior), pass a suitable `JsonSchemaFactory` to `validator`/`json-validator`.

[inline-deref]: (http://json-schema.org/latest/json-schema-core.html#anchor30).

## 0.3.0 (7.8.2016)

- `validator` and `json-validator` now take a `JsonSchemaFactory` as an optional argument. This allows e.g. pre-loading schema definitions.

- updated dependencies:

```clojure
[cheshire "5.6.3"] is available but we use "5.4.0"
[org.clojure "1.8.0"] is available but we use "1.7.0"
```

- dev-dependencies:

```clojure
[lein-ring "0.9.6"] is available but we use "0.9.4"
[midje "1.7.0"] is available but we use "1.7.0-SNAPSHOT"
```

## 0.2.0 (7.3.2015)

- **BREAKING**: rewrote the whole public api to support caching of computed `JSONSchema`-objects.
- up to 1000x faster on a [sample-project](https://github.com/metosin/ring-swagger/blob/master/test/ring/swagger/validator.clj)

## 0.1.0 (6.3.2015)

- Initial public version
