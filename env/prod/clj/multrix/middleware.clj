(ns multrix.middleware
  (:require [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.params :refer [wrap-params]]))

(defn wrap-middleware [handler]
  (-> handler (wrap-defaults site-defaults) wrap-keyword-params
      wrap-params))
