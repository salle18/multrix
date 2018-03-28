(ns multrix.ws.core
  (:require
    [taoensso.sente :as sente]
    [taoensso.sente.server-adapters.http-kit :refer (get-sch-adapter)]
    [multrix.ws.handler :refer [event-handler]]))

(let [{:keys [ch-recv
              send-fn
              connected-uids
              ajax-post-fn
              ajax-get-or-ws-handshake-fn]}
      (sente/make-channel-socket! (get-sch-adapter) {:user-id-fn (fn [ring-req] (:client-id ring-req))})]

  (def ring-ajax-post ajax-post-fn)
  (def ring-ajax-get-or-ws-handshake ajax-get-or-ws-handshake-fn)
  (def ch-receive! ch-recv)
  (def ch-send! send-fn)
  (def connected-uids connected-uids))

(defonce router_ (atom nil))

(defn stop-router! [] (when-let [stop-fn @router_] (stop-fn)))
(defn start-router! [handler]
  (stop-router!)
  (reset! router_
          (sente/start-server-chsk-router!
           ch-receive! (partial event-handler handler))))

(defn start! [handler] (start-router! handler))
