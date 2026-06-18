(ns glace-api.glaces-service
  (:require [clojure.string :as str]
            [glace-api.glaces-repository :as repo]
            [glace-api.validation :as validate]))

(defn create [repository name]
  (validate/validate-field-str name)
  (repo/create-glace @repository name)
  {:message (str "create : " name)})

(defn delete [repository id]
  (validate/validate-field-int id)
  (when-not (repo/glace->exist @repository id)
    (throw (ex-info "can't find element" {:type :not-found})))
  (repo/delete-glace @repository id)
  {:message (str "supprimé : " id)})

(defn get-all [repository]
  (repo/get-all @repository))