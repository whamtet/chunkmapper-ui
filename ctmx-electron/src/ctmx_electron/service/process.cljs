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
    (js/recenter lat lng)
    "region"
    (js/addRegion #js [lat1 lng1] #js [lat2 lng2])
    nil))

(defn new-game [game]
  (let [{:strs [lat lng]} (js->clj js/newLocation)
        process
        (.spawn child_process
                "java"
                #js ["-jar" "chunkmapper-0.2.jar"
                     "action" "new"
                     "name" (pr-str game)
                     "lat" lat
                     "lng" lng])
        log #(let [s (str %)]
               (when (.startsWith s "logback: ")
                 (-> s (.replace "logback: " "") util/from-json on-logback)))]
    (set! js/window.p process)
    (doto process
      (-> .-stdout (.on "data" log))
      (-> .-stderr (.on "data" #(-> % str js/console.error))))))
