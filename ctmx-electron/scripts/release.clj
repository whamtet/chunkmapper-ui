(require '[cljs.build.api :as b])

(println "Building ...")

(let [start (System/nanoTime)]
  (b/build "src"
           {:output-to "../ctmx_electron.js"
            :output-dir "release"
            :optimizations :simple
            :verbose true
            :foreign-libs [{:file "htmx.js"
                            :provides ["htmx"]
                            :module-type :commonjs}]})
  (println "... done. Elapsed" (/ (- (System/nanoTime) start) 1e9) "seconds"))
