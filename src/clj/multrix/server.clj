(ns multrix.server
  (:require [multrix.handler :refer [app]]
            [multrix.config :refer [config]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

(defn -main [& args]
  (run-jetty app config))
