(ns multrix.util.seq)

(defn in?
  "true if coll contains elem"
  [coll elem]
  (some #(= elem %) coll))
