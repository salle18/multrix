(ns multrix.game.state
  (:require [multrix.game.config :refer [max-number-of-clients court-width court-height]]
            [multrix.util.seq :as seq]
            [multrix.game.blocks :as blocks]
            [multrix.game.fields :as fields]))

(def new-game-state
  {:client-uids   (vec (repeat max-number-of-clients nil))
   :client-states {}})

(defonce game-state$ (atom new-game-state))

(def new-game-board
  (vec (repeat court-height (vec (repeat court-width fields/empty-field)))))

(def new-client-state
  {:score      0
   :board      new-game-board
   :bag        blocks/tetrominos-bag
   :block      nil
   :next-block nil})

(defn init-client-state [_]
  (let [{:as client-state :keys [bag]} new-client-state]
    (assoc client-state :bag (next (next bag)) :block (first bag) :next-block (second bag))))

(defn rotate [client-uid] ())

(defn move-right [client-uid] ())

(defn move-down [client-uid]
  (multrix.util.log/->debug! "MOVE DOWN: %s" client-uid))

(defn move-left [client-uid] ())

(defn speed-down [client-uid] ())

(defn client-uids []
  (let [{:keys [client-uids]} @game-state$] (filter some? client-uids)))

(defn game-full? [] (= max-number-of-clients (count (client-uids))))

(defn game-empty? [] (zero? (count (client-uids))))

(defn client-uid? [client-uid]
  (let [{:keys [client-uids]} @game-state$] (seq/in? client-uids client-uid)))

(defn pack-block [{:keys [blocks]}] (first blocks))

(defn pack-client-state [client-state]
  (let [packed-blocks (seq/select-keys-with client-state pack-block [:block :next-block])]
    (apply assoc (select-keys client-state [:score :board]) (mapcat seq packed-blocks))))

(defn clients-state []
  (let [{:keys [client-states]} @game-state$]
    (map #(pack-client-state (get client-states %)) (client-uids))))

(defn client-state [client-uid]
  (let [{:keys [client-states]} @game-state$
        client-state            (get client-states client-uid)]
    (pack-client-state client-state)))

(defn- add-client-state [{:keys [client-uids client-states]} client-uid]
  (let [empty-index (.indexOf client-uids nil)]
    {:client-uids   (assoc-in client-uids [empty-index] client-uid)
     :client-states (update-in client-states [client-uid] init-client-state)}))

(defn add-client [client-uid]
  (if (game-full?)
    :game-full
    (do (swap! game-state$ add-client-state client-uid) :connected)))

(defn- remove-client-state [{:keys [client-uids client-states]} client-uid]
  (let [client-uid-index (.indexOf client-uids client-uid)]
    {:client-uids   (assoc-in client-uids [client-uid-index] nil)
     :client-states (dissoc client-states client-uid)}))

(defn remove-client [client-uid]
  (if (client-uid? client-uid)
    (swap! game-state$ remove-client-state client-uid)))
