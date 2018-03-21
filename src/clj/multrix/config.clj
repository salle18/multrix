(ns multrix.config
  (:require [multrix.env :refer [get-env-param]]))

(defn normalize-port [port, default]
  (cond (number? port) port
    (string? port)     (Integer/parseInt port)
    :else              default))

(def server-config
  {:port  (normalize-port (get-env-param :port) 3000)
   :join? false})
