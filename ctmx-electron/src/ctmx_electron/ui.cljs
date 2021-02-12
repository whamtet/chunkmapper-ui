(ns ctmx-electron.ui
  (:require
    ctmx.rt
    [ctmx-electron.service.map :as map]
    [ctmx-electron.service.process :as process]
    [ctmx-electron.service.minecraft-dir :as minecraft-dir]
    [ctmx-electron.ui.new-dialog :as new-dialog]
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx.core :as ctmx]))

(ctmx/defcomponent ^:endpoint chunkmaps [req
                                         new-game
                                         save
                                         ^boolean cancel
                                         cheats]
  (ctmx/with-req req
    (when cancel
      (set! js/newLocation nil))
    (when delete?
      (if save
        (minecraft-dir/delete! save)
        (do
          (process/kill!)
          (map/clear))))
    (let [saves (minecraft-dir/saves)
          msg (if (empty? saves)
                "Double click map to create a new chunkmap."
                "Double click map to create a new chunkmap, or select one of the existing maps below.")
          game-exists? (some #(= new-game %) saves)
          start-process? (and post? top-level? (not game-exists?))
          cheats (if (and patch? top-level?)
                   true
                   cheats)
          req (update req :params merge {:game-exists? game-exists? :cheats cheats})]
      (when start-process?
        (process/new-game (or new-game save) params))
      (if start-process?
        [:h2.text-center {:id id :hx-target "this"}
         "Building " (or save new-game) "..."
         [:button.btn.btn-primary.ml-2
          {:hx-delete "chunkmaps"} "Cancel"]]
        [:div.p-2 {:id id :hx-target "this" :style "border: 1px solid black"}
         [:h4 "Chunkmaps"]
         [:p msg
          " Click " [:button.leaflet-control-geocoder-icon {:type "button"} " "] " to find a place."]
         [:div#modal-btn.d-none
          {:_ "on htmx:afterOnLoad wait 10ms then add .show to #modal then add .show to #modal-backdrop"
           :hx-patch "chunkmaps"}]
         (when (or (and patch? top-level?) game-exists?)
           (new-dialog/modal req))
         (for [save saves]
           [:div.row
            [:div.col-2
             [:a {:hx-post "chunkmaps"
                  :href "javascript:void(0)"
                  :hx-vals (util/json {:save save})} save]]
            [:div.col-1
             [:a {:hx-delete "chunkmaps"
                  :href "javascript:void(0)"
                  :hx-vals (util/json {:save save})
                  :hx-confirm (str "Delete " save "?")}
              "Delete"]]])]))))

(ctmx/defcomponent ^:endpoint panel [req]
  (ctmx/with-req req
    (when post?
      (-> req :elt minecraft-dir/update-dir!))
    (cond
      (process/java?)
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
      :else
      (do
        (js/alert "Java required.  Please install Java and restart Chunkmapper.")
        [:h3.text-center.my-5
         "Java required.  Please install Java and restart Chunkmapper."]))))
