(ns multrix.game.state
  (:require [multrix.game.config :refer [max-number-of-players]]))

(defonce game-state (atom {}))

(def new-game-state {:score 0})

(defn handleUp [client-id] (swap! game-state update-in [client-id :score] inc))

(defn handleRight [client-id]
  (swap! game-state update-in [client-id :score] inc))

(defn handleDown [client-id]
  (swap! game-state update-in [client-id :score] dec))

(defn handleLeft [client-id]
  (swap! game-state update-in [client-id :score] dec))

(defn handleAddClient [client-id send]
  (if (< (count @game-state) max-number-of-players)
    (do (swap! game-state assoc client-id new-game-state)
      (send client-id [:multrix/game-init {:data @game-state}]))
    (send client-id [:multrix/game-full])))

(defn handleRemoveClient [client-id] (swap! game-state dissoc client-id))
