(ns glace-api.validation
  (:require [clojure.string :as str]))

(defn validate_field_str [arg]
  (when (or (nil? arg) (str/blank? arg))
    (throw (ex-info "missing a str field" {:type :validation}))))

(defn validate_field_int [arg]
  (when (nil? arg)
    (throw (ex-info "missing an int field" {:type :validation}))))