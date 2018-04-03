(ns multrix.game.renderer.console
  (:require [multrix.util.log :as log]))

(defn render [board]
  (log/->debug! "\n\n**********************************")
  (run! (partial log/->debug! "*%s*") board)
  (log/->debug! "**********************************\n\n"))
