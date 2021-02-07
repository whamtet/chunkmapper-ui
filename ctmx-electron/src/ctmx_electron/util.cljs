(ns ctmx-electron.util
  (:require
    [clojure.walk :as walk]))

(def osx? (= "darwin" js/process.platform))
(def windows? (= "win32" js/process.platform))
(def linux? (and (not osx?) (not windows?)))

(defn json [s]
  (-> s clj->js js/JSON.stringify))

(defn from-json [s]
  (-> s js/JSON.parse js->clj walk/keywordize-keys))
