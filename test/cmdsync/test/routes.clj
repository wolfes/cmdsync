(ns cmdsync.test.routes
  (:use clojure.test
        ring.mock.request  
        cmdsync.routes))

(deftest test-app
  ;(testing "main route"
    ;(let [response (app (request :get "/"))]
      ;(is (= (:status response) 200))
      ;(is (= (:body response) "Hello World"))))
  
  (testing "not-found route"
    (let [response (routes (request :get "/invalid"))]
      (is (= (:status response) 404)))))
