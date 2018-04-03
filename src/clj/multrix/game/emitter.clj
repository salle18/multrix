(ns multrix.game.emitter
  (:require [clojure.core.async
             :as    async
             :refer [close! go-loop <! timeout]]
            [multrix.game.events :as events]
            [multrix.game.config :as config]
            [multrix.game.state :as state]))

(defonce output$ (atom nil))

(defonce emit-channels$ (atom {}))

(defn output! [client-uid event]
  (if-let [output! @output$] (output! (name client-uid) event)))

(defn emit-with-timeout! [emitter! test? timeout]
  (go-loop []
           (<! (async/timeout timeout))
           (when (test?) (emitter!) (recur))))

(defn init! [output!] (reset! output$ output!))

(defn emit-state! []
  (let [client-uids (state/client-uids)]
    (run! #(output! % [events/game-state-all {:data (state/clients-state)}]) client-uids)))

(defn emit-state-client! [client-uid]
  (output! client-uid [events/game-state {:data (state/client-state client-uid)}]))

(defn emit-game-full! [client-uid]
  (output! client-uid [events/game-full]))

(def state-channel-key :multrix/state-channel)

(defn create-emit-channel! [channel-key channel]
  (swap! emit-channels$ assoc channel-key channel))

(defn create-emit-state-channel! []
  (let [state-emit-channel (emit-with-timeout! emit-state! (complement state/game-empty?) config/game-latency-state-all)]
    (create-emit-channel! state-channel-key state-emit-channel)))

(defn start-client-emit! [client-uid]
  (state/move-down client-uid) (emit-state-client! client-uid))

(defn create-emit-state-client-channel! [client-uid]
  (let [state-client-emit-channel (emit-with-timeout! #(start-client-emit! client-uid)
                                                      #(and (state/client-uid? client-uid) (state/client-playing? client-uid)) config/game-latency-state)]
    (create-emit-channel! client-uid state-client-emit-channel)))

(defn destroy-emit-channel! [channel-key]
  (if-let [emit-channel (get @emit-channels$ channel-key)]
    (do
      (close! emit-channel)
      (swap! emit-channels$ dissoc channel-key))))

(defn connect-client! [client-uid]
  (if-not (state-channel-key @emit-channels$) (create-emit-state-channel!))
  (create-emit-state-client-channel! client-uid)
  (output! client-uid [events/game-init {:data (state/clients-state)}]))

(defn disconnect-client! [client-uid]
  (destroy-emit-channel! client-uid)
  (if (state/game-empty?) (destroy-emit-channel! state-channel-key)))
