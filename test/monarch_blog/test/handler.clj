(ns monarch-blog.test.handler
  (:require [clojure.java.jdbc :as sql])
  (:use clojure.test
        ring.mock.request
        monarch-blog.handler))

(def test-spec "postgresql://localhost:5432/test_monarch_blog")

(defn reset-posts [f]
  (f)
  (sql/db-do-commands test-spec
                      "delete from posts"))

(use-fixtures :once reset-posts)

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (= (:body response) (layout
                                [:div.posts
                                 [:a {:href "/posts/new"} "New Post"]])))))
  (testing "new post without post param"
    (let [response (app (request :get "/posts/new"))]
      (is (= (:status response) 200))
      (is (= (:body response) (layout
                                [:h3 "New Post"]
                                [:form {:action "/posts" :method "POST" :class "new-post"}
                                 [:div.form-line
                                  [:label {:for "title"} "Title"]
                                  [:input {:type "text" :class "field" :id "title" :name "title"}]]
                                 [:div.form-line
                                  [:label {:for "body"} "Body"]
                                  [:textarea {:id "body" :class "field" :name "body"}]]
                                 [:div.form-line
                                  [:input {:type "submit" :class "submit" :value "Submit"}]]])))))

  (testing "new post with post param"
    (let [test-post {:title "Finches" :body "I like finches they are cool."}
          response (app (request :get "/posts/new" test-post))]
      (is (= (:status response) 200))
      (is (= (:body response) (layout
                                [:h3 "New Post"]
                                [:form {:action "/posts" :method "POST" :class "new-post"}
                                 [:div.form-line
                                  [:label {:for "title"} "Title"]
                                  [:input {:type "text" :class "field" :id "title" :name "title" :value (:title test-post)}]]
                                 [:div.form-line
                                  [:label {:for "body"} "Body"]
                                  [:textarea {:id "body" :name "body" :class "field"} (:body test-post)]]
                                 [:div.form-line
                                  [:input {:type "submit" :value "Submit" :class "submit"}]]])))))

  (testing "new post with post param"
    (let [test-post {:title "Finches" :body "I like finches they are cool."}
          response (app (request :post "/posts" test-post))]
      (is (= (:status response) 302))
      (is (re-find #"/posts/\d+"(get-in response [:headers "Location"])))))

  (testing "new post rerenders form when missing title"
    (let [test-post {:body "I like finches they are cool."}
          response (app (request :post "/posts" test-post))]
      (is (= (:status response) 200))
      (is (= (:body response) (new-post test-post)))))

  (testing "new post rerenders form when missing body"
    (let [test-post {:title "Finches"}
          response (app (request :post "/posts" test-post))]
      (is (= (:status response) 200))
      (is (= (:body response) (new-post test-post)))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
