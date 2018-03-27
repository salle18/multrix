(ns multrix.ws.core
  (:require [taoensso.sente :as sente
             :refer             (cb-success?)]
            [taoensso.encore :as encore]
            [multrix.ws.handler :refer [event-msg-handler]]))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk" {:client-id (encore/uuid-str)})]
  (def chsk chsk)
  (def ch-chsk ch-recv)
  (def chsk-send! send-fn)
  (def chsk-state state))

(defonce router_ (atom nil))

(defn stop-router! [] (when-let [stop-f @router_] (stop-f)))
(defn start-router! []
  (stop-router!)
  (reset! router_
          (sente/start-client-chsk-router!
           ch-chsk event-msg-handler)))

(defn start! [] (start-router!))
