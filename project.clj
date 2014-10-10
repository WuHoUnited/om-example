(defproject om-example "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.7.3"]
                 [sablono "0.2.22"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]]

  :source-paths ["src"]

  :cljsbuild {:builds [{:id "prod"
                        :source-paths ["src"]
                        :compiler {
                                   :output-to "public/prod/om_example.js"
                                   :optimizations :advanced
                                   :pretty-print false
                                   :preamble ["react/react.min.js"]
                                   :externs ["react/externs/react.js"]}}
                       {:id "dev"
                        :source-paths ["src"]
                        :compiler {
                                   :output-to "public/dev/om_example.js"
                                   :output-dir "out"
                                   :optimizations :none
                                   :source-map true}}]})
