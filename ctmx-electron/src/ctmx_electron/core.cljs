(ns ctmx-electron.core
  (:require
    [ctmx.intercept :as intercept]
    [ctmx-electron.ui :as ui]
    ctmx.rt
    hiccups.runtime)
  (:require-macros
    [ctmx.core :as ctmx]
    [hiccups.core :as hiccups]))

(enable-console-print!)

(println "MyActivity Hello world!")

(ctmx/defstatic main []
  (let [content (js/document.getElementById "content")]
    (set! (.-innerHTML content)
          (hiccups/html
            (ui/panel nil)))
    (js/htmx.process content)))

(intercept/set-responses!
  (ctmx/metas main))

(main)

(set! js/htmx.config.defaultSettleDelay 0)
(set! js/htmx.config.defaultSwapStyle "outerHTML")
