(ns multrix.game.handler
  (:require [multrix.util :refer [->output!]]))

(defmulti event-handler
  "Multimethod to handle server game events"
  :id)

(defmethod event-handler :multrix/connected
  [_]
  (->output! "Game connected"))

(defmethod event-handler :multrix/disconnected
  [_]
  (->output! "Game disconnected"))

(defmethod event-handler :multrix/game-full
  [_]
  (->output! "Game full"))

(defmethod event-handler :multrix/game-init
  [event]
  (->output! "State: %s" event))

(defmethod event-handler :multrix/state
  [event]
  (->output! "State: %s" event))

(defmethod event-handler :default
  [event]
  (->output! "Unhandled game event: %s" event))
