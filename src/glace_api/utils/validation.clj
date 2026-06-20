(ns glace-api.utils.validation
  (:require [clojure.string :as str]))

(defn validate-field-str [arg]
  (when (or (nil? arg) (str/blank? arg))
    (throw (ex-info "missing a str field" {:type :validation}))))

(defn validate-field-int [arg]
  (when (nil? arg)
    (throw (ex-info "missing an int field" {:type :validation}))))