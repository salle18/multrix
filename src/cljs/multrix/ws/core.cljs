(ns multrix.ws.core
  (:require [taoensso.sente :as sente]
            [taoensso.encore :as encore]
            [multrix.ws.config :refer [ws-route]]
            [multrix.ws.handler :refer [event-handler]]))

(defonce router$ (atom nil))

(defn stop-router! [] (when-let [stop-f @router$] (stop-f)))
(defn start-router! [ch-receive! handler]
  (stop-router!)
  (reset! router$
          (sente/start-client-chsk-router!
           ch-receive! (partial event-handler handler))))

(defn start! [client-uid handler]
  (let [{:keys [ch-recv send-fn]}
        (sente/make-channel-socket! ws-route {:type :auto :client-id client-uid})]
    (start-router! ch-recv handler)
    send-fn))
