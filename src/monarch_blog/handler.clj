(ns monarch-blog.handler
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [redirect]]
            [hiccup.core :refer [html]]
            [hiccup.element :refer [link-to]]
            [hiccup.page :refer [include-css]]
            [hiccup.form :refer [text-field text-area form-to label submit-button]]
            [clojure.java.jdbc :as sql]))

(def spec (System/getenv "DATABASE_URL"))

(defn all-posts []
  (into [] (sql/query spec
                      ["select * from posts order by id"])))

(defn save-post [{title :title body :body :as post}]
  (when-not (or (empty? title) (empty? body))
    (sql/insert! spec :posts post)))

(defn layout [& body]
  (html
    [:head
     (include-css "/css/reset.css")
     (include-css "/css/style.css")]
    [:main.container
     [:h1 "My Blog"]
     body]))

(defn post-link [{id :id title :title}]
  (link-to (str "/posts/" id)
           title))

(defn index [posts]
  (layout
    [:div.posts
     (link-to "/posts/new" "New Post")
     (map post-link posts)]))

(defn new-post [post]
  (layout
    [:h3 "New Post"]
    (form-to {:class "new-post"} [:post "/posts"]
             [:div.form-line
              (label "title" "Title")
              (text-field {:class "field"} "title" (:title post))]
             [:div.form-line
              (label "body" "Body")
              (text-area {:class "field"} "body" (:body post))]
             [:div.form-line
              (submit-button {:class "submit"} "Submit")])))


(defroutes app-routes
  (GET "/" [] (index (all-posts)))
  (GET "/posts/new" {post :params} (new-post post))
  (POST "/posts" {post :params} (if-let [saved-post (save-post post)]
                                  (redirect (str "/posts/" (:id (first saved-post))))
                                  (new-post post)))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
