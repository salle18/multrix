(ns multrix.game.state
  (:require [multrix.game.config :refer [max-number-of-players court-width court-height]]
            [multrix.game.fields :as fields]
            [multrix.game.events :as events]))

(defonce game-state (atom {}))

(def new-game-board
  (into [] (repeat court-height (into [] (repeat court-width fields/empty-field)))))

(def new-game-state {:score 0 :board new-game-board})

(defn rotate [client-id] (swap! game-state update-in [client-id :score] inc))

(defn move-right [client-id]
  (swap! game-state update-in [client-id :score] inc))

(defn move-down [client-id]
  (swap! game-state update-in [client-id :score] dec))

(defn move-left [client-id]
  (swap! game-state update-in [client-id :score] dec))

(defn speed-down [client-id]
  (swap! game-state update-in [client-id :score] dec))

(defn add-client [client-id send]
  (if (< (count @game-state) max-number-of-players)
    (do (swap! game-state assoc client-id new-game-state)
      (send client-id [events/game-init {:data @game-state}]))
    (send client-id [events/game-full])))

(defn remove-client [client-id] (swap! game-state dissoc client-id))
