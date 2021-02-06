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

(def default-req {:params {}})

(ctmx/defstatic main []
  (let [content (js/document.getElementById "content")]
    (set! (.-innerHTML content)
          (hiccups/html
            (ui/panel default-req)))
    (js/htmx.process content)))

(intercept/set-responses!
  (ctmx/metas main))

(main)

(set! js/htmx.config.defaultSettleDelay 0)
(set! js/htmx.config.defaultSwapStyle "outerHTML")
