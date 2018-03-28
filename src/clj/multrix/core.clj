(ns multrix.core
  (:require [multrix.routes :refer [app]]
            [multrix.ws.core :as ws]
            [multrix.config :refer [server-config]]
            [org.httpkit.server :refer [run-server]]
            [multrix.event-handler :refer [event-handler]])
  (:gen-class))

(defn -main [& args]
  (do
    (ws/start! event-handler)
    (println "Starting server...")
    (run-server app server-config)))
