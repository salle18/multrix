(ns multrix.server
  (:require [multrix.handler :refer [app]]
            [multrix.config :refer [config]]
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

(defn -main [& args]
  (do
    (println "Server started...")
    (run-server app config)))
