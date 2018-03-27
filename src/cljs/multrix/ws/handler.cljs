(ns multrix.ws.handler
  (:require-macros
    [cljs.core.async.macros :as asyncm
     :refer                     (go go-loop)])
  (:require [taoensso.encore :as encore
             :refer-macros       (have)]
            [multrix.util :refer [->output!]]))

(defmulti event-handler
  "Multimethod to handle server push events"
  first)

(defmethod event-handler :default
  [data]
  (->output! "Unhandled server push event: %s" data))

(defmethod event-handler :chsk/ws-ping
  []
  (->output! "Server ping"))

(defmulti -event-msg-handler
  "Multimethod to handle Sente `event-msg`s"
  :id)

(defn event-msg-handler
  "Wraps `-event-msg-handler` with logging, error catching, etc."
  [{:as ev-msg :keys [id ?data event]}]
  (-event-msg-handler ev-msg))

(defmethod -event-msg-handler :default
  [{:as ev-msg :keys [event]}]
  (->output! "Unhandled event: %s" event))

(defmethod -event-msg-handler :chsk/handshake
  [{:as ev-msg :keys [?data]}]
  (let [[?uid ?csrf-token ?handshake-data] ?data]
    (->output! "Handshake: %s" ?data)))

(defmethod -event-msg-handler :chsk/state
  [{:as ev-msg :keys [?data]}]
  (let [[old-state-map new-state-map] (have vector? ?data)]
    (if (:first-open? new-state-map)
      (->output! "Channel socket successfully established!: %s" new-state-map)
      (->output! "Channel socket state change: %s" new-state-map))))

(defmethod -event-msg-handler :chsk/recv
  [{:as ev-msg :keys [?data]}]
  (->output! "Push event from server: %s" ?data)
  (event-handler ?data))
