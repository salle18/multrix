(ns multrix.event-middleware
  "Simple event handler that remaps ws events to multrix game events."
  (:require [multrix.util :refer [->output!]]))

(defmulti -event-middleware
  "Multimethod to handle server events"
  :id)

(defn event-middleware [handler event] (-event-middleware event handler))

(defmethod -event-middleware :handshake
  [_ handler]
  (->output! "Handshake"))

(defmethod -event-middleware :ping
  [_ handler]
  (->output! "Ping"))

(defmethod -event-middleware :connected
  [_ handler]
  (handler [:multrix/connected]))

(defmethod -event-middleware :disconnected
  [_ handler]
  (handler [:multrix/disconnected]))

(defmethod -event-middleware :default
  [event handler]
  (handler event))
