(ns multrix.config
  (:require [config.core :refer [env]]))

(defn normalize-port [port, default]
  (cond (number? port) port
    (string? port)     (Integer/parseInt port)
    :else              default))

(def config
  {:port  (normalize-port (env :port) 3000)
   :join? false})
