## 0.6.1 (2020-05-16)

- updated dependencies:

```clj
[metosin/jsonista "0.2.6"] is available but we use "0.2.5"
[com.github.java-json-tools/json-schema-validator "2.2.13"] is available but we use "2.2.11"
```

## 0.6.0 (2019-11-20)

- jsonista is used internally instead of Cheshire. ([#12](https://github.com/metosin/scjsv/issues/12))
- scjsv now requires Java 1.8 or later.
- Make `:deep-check` configurable via the factory functions. ([#13](https://github.com/metosin/scjsv/pull/13))
- Updated dependencies

## 0.5.0 (2018-09-25)

- You can now validate data with `java.io.Reader` as the input in addition to strings and Clojure maps. ([#10](https://github.com/metosin/scjsv/pull/10))
- You can validate children of a container even if the container itself is invalid by passing `{:deep-check true}` to the validator function. ([#7](https://github.com/metosin/scjsv/pull/7))
- Updated dependencies.

## 0.4.1 (2018-02-15)

- updated dependencies

## 0.4.0 (2016-10-04)

- Make the validator configurable via an options map. You can use it to enable [inline dereferencing][inline-deref] by passing `{:dereferencing :inline}` as the second parameter to `validator`/`json-validator`.

[inline-deref]: (http://json-schema.org/latest/json-schema-core.html#anchor30).

## 0.3.0 (2016-08-07)
the
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
