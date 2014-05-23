(ns monarch-blog.test.handler
  (:use clojure.test
        ring.mock.request
        monarch-blog.handler))

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
                                [:form {:action "/posts" :method "POST"}
                                 [:label {:for "title"} "Title"]
                                 [:input {:type "text" :id "title" :name "title"}]
                                 [:label {:for "body"} "Body"]
                                 [:textarea {:id "body" :name "body"}]]) ))))

  (testing "new post with post param"
    (let [test-post {:title "Finches" :body "I like finches they are cool."}
          response (app (request :get "/posts/new" test-post))]
      (is (= (:status response) 200))
      (is (= (:body response) (layout
                                [:form {:action "/posts" :method "POST"}
                                 [:label {:for "title"} "Title"]
                                 [:input {:type "text" :id "title" :name "title" :value (:title test-post)}]
                                 [:label {:for "body"} "Body"]
                                 [:textarea {:id "body" :name "body"} (:body test-post)]]) ))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))
