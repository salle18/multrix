(ns multrix.routes
  (:require [compojure.core :refer [GET POST defroutes]]
            [compojure.route :refer [not-found resources]]
            [multrix.middleware :refer [wrap-middleware]]
            [multrix.view :refer [app-page]]
            [multrix.ws :refer [ring-ajax-post ring-ajax-get-or-ws-handshake]]))

(defroutes routes
  (GET "/" [] (app-page))
  (GET "/chsk" req (ring-ajax-get-or-ws-handshake req))
  (POST "/chsk" req (ring-ajax-post req))
  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
