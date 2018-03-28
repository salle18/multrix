(ns multrix.game.handler
  (:require [multrix.util :refer [->output!]]))

(defmulti event-handler
  "Multimethod to handle server game events"
  first)

(defmethod event-handler :multrix/connected
  [_]
  (->output! "Game connected"))

(defmethod event-handler :multrix/disconnected
  [_]
  (->output! "Game disconnected"))

(defmethod event-handler :default
  [event]
  (->output! "Unhandled game event: %s" event))
