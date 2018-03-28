(ns multrix.game.handler
  (:require [multrix.util :refer [->output!]]
            [multrix.game.state :as state]))

(defmulti event-handler
  "Multimethod to handle server game events"
  :id)

(defmethod event-handler :multrix/connected
  [{:keys [client-id]}]
  (->output! "Game client connected: %s" client-id))

(defmethod event-handler :multrix/disconnected
  [{:keys [client-id]}]
  (->output! "Game client disconnected: %s" client-id))

(defmethod event-handler :multrix/up
  [{:keys [client-id]}]
  (state/handleUp client-id))

(defmethod event-handler :multrix/right
  [{:keys [client-id]}]
  (state/handleRight client-id))

(defmethod event-handler :multrix/down
  [{:keys [client-id]}]
  (state/handleDown client-id))

(defmethod event-handler :multrix/left
  [{:keys [client-id]}]
  (state/handleLeft client-id))

(defmethod event-handler :default
  [{:keys [id]}]
  (->output! "Unhandled game event: %s" id))
