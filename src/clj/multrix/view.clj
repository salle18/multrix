(ns multrix.view
  (:require [hiccup.page :refer [include-js include-css html5]]
            [multrix.env :refer [dev?]]))

(def mount-target
  [:div#app
   [:h3 "ClojureScript has not been compiled!"]
   [:p
    "please run "
    [:b "lein figwheel"]
    " in order to start the compiler"]])

(defn head []
  [:head
   [:title "Multrix"]
   [:meta {:charset "utf-8"}]
   [:meta
    {:name    "viewport"
     :content "width=device-width, initial-scale=1"}]
   (include-css (if dev? "/css/site.css" "/css/site.min.css"))])

(defn app-page []
  (html5
   (head)
   [:body
    {:class "body-container"}
    mount-target
    (include-js "/js/app.js")]))
