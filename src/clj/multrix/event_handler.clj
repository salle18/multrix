(ns multrix.event-handler
  (:require [multrix.util :refer [->output!]]))

(defmulti event-handler
  "Multimethod to handle server events"
  :id)

(defmethod event-handler :ping
  [_]
  (->output! "Ping"))

(defmethod event-handler :connected
  [event]
  (->output! "Connected: %s" event))

(defmethod event-handler :disconnected
  [event]
  (->output! "Disconnected: %s" event))

(defmethod event-handler :default
  [event]
  (->output! "Unhandled server event: %s" event))
