(defproject ctmx-electron "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.10.2"]
                 [org.clojure/clojurescript "1.10.773"]
                 [ctmx "0.1.0-SNAPSHOT"]
                 [alandipert/storage-atom "1.2.4"]]
  :jvm-opts ^:replace ["-Xmx1g" "-server"]
  :plugins [[lein-npm "0.6.2"]]
  :npm {:dependencies [[source-map-support "0.4.0"]]}
  :source-paths ["src" "target/classes"]
  :clean-targets [:target-path "out" "release"]
  :target-path "target")
