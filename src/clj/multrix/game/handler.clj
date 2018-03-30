(ns multrix.game.handler
  (:require [multrix.util :refer [->output!]]
            [multrix.game.events :as events]
            [multrix.game.state :as state]))

(defmulti event-handler
  "Multimethod to handle server game events"
  :id)

(defmethod event-handler events/connected
  [{:keys [client-uid send]}]
  (let [connected-state (state/add-client client-uid)]
    (case connected-state
      :connected (do
                   (->output! "Client connected: %s" client-uid)
                   (send client-uid [events/game-init {:data (state/get-clients-state)}]))
      :game-full (send client-uid [events/game-full]))))

(defmethod event-handler events/disconnected
  [{:keys [client-uid]}]
  (->output! "Client disconnected: %s" client-uid)
  (state/remove-client client-uid))

(defmethod event-handler events/rotate
  [{:keys [client-uid]}]
  (state/rotate client-uid))

(defmethod event-handler events/move-right
  [{:keys [client-uid]}]
  (state/move-right client-uid))

(defmethod event-handler events/move-down
  [{:keys [client-uid]}]
  (state/move-down client-uid))

(defmethod event-handler events/move-left
  [{:keys [client-uid]}]
  (state/move-left client-uid))

(defmethod event-handler events/speed-down
  [{:keys [client-uid]}]
  (state/speed-down client-uid))

(defmethod event-handler :default
  [{:keys [id]}]
  (->output! "Unhandled game event: %s" id))
