(ns ctmx-electron.service.minecraft-dir
  (:require
    [clojure.string :as string]
    [ctmx-electron.service.storage :as storage]
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx-electron.util :refer [requires]])
  (:refer-clojure :exclude [exists?]))

(requires "fs" "os")

(def home-dir (.homedir os))

(def default-dir
  (cond
    util/osx? (str home-dir "/Library/Application Support/minecraft")
    util/windows? (str js/process.env.APPDATA "\\.minecraft")
    util/linux? (str home-dir "/.minecraft")))

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
        (.split $ util/file-separator)
        (butlast $)
        (string/join util/file-separator $)
        (storage/set-minecraft-dir $)))

(defn game-dir [f]
  (str (dir) util/file-separator "saves" util/file-separator f))

(defn chunkmapper? [f]
  (.existsSync fs (str (game-dir f) util/file-separator "chunkmapper")))

(defn saves []
  (as-> (str (dir) util/file-separator "saves") $
        (if (.existsSync fs $)
          (.readdirSync fs $ #js {:withFileTypes true})
          ())
        (filter #(.isDirectory %) $)
        (map #(.-name %) $)
        (filter chunkmapper? $)))

(defn delete! [name]
  (.rmdirSync fs
              (game-dir name)
              #js {:recursive true}))

