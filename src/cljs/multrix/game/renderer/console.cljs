(ns multrix.game.renderer.console
  (:require [multrix.util.log :as log]))

(defn render [board]
  (let [line-count$ (atom 0)]
    (log/->debug! "\n")
    (run!
     (fn [row] (log/->debug! "%s %s" @line-count$ row) (swap! line-count$ inc)) board)
    (log/->debug! "\n")))
