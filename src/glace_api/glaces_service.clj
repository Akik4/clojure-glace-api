(ns glace-api.glaces_service
  (:require [clojure.string :as str]
            [glace-api.glaces-repository :as repo]
            [glace-api.validation :as validate]))

(defn create [name]
  (validate/validate_field_str name)
  (repo/create-glace name)
  {:message (str "create : " name)})

(defn delete [id]
  (validate/validate_field_int id)
  (when-not (repo/glace->exist id)
    (throw (ex-info "can't find element" {:type :validation})))
  (repo/delete-glace id)
  {:message (str "supprimé : " id)})

(defn get-all []
  (repo/get-all))