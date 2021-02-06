(ns ctmx-electron.util)

(def osx? (= "darwin" js/process.platform))
(def windows? (= "win32" js/process.platform))
(def linux? (and (not osx?) (not windows?)))

(defn json [s]
  (-> s clj->js js/JSON.stringify))
