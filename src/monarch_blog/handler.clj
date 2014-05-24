(ns monarch-blog.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [redirect]]
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

(defn save-post [{title :title body :body :as post}]
  (when (and title body)
    (assoc post :id 123)))

(defroutes app-routes
  (GET "/" [] (index))
  (GET "/posts/new" {post :params} (new-post post))
  (POST "/posts/new" {post :params} (if-let [saved-post (save-post post)]
                                      (redirect (str "/posts/" (:id saved-post)))
                                      (new-post post)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
