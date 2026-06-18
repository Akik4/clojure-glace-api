(ns glace-api.service-test
  (:require [clojure.test :refer :all]
            [glace-api.glaces_service :refer :all]
            ))

(use-fixtures :each (fn [test-fn]
                      (reset! glace-api.glaces-repository/glaces [])
                      (reset! glace-api.glaces-repository/next-id 0)
                      (test-fn)))

(deftest create-test
  (testing "Glace Creation."
    (create "test")
    (create "deux glaces")
    (is (= 2 (count (get-all))))))

(deftest create-validation-test
  (testing "Creation Validation"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing a str field"
                          (create nil)))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing a str field"
                          (create "")))))

(deftest delete-test
  (testing "Glace deletion"
    (create "chocolat")
    (create "vanille")
    (delete 1)
    (is (= 1 (count (get-all))))))

(deftest delete-validation-test
  (testing "Delete glace validation"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing an int field"
                          (delete nil)))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"can't find element"
                          (delete 999)))))