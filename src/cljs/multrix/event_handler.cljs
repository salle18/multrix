(ns multrix.event-handler
  (:require [multrix.util :refer [->output!]]))

(defmulti event-handler
  "Multimethod to handle server events"
  :id)

(defmethod event-handler :handshake
  [_]
  (->output! "Handshake"))

(defmethod event-handler :ping
  [_]
  (->output! "Ping:"))

(defmethod event-handler :connected
  [_]
  (->output! "Connected"))

(defmethod event-handler :disconnected
  [_]
  (->output! "Disconnected"))

(defmethod event-handler :default
  [event]
  (->output! "Unhandled server event: %s" event))
