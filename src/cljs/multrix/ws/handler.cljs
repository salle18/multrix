(ns multrix.ws.handler)

(defmulti -event-msg-handler
  "Multimethod to handle Sente events"
  :id)

(defn event-msg-handler
  "Wraps `-event-msg-handler`"
  [handler event]
  (-event-msg-handler event handler))

(defmethod -event-msg-handler :default
  [{:keys [?data]} handler]
  (handler {:id :unknown :data ?data}))

(defmethod -event-msg-handler :chsk/handshake
  [{:keys [?data]} handler]
  (handler {:id :handshake}))

(defmethod -event-msg-handler :chsk/state
  [{:keys [?data]} handler]
  (let [[_ new-state-map] ?data]
    (if (:open? new-state-map)
      (handler {:id :connected})
      (handler {:id :disconnected}))))

(defmethod -event-msg-handler :chsk/recv
  [{:keys [?data]} handler]
  (let [id (first ?data)]
    (if (= id :chsk/ws-ping)
      (handler {:id :ping})
      (handler {:id id :data ?data}))))
