(ns monarch-blog.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [hiccup.core :refer [html]]
            [hiccup.element :refer [link-to]]
            [hiccup.form :refer [text-field text-area form-to label submit-button]]))

(defn layout [& body]
  (html body))
(defn index []
  (layout
    [:div.posts
     (link-to "/posts/new" "New Post")]))

(defn new-post [post]
  (layout
    (form-to [:post "/posts"]
             (label "title" "Title")
             (text-field "title" (:title post))
             (label "body" "Body")
             (text-area "body" (:body post)))))

(defroutes app-routes
  (GET "/" [] (index))
  (GET "/posts/new" {post :params} (new-post post))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
