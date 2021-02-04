(require '[cljs.build.api :as b])

(b/watch "src"
         {:main 'ctmx-electron.core
          :output-to "../ctmx_electron.js"
          :output-dir "out"
          :optimizations :whitespace
          :foreign-libs [{:file "htmx.js"
                          :provides ["htmx"]
                          :module-type :commonjs}]})
