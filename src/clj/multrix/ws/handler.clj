(ns multrix.ws.handler
  (:require
    [multrix.util :refer [->output!]]))

(def ping-counts (atom 0))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id)

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler :default
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid session)]
    (->output! "Unhandled event: %s" event)
    (when ?reply-fn
      (?reply-fn {:umatched-event-as-echoed-from-from-server event}))))

(defmethod -event-msg-handler :chsk/ws-ping
  [_]
  (let [c (swap! ping-counts inc)]
    (when (zero? (mod c 10001)) (reset! ping-counts 0))
    (when (zero? (mod c 10))
      (->output! "Ping counts: %s" c))))

(defmethod -event-msg-handler :chsk/uidport-open
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn client-id send-fn]}]
  (let [session (:session ring-req)]
    (->output! "Connected: %s" client-id)))

(defmethod -event-msg-handler :chsk/uidport-close
  [{:as ev-msg :keys [event id ?data ring-req ?reply-fn client-id send-fn]}]
  (let [session (:session ring-req)
        uid     (:uid session)]
    (->output! "Disconnected: %s" client-id)))
