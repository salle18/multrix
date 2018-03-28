(ns multrix.game.state)

(defonce game-state (atom 100))

(defn handleUp [client-id] (swap! game-state inc))

(defn handleRight [client-id] (swap! game-state inc))

(defn handleDown [client-id] (swap! game-state dec))

(defn handleLeft [client-id] (swap! game-state dec))
