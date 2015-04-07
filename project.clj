(defproject metosin/scjsv "0.2.0-SNAPSHOT"
  :description "Simple JSON-Schema validator for Clojure"
  :url "https://github.com/metosin/scjsv"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [cheshire "5.4.0"]
                 [com.github.fge/json-schema-validator "2.2.6"]]
  :profiles {:dev {:dependencies [[midje "1.7.0-SNAPSHOT"]]
                   :plugins [[lein-clojars "0.9.1"]
                             [lein-ring "0.9.3"]
                             [lein-midje "3.1.3"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0-alpha4"]]}}
  :aliases {"test-ancient" ["midje"]})
