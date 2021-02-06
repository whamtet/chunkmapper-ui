(ns ctmx-electron.util)

(defmacro requires [& strs]
  `(do
     ~@(for [s strs]
         `(def ~(symbol s) (js/require ~s)))))

(defmacro getset [sym]
  `(do
     (defn ~(symbol (str "get-" sym)) [] (deref ~sym))
     (defn ~(symbol (str "set-" sym)) [x#] (reset! ~sym x#))))
