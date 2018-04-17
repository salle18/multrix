(ns multrix.game.handler
  (:require [multrix.game.emitter :as emitter]
            [multrix.game.events :as events]
            [multrix.game.state :as state]
            [multrix.util.log :as log]))

(defmulti event-handler
  "Multimethod to handle server game events"
  :id)

(defmethod event-handler events/connected
  [{:keys [client-uid]}]
  (log/->debug! "Client connected: %s" client-uid)
  (let [connected-state (state/add-client client-uid)]
    (case connected-state
      :connected (emitter/connect-client! client-uid)
      :game-full (emitter/emit-game-full! client-uid))))

(defmethod event-handler events/disconnected
  [{:keys [client-uid]}]
  (log/->debug! "Client disconnected: %s" client-uid)
  (state/remove-client client-uid)
  (emitter/disconnect-client! client-uid))

(defmethod event-handler events/rotate
  [{:keys [client-uid]}]
  (state/rotate client-uid)
  (emitter/emit-state-client! client-uid))

(defmethod event-handler events/move-right
  [{:keys [client-uid]}]
  (state/move-right client-uid)
  (emitter/emit-state-client! client-uid))

(defmethod event-handler events/move-down
  [{:keys [client-uid]}]
  (state/move-down client-uid)
  (emitter/emit-state-client! client-uid))

(defmethod event-handler events/move-left
  [{:keys [client-uid]}]
  (state/move-left client-uid)
  (emitter/emit-state-client! client-uid))

(defmethod event-handler events/speed-down
  [{:keys [client-uid]}]
  (state/speed-down client-uid))

(defmethod event-handler :default
  [{:keys [id]}]
  (log/->debug! "Unhandled game event: %s" id))
