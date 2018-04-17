(ns multrix.game.core
  (:require [multrix.game.handler :refer [event-handler]]
            [multrix.game.emitter :as emitter]))

(defn start! [input! output!]
  (input! event-handler)
  (emitter/init! output!))
