(ns multrix.ws.handler
  (:require
    [multrix.util :refer [->output!]]))

(defmulti -event-msg-handler
  "Multimethod to handle Sente events"
  :id)

(defn event-msg-handler
  "Wraps `-event-msg-handler`"
  [handler event]
  (-event-msg-handler event handler))

(defmethod -event-msg-handler :default
  [{:keys [event ?data ?reply-fn]} handler]
  (handler {:id (:id event) :data ?data :?reply-fn ?reply-fn}))

(defmethod -event-msg-handler :chsk/ws-ping
  [_ handler]
  (handler {:id :ping}))

(defmethod -event-msg-handler :chsk/uidport-open
  [{:keys [client-id]} handler]
  (handler {:id :connected :data client-id}))

(defmethod -event-msg-handler :chsk/uidport-close
  [{:keys [client-id]} handler]
  (handler {:id :disconnected :data client-id}))
