(ns multrix.ws.handler)

(defmulti -event-handler
  "Multimethod to handle Sente events"
  :id)

(defn event-handler
  "Wraps `-event-handler`"
  [handler event]
  (-event-handler event handler))

(defmethod -event-handler :default
  [{:keys [id ?data ?reply-fn client-uid send-fn]} handler]
  (handler
   {:id id :data ?data :reply ?reply-fn :send send-fn :client-uid client-uid}))

(defmethod -event-handler :chsk/ws-ping
  [_ handler]
  (handler {:id :ping}))

(defmethod -event-handler :chsk/uidport-open
  [{:keys [client-id send-fn]} handler]
  (handler {:id :connected :client-uid client-id :send send-fn}))

(defmethod -event-handler :chsk/uidport-close
  [{:keys [client-id]} handler]
  (handler {:id :disconnected :client-uid client-id}))
