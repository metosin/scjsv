## *.*.* 

- updated dependencies:

```clojure
[cheshire "5.5.0"] is available but we use "5.4.0"
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
