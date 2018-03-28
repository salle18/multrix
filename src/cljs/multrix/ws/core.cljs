(ns multrix.ws.core
  (:require [taoensso.sente :as sente]
            [taoensso.encore :as encore]
            [multrix.ws.config :refer [ws-route]]
            [multrix.ws.handler :refer [event-msg-handler]]))

(let [{:keys [ch-recv send-fn]}
      (sente/make-channel-socket! ws-route {:type :auto :client-id (encore/uuid-str)})]
  (def ch-receive! ch-recv)
  (def ch-send! send-fn))

(defonce router_ (atom nil))

(defn stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! [handler]
  (stop-router!)
  (reset! router_
          (sente/start-client-chsk-router!
           ch-receive! (partial event-msg-handler handler))))

(defn start! [handler] (start-router! handler))
