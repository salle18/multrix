(ns multrix.routes
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [multrix.middleware :refer [wrap-middleware]]
            [multrix.view :refer [app-page]]
            [multrix.ws.config :refer [ws-route]]
            [multrix.ws.core :refer [ring-ajax-post ring-ajax-get-or-ws-handshake]]))

(defroutes routes
  (GET "/" [] (app-page))
  (GET ws-route req (ring-ajax-get-or-ws-handshake req))
  (POST ws-route req (ring-ajax-post req))
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
