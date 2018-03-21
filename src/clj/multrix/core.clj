(ns multrix.core
  (:require [multrix.routes :refer [app]]
            [multrix.config :refer [server-config]]
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

(defn -main [& args]
  (do
    (println "Server started...")
    (run-server app server-config)))
