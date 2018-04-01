(ns multrix.game.state
  (:require [reagent.core :as r]))

(defonce game-state$ (r/atom {:connected false :client-uid nil :clients {}}))

(defn set-client-uid [client-uid]
  (swap! game-state$ assoc :client-uid client-uid))

(defn set-connected [connected] (swap! game-state$ assoc :connected connected))
