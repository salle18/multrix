(ns multrix.game.emitter
  (:require [clojure.core.async
             :as    async
             :refer [close! go-loop <! timeout]]))

(defn emit-all! [get-uids emitter!]
  (let [uids (get-uids)]
    (doseq [uid   uids
            :when (some? uid)]
      (emitter! uid))))

(defn start-emit! [emitter timeout]
  (go-loop []
           (<! (async/timeout timeout))
           (emitter)
           (recur)))

(defn start! [get-uids emitter! timeout]
  (start-emit! (fn [] (emit-all! get-uids emitter!)) timeout))
