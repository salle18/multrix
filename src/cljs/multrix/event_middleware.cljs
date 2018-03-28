(ns multrix.event-middleware
  "Simple event handler that remaps ws events to multrix game events."
  (:require [multrix.game.config :refer [event-namespace]]
            [multrix.util :refer [->output!]]))

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
  (handler {:id :multrix/connected}))

(defmethod -event-middleware :disconnected
  [_ handler]
  (handler {:id :multrix/disconnected}))

(defmethod -event-middleware :default
  [{:as event :keys [id]} handler]
  (if (= (namespace id) event-namespace)
    (handler event)
    (->output! "Unknown event: %s" id)))
