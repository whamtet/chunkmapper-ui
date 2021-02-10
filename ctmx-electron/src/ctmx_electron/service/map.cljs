(ns ctmx-electron.service.map)

(def red-regions (atom {}))
(def blue-regions (atom ()))

(defn red-region [p1 p2]
  (let [region (js/L.rectangle (clj->js [p1 p2]) #js {:color "red" :weight 0.5})]
    (.addTo region js/map)
    (swap! red-regions assoc [p1 p2] region)))

(defn blue-region [p1 p2]
  (let [region (js/L.rectangle (clj->js [p1 p2]) #js {:color "blue" :weight 0.5})]
    (when-let [red-region (@red-regions [p1 p2])]
      (.remove red-region)
      (swap! red-regions dissoc [p1 p2]))
    (.addTo region js/map)
    (swap! blue-regions conj region)))

(defn clear []
  (doseq [region (concat (vals @red-regions) @blue-regions)]
    (.remove region))
  (reset! red-regions {})
  (reset! blue-regions ())
  (set! js/newLocation nil)
  (when js/steve
    (js/steve.remove)
    (set! js/steve nil)
    (set! js/added false)))
