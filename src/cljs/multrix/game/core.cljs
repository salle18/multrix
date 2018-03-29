(ns multrix.game.core
  (:require [multrix.game.listeners :as listeners]
            [multrix.game.handler :refer [event-handler]]))

(defn start! [input!]
  (listeners/init! (input! event-handler)))
