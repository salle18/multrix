(ns multrix.game.handler
  (:require [multrix.util :refer [->output!]]
            [multrix.game.events :as events]
            [multrix.game.state :as state]))

(defmulti event-handler
  "Multimethod to handle server game events"
  :id)

(defmethod event-handler events/connected
  [{:keys [client-id send]}]
  (->output! "Client connected: %s" client-id)
  (state/add-client client-id send))

(defmethod event-handler events/disconnected
  [{:keys [client-id]}]
  (->output! "Client disconnected: %s" client-id)
  (state/remove-client client-id))

(defmethod event-handler events/rotate
  [{:keys [client-id]}]
  (state/rotate client-id))

(defmethod event-handler events/move-right
  [{:keys [client-id]}]
  (state/move-right client-id))

(defmethod event-handler events/move-down
  [{:keys [client-id]}]
  (state/move-down client-id))

(defmethod event-handler events/move-left
  [{:keys [client-id]}]
  (state/move-left client-id))

(defmethod event-handler events/speed-down
  [{:keys [client-id]}]
  (state/speed-down client-id))

(defmethod event-handler :default
  [{:keys [id]}]
  (->output! "Unhandled game event: %s" id))
