(ns ctmx-electron.ui
  (:require
    ctmx.rt)
  (:require-macros
    [ctmx.core :as ctmx]))

(ctmx/defcomponent panel [req]
  [:div
   [:h2.text-center "Chunkmapper"]])
