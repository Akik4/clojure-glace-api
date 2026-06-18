(ns glace-api.service-test
  (:require [clojure.test :refer :all]
            [glace-api.glaces-memory-repository :as mem-repo]
            [glace-api.glaces-service :refer :all]
            ))

(def repository (atom nil))

(use-fixtures :each (fn [test-fn]
                      (reset! repository (mem-repo/make-memory-repository))
                      (test-fn)))

(deftest create-test
  (testing "Glace Creation."
    (create @repository "test")
    (create @repository "deux glaces")
    (is (= 2 (count (get-all @repository))))))

(deftest create-validation-test
  (testing "Creation Validation"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing a str field"
                          (create @repository  nil)))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing a str field"
                          (create @repository "")))))

(deftest delete-test
  (testing "Glace deletion"
    (create @repository "chocolat")
    (create @repository "vanille")
    (delete @repository 1)
    (is (= 1 (count (get-all @repository ))))))

(deftest delete-validation-test
  (testing "Delete glace validation"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing an int field"
                          (delete @repository nil)))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"can't find element"
                          (delete @repository 999)))))