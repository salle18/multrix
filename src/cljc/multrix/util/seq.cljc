(ns multrix.util.seq)

(defn in?
  "true if collection contains element"
  [collection element]
  (some #(= element %) collection))
