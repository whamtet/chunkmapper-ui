(ns ctmx-electron.service.storage
  (:require
    [alandipert.storage-atom :refer [local-storage]])
  (:require-macros
    [ctmx-electron.util :refer [getset]]))

(def minecraft-dir (local-storage (atom nil) :minecraft-dir))
(getset minecraft-dir)
