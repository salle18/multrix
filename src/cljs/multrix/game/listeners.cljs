(ns multrix.game.listeners
  (:require [multrix.game.controls :as controls]
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
    (addControl controls/arrow-up events/rotate)
    (addControl controls/arrow-right events/move-right)
    (addControl controls/arrow-down events/move-down)
    (addControl controls/arrow-left events/move-left)
    (addControl controls/space events/speed-down)))
