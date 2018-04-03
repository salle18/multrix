(ns multrix.game.state
  (:require [multrix.game.config :refer [max-number-of-clients board-width board-height]]
            [multrix.util.seq :as seq]
            [multrix.game.blocks :as blocks]
            [multrix.game.status :as status]
            [multrix.game.direction :as direction]
            [multrix.util.log :as log]
            [multrix.game.fields :as fields]))

(def new-game-state
  {:client-uids   (vec (repeat max-number-of-clients nil))
   :client-states {}})

(defonce game-state$ (atom new-game-state))

(def new-game-board
  (vec (repeat board-height (vec (repeat board-width fields/empty-field)))))

(defn new-bag []
  (vec (shuffle (flatten (map #(repeat 4 %) blocks/tetrominos)))))

(def new-client-state
  {:player-status status/playing
   :board         new-game-board
   :bag           (new-bag)
   :block         nil
   :next-block    nil})

(defn init-block [block]
  {:type      block
   :direction direction/up
   :x         (int (/ board-width 2))
   :y         0})

(defn init-client-state [_]
  (let [{:as client-state :keys [bag]} new-client-state]
    (assoc client-state :bag (next (next bag)) :block (init-block (first bag)) :next-block (second bag))))

(defn update-in-client-state [client-uid f]
  (swap! game-state$ update-in [:client-states client-uid]
         (fn [{:as client-state :keys [player-status]}]
           (if (= player-status status/playing) (f client-state) client-state))))

(defn map-block [f {:keys [type direction x y]}]
  (let [position (get-in type [:positions direction])]
    (filter some?
            (map-indexed
             (fn [index bit]
               (let [dx     (mod index 4)
                     dy     (int (/ index 4))
                     field? (not (zero? (bit-and bit position)))]
                 (if field? (f (+ x dx) (+ y dy)))))
             blocks/bit-positions))))

(defn empty-field? [board x y] (= (get-in board [y x]) fields/empty-field))

(defn allowed-block? [board block]
  (every? true?
          (map-block
           (fn [x y] (and (<= 0 x board-width) (<= y board-height) (empty-field? board x y)))
           block)))

(defn merge-block-board [block board]
  (let [indices (map-block (fn [x, y] [y x]) block)]
    (reduce #(assoc-in %1 %2 fields/full-field) board indices)))

(defn update-next-block [{:as client-state :keys [board bag next-block block]}]
  (let [new-block (init-block next-block)]
    (if (allowed-block? board new-block)
      (-> client-state (assoc :block new-block)
          (assoc :next-block (first bag))
          (assoc :bag (or (next bag) (new-bag)))
          (assoc :board (merge-block-board block board)))
      (-> client-state (assoc :player-status status/lost)
          (assoc :board (merge-block-board new-block board))))))

(defn move [client-uid move-direction]
  (let [client-state (get-in @game-state$ [:client-states client-uid])
        block        (:block client-state)
        board        (:board client-state)]
    (condp = move-direction
      direction/down (if (allowed-block? board (update block :y inc))
                       (update-in-client-state client-uid (fn [client-state] (update-in client-state [:block :y] inc)))
                       (update-in-client-state client-uid update-next-block))
      direction/left
      (if (allowed-block? board (update block :x dec))
        (update-in-client-state client-uid (fn [client-state] (update-in client-state [:block :x] dec))))
      direction/right
      (if (allowed-block? board (update block :x inc))
        (update-in-client-state client-uid (fn [client-state] (update-in client-state [:block :x] inc)))))))

(defn next-direction [direction] (mod (inc direction) 4))

(defn rotate [client-uid]
  (let [client-state (get-in @game-state$ [:client-states client-uid])
        block        (:block client-state)
        board        (:board client-state)]
    (if (allowed-block? board (update block :direction next-direction))
      (update-in-client-state client-uid
                              (fn [client-state] (update-in client-state [:block :direction] next-direction))))))

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

(defn pack-clients-state [client-state]
  (if client-state (select-keys client-state [:board])))

(defn clients-state []
  (let [{:keys [client-uids client-states]} @game-state$]
    (map #(pack-clients-state (get client-states %)) client-uids)))

(defn client-state [client-uid]
  (let [{:keys [client-states]}                        @game-state$
        {:keys [board block player-status]}            (get client-states client-uid)]
    {:board         (merge-block-board block board)
     :player-status player-status}))

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
