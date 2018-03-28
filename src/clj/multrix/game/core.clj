(ns multrix.game.core
  (:require [multrix.game.handler :refer [event-handler]]
            [multrix.game.emitter :as emmiter]
            [multrix.game.state :refer [game-state]]))

(defn state-emitter-all! [output! uid]
  (output! uid [:multrix/state-all {:data @game-state}]))

(defn state-emitter! [output! uid]
  (output! uid [:multrix/state {:data (get @game-state uid)}]))

(defn start! [input! output! uids]
  (input! event-handler)
  (emmiter/start! uids (partial state-emitter-all! output!) 2500)
  (emmiter/start! uids (partial state-emitter! output!) 1000))
