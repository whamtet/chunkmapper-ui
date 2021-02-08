(ns ctmx-electron.service.process
  (:require
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx-electron.util :refer [requires]]))

(requires "child_process")

(defn java? []
  (->
    (.spawnSync child_process "java" #js ["-version"])
    .-status
    zero?))

(defn on-logback [{:keys [evt lat lng lat1 lng1 lat2 lng2]}]
  (case evt
    "steve"
    (js/setSteve lat lng)
    "region"
    (js/addRegion #js [lat1 lng1] #js [lat2 lng2])
    nil))

(def process)

(defn new-game [game]
  (let [{:strs [lat lng]} (js->clj js/newLocation)
        log #(let [s (str %)]
               (if (.startsWith s "logback: ")
                 (-> s (.replace "logback: " "") util/from-json on-logback)
                 (prn s)))]
    (set! process
          (.spawn child_process
                  "java"
                  #js ["-jar" "chunkmapper-0.2.jar"
                       "action" "new"
                       "name" game
                       "lat" lat
                       "lng" lng]))
    (doto process
      (-> .-stdout (.on "data" log))
      (-> .-stderr (.on "data" #(-> % str js/console.error))))))
