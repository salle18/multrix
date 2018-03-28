(ns multrix.game.core
  (:require [multrix.game.handler :refer [event-handler]]
            [multrix.game.emitter :as emmiter]
            [multrix.game.state :refer [game-state]]))

(defn state-emitter! [output! id]
  (output! id [:multrix/state {:data @game-state}]))

(defn start! [input! output! uids]
  (input! event-handler)
  (emmiter/start! uids (partial state-emitter! output!) 1000))
