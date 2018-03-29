(ns multrix.game.core
  (:require [multrix.game.handler :refer [event-handler]]
            [multrix.game.emitter :as emmiter]
            [multrix.game.events :as events]
            [multrix.game.config :as config]
            [multrix.game.state :refer [get-client-uids get-clients-state get-client-state]]))

(defn state-emitter-all! [output! client-uid]
  (output! client-uid [events/game-state-all {:data (get-clients-state)}]))

(defn state-emitter! [output! client-uid]
  (output! client-uid [events/game-state {:data (get-client-state client-uid)}]))

(defn start! [input! output!]
  (input! event-handler)
  (emmiter/start! get-client-uids (partial state-emitter-all! output!) config/game-latency-state-all)
  (emmiter/start! get-client-uids (partial state-emitter! output!) config/game-latency-state))
