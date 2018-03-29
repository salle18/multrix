(ns multrix.game.handler
  (:require [multrix.game.events :as events]
            [multrix.game.state :as state]
            [multrix.util :refer [->output!]]))

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
  (->output! "Game full"))

(defmethod event-handler events/game-init
  [event]
  (->output! "Init: %s" event))

(defmethod event-handler events/game-state
  [event]
  (->output! "State: %s" event))

(defmethod event-handler events/game-state-all
  [event]
  (->output! "State for all: %s" event))

(defmethod event-handler :default
  [event]
  (->output! "Unhandled game event: %s" event))
