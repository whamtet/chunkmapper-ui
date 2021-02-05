(ns ctmx-electron.service.minecraft-dir
  (:require
    [clojure.string :as string]
    [ctmx-electron.util :as util])
  (:require-macros
    [ctmx-electron.util :refer [requires]]))

(requires "fs" "os")

(def home-dir (.homedir os))

(def dir
  (str home-dir
       (cond
         util/osx? "/Library/Application Support/minecraft"
         util/windows? "/AppData/.minecraft"
         util/linux? "/.minecraft")))

(defn exists? []
  (.existsSync fs dir))

(defn update-dir! [elt]
  (as-> elt $
        (.-files $)
        (aget $ 0)
        (.-path $)
        (.split $ "/")
        (butlast $)
        (string/join "/" $)
        (set! dir $)))


