(ns ctmx-electron.ui.new-dialog
  (:require
    [clojure.string :as string]
    ctmx.rt
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx.core :as ctmx]))

;gaia mode
;cheats?
;difficulty: Peaceful, Easy, Normal, Hard
;Survival, Creative, Hardcore

(defn select [label name value values]
  [:div
   [:label.mr-2 label]
   [:select {:name name}
    (for [v values]
      [:option {:value (.toLowerCase v)
                :selected (-> v .toLowerCase (= value))} v])]])

(defn checkbox [label name checked?]
  [:div
   [:label.mr-2 label]
   [:input {:type "checkbox"
            :name name
            :value "true"
            :checked checked?}]])

(defn class-string [& names]
  (->> names
       (filter identity)
       (map name)
       (string/join " ")))

(ctmx/defcomponent modal [req
                          new-game
                          difficulty
                          gameMode
                          ^:boolean cheats
                          ^:boolean gaia
                          game-exists?]
  (list
    [:div#modal-backdrop
     {:class (class-string :modal-backdrop :fade (when game-exists? :show))
      :style "display:block"}]
    [:div#modal
     {:tabindex -1
      :class (class-string :modal :fade (when game-exists? :show))
      :style "display:block"}
     [:div.modal-dialog.modal-dialog-centered
      [:div.modal-content
       [:div.modal-header
        [:h5.modal-title "New Chunkmap"]]
       [:form.modal-body {:hx-post "chunkmaps"}
        [:div
         [:label.mr-2 "Game Name"]
         [:input.mr-2 {:type "text"
                       :name "new-game"
                       :value new-game
                       :required true}]
         (when game-exists?
           [:span.badge.badge-danger "Game exists"])]
        (select
          "Difficulty"
          "difficulty"
          difficulty
          ["Peaceful" "Easy" "Normal" "Hard"])
        (select
          "Game Mode"
          "gameMode"
          gameMode
          ["Creative" "Survival" "Hardcore"])
        (checkbox "Enable Cheats?" "cheats" cheats)
        (checkbox "Gaia Mode" "gaia" gaia)
        [:button.btn.btn-primary.mr-2.mt-2 "Ok"]
        [:button.btn.btn-primary.mr-2.mt-2
         {:hx-get "chunkmaps"
          :hx-vals (util/json {:cancel true})}
         "Cancel"]]]]]))
