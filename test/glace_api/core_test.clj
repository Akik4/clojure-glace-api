(ns glace-api.core-test
  (:require [cheshire.core :as json]
            [clojure.test :refer :all]
            [glace-api.glaces-service :refer :all]
            [ring.mock.request :as mock]
            [glace-api.core :refer [handler]]
            ))

(use-fixtures :each (fn [test-fn]
                      (reset! glace-api.glaces-repository/glaces [])
                      (reset! glace-api.glaces-repository/next-id 0)
                      (test-fn)))

(defn json-request [method url body]
  (-> (mock/request method url)
      (mock/content-type "application/json")
      (mock/body (json/generate-string body))))

(deftest http-get-glaces-test
         (testing "GET /glaces returns 200"
                  (let [response (handler (mock/request :get "/glaces"))]
                    (is (= 200 (:status response))))))

(deftest http-create-glace-test
         (testing "POST /glaces returns 200"
                  (let [response (handler (json-request :post "/glaces" {:name "fraise"}))]
                    (is (= 200 (:status response))))))

(deftest http-create-glace-invalid-test
         (testing "POST /glaces without name returns 400"
                  (let [response (handler (json-request :post "/glaces" {}))]
                    (is (= 400 (:status response))))))

(deftest http-delete-glace-test
         (testing "DELETE /glaces returns 200"
                  (create "fraise")
                  (let [response (handler (json-request :delete "/glaces" {:id 1}))]
                    (is (= 200 (:status response))))))

(deftest http-delete-not-found-test
         (testing "DELETE /glaces with unknown id returns 404"
                  (let [response (handler (json-request :delete "/glaces" {:id 999}))]
                    (is (= 404 (:status response))))))