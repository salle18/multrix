(ns multrix.game.core
  (:require [multrix.game.handler :refer [event-handler]]
            [multrix.game.emitter :as emmiter]
            [multrix.game.state :refer [game-state]]))

(defn start! [input! output! uids]
  (input! event-handler)
  (emmiter/start! uids (fn [id] (output! id [:multrix/state {:data @game-state}])) 1000))
