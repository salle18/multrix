(ns multrix.game.state
  (:require [multrix.game.config :refer [max-number-of-clients court-width court-height]]
            [multrix.game.fields :as fields]
            [multrix.game.events :as events]))

(def new-game-state {:client-uids (into [] (repeat max-number-of-clients nil)) :client-states {}})

(defonce game-state (atom new-game-state))

(def new-game-board
  (into [] (repeat court-height (into [] (repeat court-width fields/empty-field)))))

(def new-client-state {:score 0 :board new-game-board})

(defn rotate [client-uid] ())

(defn move-right [client-uid] ())

(defn move-down [client-uid] ())

(defn move-left [client-uid] ())

(defn speed-down [client-uid] ())

(defn has-empty-slots? [] (some nil? (:client-uids @game-state)))

(defn add-client-state [{:keys [client-uids client-states]} client-uid]
  (let [empty-index (.indexOf client-uids nil)]
    {:client-uids    (assoc-in client-uids [empty-index] client-uid)
     :client-states (assoc-in client-states [client-uid] new-client-state)}))

(defn add-client [client-uid send]
  (if (has-empty-slots?)
    (do (swap! game-state add-client-state client-uid)
      (send client-uid [events/game-init {:data @game-state}]))
    (send client-uid [events/game-full])))

(defn remove-client-state [{:keys [client-uids client-states]} client-uid]
  (let [client-uid-index (.indexOf client-uids client-uid)]
    {:client-uids    (assoc-in client-uids [client-uid-index] nil)
     :client-states (dissoc client-states client-uid)}))

(defn remove-client [client-uid]
  (swap! game-state remove-client-state client-uid))

(defn get-client-uids [] (let [{:keys [client-uids]} @game-state] client-uids))

(defn get-clients-state [] (let [{:keys [client-uids client-states]} @game-state] (map #(get client-states %) client-uids)))

(defn get-client-state [client-uid] (let [{:keys [client-states]} @game-state] (get client-states client-uid)))
