(ns ctmx-electron.ui
  (:require
    ctmx.rt
    [ctmx-electron.service.process :as process]
    [ctmx-electron.service.minecraft-dir :as minecraft-dir]
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx.core :as ctmx]))

(def modal
  (list
    [:div#modal-backdrop.modal-backdrop.fade {:style "display:block"}]
    [:div#modal.modal.fade {:tabindex -1 :style "display:block"}
     [:div.modal-dialog.modal-dialog-centered
      [:div.modal-content
       [:div.modal-header
        [:h5.modal-title "New Chunkmap"]]
       [:form.modal-body {:hx-post "chunkmaps"}
        [:label.mr-2 "Game Name"]
        [:input {:type "text" :name "new-game" :required true}][:br]
        [:button.btn.btn-primary.mr-2.mt-2 "Ok"]
        [:button.btn.btn-primary.mr-2.mt-2
         {:hx-get "chunkmaps"}
         "Cancel"]]]]]))

(ctmx/defcomponent ^:endpoint chunkmaps [req new-game]
  (ctmx/with-req req
    (let [saves (minecraft-dir/saves)
          msg (if (empty? saves)
                "Double click map to create a new chunkmap"
                "Double click map to create a new chunkmap, or select one of the existing maps below.")]
      (when (and post? top-level?)
        (process/new-game new-game))
      [:div.p-2 {:id id :hx-target "this" :style "border: 1px solid black"}
       [:h4 "Chunkmaps"]
       [:p msg
        " Click " [:button.leaflet-control-geocoder-icon {:type "button"} " "] " to find a place."]
       [:div#modal-btn.d-none
        {:_ "on htmx:afterOnLoad wait 10ms then add .show to #modal then add .show to #modal-backdrop"
         :hx-patch "chunkmaps"}]
       (when (and top-level? patch?)
         modal)
       (for [save saves]
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
            "Delete"]]])])))

(ctmx/defcomponent ^:endpoint panel [req]
  (ctmx/with-req req
    (when post?
      (-> req :elt minecraft-dir/update-dir!))
    (when patch?
      (-> req :params :save prn))
    (if (process/java?)
      [:div.my-2 {:id id :hx-ext "intercept" :hx-target "this"}
       [:div.row
        [:div.col-1]
        [:div.col-10
         [:h2.text-center "Chunkmapper"]
         (if (minecraft-dir/exists?)
           (chunkmaps req)
           [:div
            [:div
             [:span.mr-2 "Minecraft not found.  Select location manually"]
             [:input {:type "file"
                      :webkitdirectory true
                      :hx-post "panel"
                      :hx-target (hash ".")}]]
            [:div
             "Or install Minecraft and restart Chunkmapper."]])]]]
      (js/alert "Java required.  Please install Java and restart Chunkmapper."))))
