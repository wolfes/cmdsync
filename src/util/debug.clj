(ns util.debug
  (:gen-class))

; Utility methods for debugging.

(defmacro dbg 
  "Print raw and eval'd expression, returns eval'd expr."
  ([x] 
  `(let [x# ~x] ; ~var gets you the evaled var.
     (println ";>>" '~x "=" x#) ; '~var gets unevaled code expr of var.
     x#)))
