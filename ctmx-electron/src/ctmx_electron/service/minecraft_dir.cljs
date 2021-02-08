(ns ctmx-electron.service.minecraft-dir
  (:require
    [clojure.string :as string]
    [ctmx-electron.service.storage :as storage]
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx-electron.util :refer [requires]]))

(requires "fs" "os")

(def home-dir (.homedir os))

(def default-dir
  (str home-dir
       (cond
         util/osx? "/Library/Application Support/minecraft"
         util/windows? "/AppData/.minecraft"
         util/linux? "/.minecraft")))

(defn dir []
  (if (.existsSync fs default-dir)
    default-dir
    (storage/get-minecraft-dir)))

(defn exists? []
  (or
    (.existsSync fs default-dir)
    (if-let [dir (storage/get-minecraft-dir)]
      (.existsSync fs dir)
      false)))

(defn update-dir! [elt]
  (as-> elt $
        (.-files $)
        (aget $ 0)
        (.-path $)
        (.split $ "/")
        (butlast $)
        (string/join "/" $)
        (storage/set-minecraft-dir $)))

(defn saves []
  (as-> (str (dir) "/saves") $
        (.readdirSync fs $ #js {:withFileTypes true})
        (filter #(.isDirectory %) $)
        (map #(.-name %) $)))

(defn delete! [name]
  (.rmdirSync fs
    (str (dir) "/saves/" name)
    #js {:recursive true}))

