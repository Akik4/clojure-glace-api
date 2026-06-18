(ns glace-api.core-test
  (:require [cheshire.core :as json]
            [clojure.test :refer :all]
            [glace-api.glaces-memory-repository :as mem-repo]
            [glace-api.glaces-service :refer :all]
            [ring.mock.request :as mock]
            [glace-api.core :as core]
            ))

(def test-repo (atom nil))
(def test-handler (atom nil))

(use-fixtures :each (fn [test-fn]
                      (reset! test-repo (mem-repo/make-memory-repository))
                      (reset! test-handler (core/make-handler @test-repo))
                      (test-fn)))

(defn json-request [method url body]
  (-> (mock/request method url)
      (mock/content-type "application/json")
      (mock/body (json/generate-string body))))

(deftest http-get-glaces-test
         (testing "GET /glaces returns 200"
                  (let [response (@test-handler (mock/request :get "/glaces"))]
                    (is (= 200 (:status response))))))

(deftest http-create-glace-test
         (testing "POST /glaces returns 200"
                  (let [response (@test-handler (json-request :post "/glaces" {:name "fraise"}))]
                    (is (= 200 (:status response))))))

(deftest http-create-glace-invalid-test
         (testing "POST /glaces without name returns 400"
                  (let [response (@test-handler (json-request :post "/glaces" {}))]
                    (is (= 400 (:status response))))))

(deftest http-delete-glace-test
         (testing "DELETE /glaces returns 200"
                  (create @test-repo "fraise")
                  (let [response (@test-handler (json-request :delete "/glaces" {:id 1}))]
                    (is (= 200 (:status response))))))

(deftest http-delete-not-found-test
         (testing "DELETE /glaces with unknown id returns 404"
                  (let [response (@test-handler (json-request :delete "/glaces" {:id 999}))]
                    (is (= 404 (:status response))))))