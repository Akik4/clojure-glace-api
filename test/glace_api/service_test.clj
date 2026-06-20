(ns glace-api.service-test
  (:require [clojure.test :refer :all]
            [glace-api.repositories.glaces-memory-repository :as mem-repo]
            [glace-api.repositories.glaces-repository :as repo]
            [glace-api.services.glaces-service :refer :all]
            ))

(def repository (atom nil))

(use-fixtures :each (fn [test-fn]
                      (reset! repository (mem-repo/make-memory-repository))
                      (test-fn)))

(deftest create-test
  (testing "Glace Creation."
    (create @repository "test")
    (create @repository "deux glaces")
    (is (= 2 (count (:success (get-all @repository)))))))

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
    (is (= 1 (count (:success (get-all @repository )))))))

(deftest delete-validation-test
  (testing "Delete glace validation"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing an int field"
                          (delete @repository nil)))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"can't find element"
                          (delete @repository 999)))))

(deftest update-state-test
  (testing "Glace update state"
    (create @repository "chocolat")
    (update-state @repository 1)
    (is (= 2 (:state (first (:success (get-all @repository))))))))

(deftest update-validation-test
  (testing "Update glace validation"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing an int field"
                          (update-state @repository nil)))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"can't find element"
                          (update-state @repository 999)))))

(deftest update-step-test
  (testing "Update more than existing step"
    (create @repository "chocolat")
    (dotimes [_ 3]
      (update-state @repository 1))
    (let [glace (:success (get-glace @repository 1))]
      (is (= 4 (:state glace))))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                         #"State out of bound"
                         (update-state @repository 1)))))

(deftest get-glace-test
  (testing "get a single glace"
    (create @repository "chocolat")
    (let [glace (:success (get-glace @repository 1))]
      (is (= "chocolat" (:name glace))))))

(deftest get-glace-validation-test
  (testing "get a single glace"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"missing an int field"
                          (get-glace @repository nil)))
    (is (thrown-with-msg? clojure.lang.ExceptionInfo
                          #"can't find element"
                          (get-glace @repository 999)))))