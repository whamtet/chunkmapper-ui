(ns ctmx-electron.ui.new-dialog
  (:require
    [ctmx-electron.util :as util]))

;gaia mode
;cheats?
;difficulty: Peaceful, Easy, Normal, Hard
;Survival, Creative, Hardcore

(defn select [label name values]
  [:div
   [:label.mr-2 label]
   [:select {:name name}
    (for [v values]
      [:option {:value (.toLowerCase v)} v])]])

(defn checkbox [label name checked?]
  [:div
   [:label.mr-2 label]
   [:input {:type "checkbox"
            :name name
            :value "true"
            :checked checked?}]])

(def modal
  (list
    [:div#modal-backdrop.modal-backdrop.fade {:style "display:block"}]
    [:div#modal.modal.fade {:tabindex -1 :style "display:block"}
     [:div.modal-dialog.modal-dialog-centered
      [:div.modal-content
       [:div.modal-header
        [:h5.modal-title "New Chunkmap"]]
       [:form.modal-body {:hx-post "chunkmaps"}
        [:div
        [:label.mr-2 "Game Name"]
        [:input {:type "text" :name "new-game" :required true}]]
        (select
          "Difficulty"
          "difficulty"
          ["Peaceful" "Easy" "Normal" "Hard"])
        (select
          "Game Mode"
          "gameMode"
          ["Creative" "Survival" "Hardcore"])
        (checkbox "Enable Cheats?" "cheats" true)
        (checkbox "Gaia Mode" "gaia" false)
        [:button.btn.btn-primary.mr-2.mt-2 "Ok"]
        [:button.btn.btn-primary.mr-2.mt-2
         {:hx-get "chunkmaps"
          :hx-vals (util/json {:cancel true})}
         "Cancel"]]]]]))
