(ns multrix.game.emitter
  (:require [clojure.core.async
             :as    async
             :refer [close! go-loop <! timeout]]))

(defn emit-all! [uids emitter!]
  (let [uids (:any @uids)]
    (doseq [uid uids]
      (emitter! uid))))

(defn start-emit! [emitter timeout]
  (go-loop []
           (<! (async/timeout timeout))
           (emitter)
           (recur)))

(defn start! [uids emitter! timeout]
  (start-emit! (fn [] (emit-all! uids emitter!)) timeout))
