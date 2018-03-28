(ns multrix.game.core
  (:require [multrix.game.listeners :as listeners]
            [multrix.game.handler :refer [event-handler]]))

(defn start! [input! output!]
  (input! event-handler)
  (listeners/init! output!))
