(defproject metosin/scjsv "0.5.1-SNAPSHOT"
  :description "Simple JSON-Schema validator for Clojure"
  :url "https://github.com/metosin/scjsv"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v20.html" }
  :dependencies [[metosin/jsonista "0.2.5"]
                 [com.github.java-json-tools/json-schema-validator "2.2.11"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0"]]
                   :plugins [[lein-codox "0.10.4"]
                             [lein-eftest "0.5.3"]]}}
  :codox {:metadata {:doc/format :markdown}}
  :deploy-repositories [["releases" :clojars]])
