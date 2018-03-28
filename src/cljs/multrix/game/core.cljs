(ns multrix.game.core
  (:require [multrix.game.controls :as controls]
            [multrix.game.handler :refer [event-handler]]))

(defn start! [input! output!]
  (input! event-handler)
  (controls/init! output!))
