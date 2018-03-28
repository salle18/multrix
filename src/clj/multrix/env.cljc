(ns multrix.env
  (:require [config.core :refer [env]]))

(defn get-env-param [key]
  (env key))

(def dev? (get-env-param :dev))
