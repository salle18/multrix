(ns multrix.server
  (:require [multrix.handler :refer [app]]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

 (defn -main [& args]
   (let [port (Integer/parseInt (or (str (env :port)) "3000"))]
     (run-jetty app {:port port :join? false})))
