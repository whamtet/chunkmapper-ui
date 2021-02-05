(ns ctmx-electron.util)

(defmacro requires [& strs]
  `(do
     ~@(for [s strs]
         `(def ~(symbol s) (js/require ~s)))))
