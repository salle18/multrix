(ns multrix.game.handler
  (:require [multrix.game.events :as events]
            [multrix.game.state :as state]
            [multrix.util.log :as log]))

(defmulti event-handler
  "Multimethod to handle server game events"
  :id)

(defmethod event-handler events/connected
  [_]
  (state/set-connected true))

(defmethod event-handler events/disconnected
  [_]
  (state/set-connected false))

(defmethod event-handler events/game-full
  [_]
  (log/->debug! "Game full"))

(defmethod event-handler events/game-init
  [event]
  (log/->debug! "Init: %s" event))

(defmethod event-handler events/game-state
  [event]
  (log/->debug! "State: %s" event))

(defmethod event-handler events/game-state-all
  [event]
  (log/->debug! "State for all: %s" event))

(defmethod event-handler :default
  [event]
  (log/->debug! "Unhandled game event: %s" event))
