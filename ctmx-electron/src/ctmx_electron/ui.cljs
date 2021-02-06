(ns ctmx-electron.ui
  (:require
    ctmx.rt
    [ctmx-electron.service.minecraft-dir :as minecraft-dir]
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx.core :as ctmx]))

(defn chunkmaps []
  [:div.p-2 {:style "border: 1px solid black"}
   [:h4 "Chunkmaps"]
   (for [save (minecraft-dir/saves)]
     [:div.row
      [:div.col-2
       [:a {:hx-patch "panel"
            :href "javascript:void(0)"
            :hx-vals (util/json {:save save})} save]]
      [:div.col-1
       [:a {:hx-delete "panel"
            :href "javascript:void(0)"
            :hx-vals (util/json {:save save})
            :hx-confirm (str "Delete " save "?")}
        "Delete"]]])])


(ctmx/defcomponent ^:endpoint panel [req]
  (ctmx/with-req req
    (when post?
      (-> req :elt minecraft-dir/update-dir!))
    (when patch?
      (-> req :params :save prn))
    [:div.my-2 {:id id :hx-ext "intercept" :hx-target "this"}
     [:div.row
      [:div.col-1]
      [:div.col-10
       [:h2.text-center "Chunkmapper"]
       (if (minecraft-dir/exists?)
         (chunkmaps)
         [:div
          [:div
           [:span.mr-2 "Minecraft not found.  Select location manually"]
           [:input {:type "file"
                    :webkitdirectory true
                    :hx-post "panel"
                    :hx-target (hash ".")}]]
          [:div
           "Or install Minecraft and restart Chunkmapper."]])]]]))
