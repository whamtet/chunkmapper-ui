(ns ctmx-electron.ui
  (:require
    ctmx.rt
    [ctmx-electron.service.minecraft-dir :as minecraft-dir]
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx.core :as ctmx]))

(ctmx/defcomponent ^:endpoint panel [req]
  (ctmx/with-req req
    (when post?
      (-> req :elt minecraft-dir/update-dir!))
    [:div.my-2 {:id id :hx-ext "intercept"}
     [:div.row
      [:div.col-1]
      [:div.col-10
       [:h2.text-center "Chunkmapper"]
       (if (minecraft-dir/exists?)
         "todo"
         [:div
          [:div
           [:span.mr-2 "Chunkmapper not found.  Select location manually"]
           [:input {:type "file"
                    :webkitdirectory true
                    :hx-post "panel"
                    :hx-target (hash ".")}]]
          [:div
           "Or install Minecraft and restart Chunkmapper."]])]]]))
