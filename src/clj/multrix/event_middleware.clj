(ns multrix.event-middleware
  (:require [multrix.game.events
             :as    events
             :refer [event-namespace]]
            [multrix.util :refer [->output!]]))

(defmulti -event-middleware
  "Multimethod to handle server events"
  :id)

(defn event-middleware [handler event] (-event-middleware event handler))

(defmethod -event-middleware :ping
  [_ handler]
  (->output! "Ping"))

(defmethod -event-middleware :connected
  [{:keys [client-uid send]} handler]
  (handler {:id events/connected :client-uid client-uid :send send}))

(defmethod -event-middleware :disconnected
  [{:keys [client-uid]} handler]
  (handler {:id events/disconnected :client-uid client-uid}))

(defmethod -event-middleware :default
  [{:as event :keys [id]} handler]
  (if (= (namespace id) event-namespace)
    (handler event)
    (->output! "Unknown event: %s" event)))
