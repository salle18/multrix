(ns multrix.game.listeners
  (:require [multrix.game.keys :as keys]
            [multrix.game.events :as events]))

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
    (addControl keys/arrow-up events/rotate)
    (addControl keys/arrow-right events/move-right)
    (addControl keys/arrow-down events/move-down)
    (addControl keys/arrow-left events/move-left)
    (addControl keys/space events/speed-down)))
