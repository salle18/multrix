(ns multrix.util.seq)

(defn in?
  "Returns true if coll contains elem."
  [coll elem]
  (some #(= elem %) coll))

(defn update-values [m f & args]
  "Returns map of the result of applying f to values in m."
  (reduce (fn [r [k v]] (assoc r k (apply f v args))) {} m))

(defn select-keys-with
  "Returns map of the result of applying f only to entries in map whose key is in keys"
  [m f keyseq] (update-values (select-keys m keyseq) f))
