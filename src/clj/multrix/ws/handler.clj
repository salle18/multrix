(ns multrix.ws.handler)

(defmulti -event-handler
  "Multimethod to handle Sente events"
  :id)

(defn event-handler
  "Wraps `-event-handler`"
  [handler event]
  (-event-handler event handler))

(defmethod -event-handler :default
  [{:keys [id ?data ?reply-fn client-id]} handler]
  (handler
   {:id id :data ?data :reply ?reply-fn :client-uid client-id}))

(defmethod -event-handler :chsk/ws-ping
  [_ handler]
  (handler {:id :ping}))

(defmethod -event-handler :chsk/uidport-open
  [{:keys [client-id]} handler]
  (handler {:id :connected :client-uid client-id}))

(defmethod -event-handler :chsk/uidport-close
  [{:keys [client-id]} handler]
  (handler {:id :disconnected :client-uid client-id}))
