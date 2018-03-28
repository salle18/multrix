(ns multrix.ws.handler)

(defmulti -event-handler
  "Multimethod to handle Sente events"
  :id)

(defn event-handler
  "Wraps `-event-handler`"
  [handler event]
  (-event-handler event handler))

(defmethod -event-handler :default
  [{:keys [?data]} handler]
  (handler {:id :unknown :data ?data}))

(defmethod -event-handler :chsk/handshake
  [{:keys [?data]} handler]
  (handler {:id :handshake}))

(defmethod -event-handler :chsk/state
  [{:keys [?data]} handler]
  (let [[_ new-state-map] ?data]
    (if (:open? new-state-map)
      (handler {:id :connected})
      (handler {:id :disconnected}))))

(defmethod -event-handler :chsk/recv
  [{:keys [?data]} handler]
  (let [[id data] ?data]
    (if (= id :chsk/ws-ping)
      (handler {:id :ping})
      (handler (assoc data :id id)))))
