(defproject metosin/scjsv "0.6.2"
  :description "Simple JSON-Schema validator for Clojure"
  :url "https://github.com/metosin/scjsv"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v20.html" }
  :dependencies [[metosin/jsonista "0.3.1"]
                 [com.github.java-json-tools/json-schema-validator "2.2.14"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.3"]]
                   :plugins [[lein-codox "0.10.7"]
                             [lein-eftest "0.5.9"]]}}
  :codox {:metadata {:doc/format :markdown}}
  :deploy-repositories [["releases" :clojars]])
