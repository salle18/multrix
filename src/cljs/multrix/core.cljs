(ns multrix.core
  (:require-macros
    [cljs.core.async.macros :as asyncm
     :refer                     (go go-loop)])
  (:require [reagent.core :as reagent
             :refer           [atom]]
            [secretary.core :as secretary
             :include-macros    true]
            [cljs.core.async :as async
             :refer              (<! >! put! chan)]
            [taoensso.sente :as sente
             :refer             (cb-success?)]
            [accountant.core :as accountant]))

(let [{:keys [chsk ch-recv send-fn state]}
      (sente/make-channel-socket! "/chsk" {:type :auto})]
  (def chsk chsk)
  (def ch-chsk ch-recv)
  (def chsk-send! send-fn)
  (def chsk-state state))

;; -------------------------
;; Views

(defn home-page []
  [:div
   [:h2 "Welcome to multrix"]
   [:div [:a {:href "/about"} "go to about page"]]])

(defn about-page []
  [:div [:h2 "About multrix"]
   [:div [:a {:href "/"} "go to the home page"]]])

;; -------------------------
;; Routes

(defonce page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
                    (reset! page #'home-page))

(secretary/defroute "/about" []
                    (reset! page #'about-page))

;; -------------------------
;; Initialize app

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
  (mount-root))
