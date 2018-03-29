(ns multrix.core
  (:require [reagent.core :as reagent
             :refer           [atom]]
            [secretary.core :as secretary
             :include-macros    true]
            [taoensso.encore :as encore]
            [accountant.core :as accountant]
            [multrix.game.core :as game]
            [multrix.game.state :as state]
            [multrix.event-middleware :refer [event-middleware]]
            [multrix.ws.core :as ws]))

(def client-uid (encore/uuid-str))

(def connected (reagent/cursor state/game-state [:connected]))

(def states (reagent/cursor state/game-state [:clients]))


;; -------------------------
;; Views

(defn app-page []
  [:div
   (if @connected [:h2 "Connected"] [:h2 "Not connected"])])

;; -------------------------
;; Routes

(defonce page (atom #'app-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
                    (reset! page #'app-page))

;; -------------------------
;; Initialize app

(defn game-handler [game-event-handler]
  (ws/start! client-uid (partial event-middleware game-event-handler)))

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
   {:nav-handler
    (fn [path]
      (secretary/dispatch! path))
    :path-exists?
    (fn [path]
      (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root)
  (game/start! game-handler))
