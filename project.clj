(defproject metosin/scjsv "0.2.0"
  :description "Simple JSON-Schema validator for Clojure"
  :url "https://github.com/metosin/scjsv"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[cheshire "5.5.0"]
                 [com.github.fge/json-schema-validator "2.2.6"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.7.0"]
                                  [midje "1.8.2"]]
                   :plugins [[lein-clojars "0.9.1"]
                             [lein-ring "0.9.7"]
                             [lein-midje "3.2"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]
                                  [org.codehaus.jsr166-mirror/jsr166y "1.7.0"]]}}
  :aliases {"test-ancient" ["midje"]})
