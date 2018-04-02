(ns multrix.game.state
  (:require [multrix.game.config :refer [max-number-of-clients court-width court-height]]
            [multrix.util.seq :as seq]
            [multrix.game.blocks :as blocks]
            [multrix.game.direction :as direction]
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

(defn init-block [block]
  {:type      block
   :direction direction/up
   :x         (int (/ court-width 2))
   :y         0})

(defn init-client-state [_]
  (let [{:as client-state :keys [bag]} new-client-state]
    (assoc client-state :bag (next (next bag)) :block (init-block (first bag)) :next-block (second bag))))

(defn update-in-client-state [client-uid f]
  (swap! game-state$ update-in [:client-states client-uid] f))

(defn map-block [f {:keys [type direction x y]}]
  (let [position (get-in type [:positions direction])]
    (map-indexed
     (fn [index bit]
       (let [dx     (mod index 4)
             dy     (int (/ index 4))
             field? (not (zero? (bit-and bit position)))]
         (if field? (f (+ x dx) (+ y dy)) true)))
     blocks/tetronimos-bit-positions)))

(defn empty-field? [board x y] (= (get-in board [y x]) fields/empty-field))

(defn allowed-block? [board block]
  (every? true?
          (map-block
           (fn [x y] (and (<= 0 x court-width) (<= y court-height) (empty-field? board x y)))
           block)))

(defn move [client-uid move-direction]
  (let [client-state (get-in @game-state$ [:client-states client-uid])
        block        (:block client-state)
        board        (:board client-state)]
    (condp = move-direction
      direction/down (if (allowed-block? board (update block :y inc))
                       (update-in-client-state client-uid (fn [client-state] (update-in client-state [:block :y] inc))))
      direction/left
      (if (allowed-block? board (update block :x dec))
        (update-in-client-state client-uid (fn [client-state] (update-in client-state [:block :x] dec))))
      direction/right
      (if (allowed-block? board (update block :x inc))
        (update-in-client-state client-uid (fn [client-state] (update-in client-state [:block :x] inc)))))))

(defn rotate [client-uid] ())

(defn move-right [client-uid]
  (move client-uid direction/right))

(defn move-down [client-uid]
  (move client-uid direction/down))

(defn move-left [client-uid] (move client-uid direction/left))

(defn speed-down [client-uid] ())

(defn client-uids []
  (let [{:keys [client-uids]} @game-state$] (filter some? client-uids)))

(defn game-full? [] (= max-number-of-clients (count (client-uids))))

(defn game-empty? [] (zero? (count (client-uids))))

(defn client-uid? [client-uid]
  (let [{:keys [client-uids]} @game-state$] (seq/in? client-uids client-uid)))

(defn pack-client-state [client-state]
  (select-keys client-state [:score :board :block :next-block]))

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
