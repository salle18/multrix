(ns multrix.game.core
  (:require [multrix.game.handler :refer [event-handler]]))

(defn start! [input!] (input! event-handler))
