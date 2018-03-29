(ns multrix.core
  (:require [multrix.routes :refer [app]]
            [multrix.ws.core :as ws]
            [multrix.config :refer [server-config]]
            [org.httpkit.server :refer [run-server]]
            [multrix.game.core :as game]
            [multrix.event-middleware :refer [event-middleware]])
  (:gen-class))

(defn game-input! [game-handler]
  (ws/start! (partial event-middleware game-handler)))

(def game-output! ws/ch-send!)

(defn -main [& args]
  (do
    (game/start! game-input! game-output!)
    (println "Starting server...")
    (run-server app server-config)))
