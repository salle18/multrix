(ns multrix.core
  (:require [reagent.core :as reagent
             :refer           [atom]]
            [secretary.core :as secretary
             :include-macros    true]
            [accountant.core :as accountant]
            [multrix.event-handler :refer [event-handler]]
            [multrix.ws.core :as ws]))

;; -------------------------
;; Views

(defn app-page []
  [:div
   [:h2 "Welcome to multrix"]])

;; -------------------------
;; Routes

(defonce page (atom #'app-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
                    (reset! page #'app-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (ws/start! event-handler)
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
