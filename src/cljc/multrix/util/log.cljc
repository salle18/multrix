(ns multrix.util.log
  (:require [taoensso.encore :as encore]
            [taoensso.timbre :as timbre]))

(defn ->debug! [messageFormat & args]
  (let [message (apply encore/format messageFormat args)]
    (timbre/debug message)))
