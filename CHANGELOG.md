## 0.5.0 (unreleased)

- You can now validate data with `java.io.Reader` as the input in addition to strings and Clojure maps. ([#10](https://github.com/metosin/scjsv/pull/10))
- Updated dependencies.

## 0.4.1 (2018-02-15)

- updated dependencies

## 0.4.0 (2016-10-04)

- Make the validator configurable via an options map. You can use it to enable [inline dereferencing][inline-deref] by passing `{:dereferencing :inline}` as the second parameter to `validator`/`json-validator`.

[inline-deref]: (http://json-schema.org/latest/json-schema-core.html#anchor30).

## 0.3.0 (2016-08-07)

- `validator` and `json-validator` now take a `JsonSchemaFactory` as an optional argument. This allows e.g. pre-loading schema definitions.
   - Thanks to [lvh](https://github.com/lvh)!

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

## 0.2.0 (2015-03-07)

- **BREAKING**: rewrote the whole public api to support caching of computed `JSONSchema`-objects.
- up to 1000x faster on a [sample-project](https://github.com/metosin/ring-swagger/blob/master/test/ring/swagger/validator.clj)

## 0.1.0 (2015-03-06)

- Initial public version
