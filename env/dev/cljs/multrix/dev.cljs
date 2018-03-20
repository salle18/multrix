(ns ^:figwheel-no-load multrix.dev
  (:require
    [multrix.core :as core]
    [devtools.core :as devtools]))

(devtools/install!)

(enable-console-print!)

(core/init!)
