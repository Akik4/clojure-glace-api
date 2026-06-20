(ns glace-api.utils.validation
  (:require [clojure.string :as str]
            [glace-api.repositories.glaces-repository :as repo]))

(defn validate-field-str [arg]
  (when (or (nil? arg) (str/blank? arg))
    (throw (ex-info "missing a str field" {:type :validation}))))

(defn validate-field-int [arg]
  (when (nil? arg)
    (throw (ex-info "missing an int field" {:type :validation}))))
