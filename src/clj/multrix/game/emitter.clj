(ns multrix.game.emitter
  (:require [clojure.core.async
             :as    async
             :refer [close! go-loop <! timeout]]
            [multrix.game.events :as events]
            [multrix.game.config :as config]
            [multrix.game.state :refer [get-client-uids get-clients-state get-client-state]]))

(defonce output (atom nil))

(defn output! [client-uid event]
  (if-let [output-fn! @output] (output-fn! client-uid event)))

(defn emit-with-timeout! [emitter! timeout]
  (go-loop []
           (<! (async/timeout timeout))
           (emitter!)
           (recur)))

(defn emit-state-all! [client-uid]
  (output! client-uid [events/game-state-all {:data (get-clients-state)}]))

(defn emit-state-client! [client-uid]
  (output! client-uid [events/game-state {:data (get-client-state client-uid)}]))

(defn emit-game-full! [client-uid]
  (output! client-uid [events/game-full]))

(defn emit-init-game! [client-uid]
  (output! client-uid [events/game-init {:data (get-clients-state)}]))

(defn start! [output-fn!]
  (reset! output output-fn!)
  (emit-with-timeout! #(run! emit-state-all! (get-client-uids)) config/game-latency-state-all))
