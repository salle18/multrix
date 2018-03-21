(ns multrix.core
  (:require [multrix.routes :refer [app]]
            [multrix.ws :as ws]
            [multrix.config :refer [server-config]]
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

(defn -main [& args]
  (do
    (ws/start!)
    (println "Starting server...")
    (run-server app server-config)))
