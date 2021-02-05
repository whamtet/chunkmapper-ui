(require '[cljs.build.api :as b])

(b/watch "src"
         {:main 'ctmx-electron.core
          :output-to "../ctmx_electron.js"
          :output-dir "out"
          :optimizations :none
          :target :nodejs
          :asset-path "ctmx-electron/out"})
