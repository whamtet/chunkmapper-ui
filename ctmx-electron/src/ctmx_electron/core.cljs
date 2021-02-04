(ns ctmx-electron.core
  (:require
    [ctmx.intercept :as intercept]
    ctmx.rt
    hiccups.runtime
    htmx)
  (:require-macros
    [ctmx.core :as ctmx]
    [hiccups.core :as hiccups]))

(enable-console-print!)

(println "MyActivity Hello world!")

(ctmx/defcomponent ^:endpoint subcomponent [req]
  (prn 'subcomponent req)
  [:div {:hx-get "subcomponent" :style "margin: 50px"}
   [:span "Click"]])

(ctmx/defstatic main []
  (let [content (js/document.getElementById "content")]
    (set! (.-innerHTML content)
          (hiccups/html
            [:div {:hx-ext "intercept"}
             (subcomponent nil)]))
    (htmx/process content)))

(intercept/set-responses!
  (ctmx/metas main))

(set! (.-defaultSettleDelay htmx/config) 0)
(set! (.-defaultSwapStyle htmx/config) "outerHTML")
(set! js/window.htmx htmx)
