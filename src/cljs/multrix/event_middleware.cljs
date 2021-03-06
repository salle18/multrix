(ns multrix.event-middleware
  "Simple event handler that remaps ws events to multrix game events."
  (:require [multrix.game.events
             :as    events
             :refer [event-namespace]]
            [multrix.util.log :as log]))

(defmulti -event-middleware
  "Multimethod to handle server events"
  :id)

(defn event-middleware [handler event] (-event-middleware event handler))

(defmethod -event-middleware :handshake
  [_ handler]
  (log/->debug! "Handshake"))

(defmethod -event-middleware :ping
  [_ handler]
  (log/->debug! "Ping"))

(defmethod -event-middleware :connected
  [_ handler]
  (handler {:id events/connected}))

(defmethod -event-middleware :disconnected
  [_ handler]
  (handler {:id events/disconnected}))

(defmethod -event-middleware :default
  [{:as event :keys [id]} handler]
  (if (= (namespace id) event-namespace)
    (handler event)
    (log/->debug! "Unknown event: %s" id)))
