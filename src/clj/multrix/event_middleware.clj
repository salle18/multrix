(ns multrix.event-middleware
  (:require [multrix.game.events
             :as    events
             :refer [event-namespace]]
            [multrix.util.log :as log]))

(defmulti -event-middleware
  "Multimethod to handle server events"
  :id)

(defn event-middleware [handler event] (-event-middleware event handler))

(defn pack-event [{:as event :keys [client-uid]} & rest]
  (apply assoc event :client-uid (keyword client-uid) rest))

(defmethod -event-middleware :ping
  [_ handler]
  (log/->debug! "Ping"))

(defmethod -event-middleware :connected
  [event handler]
  (handler (pack-event event :id events/connected)))

(defmethod -event-middleware :disconnected
  [event handler]
  (handler (pack-event event :id events/disconnected)))

(defmethod -event-middleware :default
  [{:as event :keys [id]} handler]
  (if (= (namespace id) event-namespace)
    (handler (pack-event event))
    (log/->debug! "Unknown event: %s" event)))
