(ns multrix.game.controls)

(def controls
  {:up    "ArrowUp"
   :right "ArrowRight"
   :down  "ArrowDown"
   :left  "ArrowLeft"})

(defn addEventListener [type listener]
  (js/document.addEventListener type listener))

(def addKeydownListener (partial addEventListener "keydown"))

(defn addKeyListener [key listener]
  (addKeydownListener (fn [event] (if (= (.-key event) key) (listener event)))))

(defn controlHandler [id listener]
  (fn [event] (listener [id])))

(defn addKeyControl [listener key id]
  (addKeyListener key (controlHandler id listener)))

(defn init! [output!]
  (let [addControl (partial addKeyControl output!)]
    (addControl (:up controls) :multrix/up)
    (addControl (:right controls) :multrix/right)
    (addControl (:down controls) :multrix/down)
    (addControl (:left controls) :multrix/left)))
