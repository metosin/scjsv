(defproject metosin/scjsv "0.4.1"
  :description "Simple JSON-Schema validator for Clojure"
  :url "https://github.com/metosin/scjsv"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :dependencies [[cheshire "5.8.0"]
                 [com.github.java-json-tools/json-schema-validator "2.2.8"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0"]
                                  [org.codehaus.jsr166-mirror/jsr166y "1.7.0"]
                                  [midje "1.9.1"]]
                   :plugins [[lein-clojars "0.9.1"]
                             [lein-ring "0.9.7"]
                             [lein-midje "3.2"]
                             [funcool/codeina "0.5.0" :exclude [org.clojure/clojure]]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}}
  :aliases {"test-ancient" ["midje"]}
  :deploy-repositories [["releases" :clojars]]
  :codeina {:sources ["src"]
            :target "gh-pages/doc"
            :src-uri "http://github.com/metosin/scjsv/blob/master/"
            :src-uri-prefix "#L"})
