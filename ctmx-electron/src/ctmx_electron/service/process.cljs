(ns ctmx-electron.service.process
  (:require
    [ctmx-electron.service.minecraft-dir :as minecraft-dir]
    [ctmx-electron.service.map :as map]
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx-electron.util :refer [requires]]))

(requires "child_process")

(defn java? []
  (->
    (.spawnSync child_process "java" #js ["-version"])
    .-status
    zero?))

(defn- on-logback [{:keys [evt lat lng lat1 lng1 lat2 lng2]}]
  (case evt
    "steve"
    (js/setSteve lat lng)
    "goto"
    (js/map.flyTo #js [lat1 lng1])
    "region1"
    (map/red-region [lat1 lng1] [lat2 lng2])
    "region2"
    (map/blue-region [lat1 lng1] [lat2 lng2])
    nil))

(defn- logback-json [s]
  (try
    (-> s .trim util/from-json on-logback)
    (catch :default e
      (prn e s))))

(defn log [s]
  (let [s (-> s str .trim)]
    (if (.includes s "logback:")
      (->> (.split s "logback:")
           (filter not-empty)
           (map logback-json)
           dorun)
      (println s))))
(defn err [s]
  (-> s str js/console.error))

(def process)
(def opts (if js/process.env.APP_DEV #js {} #js {:cwd js/process.resourcesPath}))

(defn new-game [game params]
  (let [{:strs [lat lng]} (js->clj js/newLocation)]
    (set! process
          (.spawn child_process
                  "java"
                  (clj->js
                    (concat
                      ["-jar" "chunkmapper-0.2.jar"
                       "name" (minecraft-dir/game-dir game)
                       "cheats" (if (-> params :cheats nil?) "false" "true")]
                      (mapcat
                        #(update % 0 name)
                        (dissoc params :new-game :cheats))
                      (when lat
                        ["lat" lat
                         "lng" lng])))
                  opts))
    (doto process
      (-> .-stdout (.on "data" log))
      (-> .-stderr (.on "data" err)))))

(defn launch [p args]
  (doto
    (.spawn child_process p args opts)
    (-> .-stdout (.on "data" log))
    (-> .-stderr (.on "data" err))))
(set! js/window.l launch)

(defn kill! []
  (when process
    (.kill process)
    (set! process nil)))
