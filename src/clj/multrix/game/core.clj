(ns multrix.game.core
  (:require [multrix.game.handler :refer [event-handler]]
            [multrix.game.emitter :as emmiter]
            [multrix.game.events :as events]
            [multrix.game.config :as config]
            [multrix.game.state :refer [game-state]]))

(defn state-emitter-all! [output! uid]
  (output! uid [events/game-state-all {:data @game-state}]))

(defn state-emitter! [output! uid]
  (output! uid [events/game-state {:data (get @game-state uid)}]))

(defn start! [input! output! uids]
  (input! event-handler)
  (emmiter/start! uids (partial state-emitter-all! output!) config/game-latency-state-all)
  (emmiter/start! uids (partial state-emitter! output!) config/game-latency-state))
