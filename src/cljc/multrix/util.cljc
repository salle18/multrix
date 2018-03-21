(ns multrix.util
  (:require [taoensso.encore :as encore]
            [taoensso.timbre :as timbre]))

(defn ->output! [messageFormat & args]
  (let [message (apply encore/format messageFormat args)]
    (timbre/debug message)))
