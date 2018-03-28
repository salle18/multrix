(ns multrix.game.emitter
  (:require [clojure.core.async
             :as    async
             :refer [close! go-loop <! timeout]]))

(defonce emit-channel (atom nil))

(defonce emit-enabled? (atom true))

(defn emit-all! [uids emitter!]
  (let [uids (:any @uids)]
    (doseq [uid uids]
      (emitter! uid))))

(defn start-emit! [emitter timeout]
  (go-loop []
           (<! (async/timeout timeout))
           (when @emit-enabled? (emitter))
           (recur)))

(defn stop-emit! []
  (close! @emit-channel)
  (reset! emit-enabled? false))

(defn start! [uids emitter! timeout]
  (reset! emit-channel (start-emit! (fn [] (emit-all! uids emitter!)) timeout)))

(defn stop! [] stop-emit!)
