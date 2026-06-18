(ns glace-api.glaces-service
  (:require [clojure.string :as str]
            [glace-api.glaces-repository :as repo]
            [glace-api.validation :as validate]))

(defn create [name]
  (validate/validate-field-str name)
  (repo/create-glace name)
  {:message (str "create : " name)})

(defn delete [id]
  (validate/validate-field-int id)
  (when-not (repo/glace->exist id)
    (throw (ex-info "can't find element" {:type :not-found})))
  (repo/delete-glace id)
  {:message (str "supprimé : " id)})

(defn get-all []
  (repo/get-all))