(ns glace-api.services.glaces-service
  (:require [glace-api.repositories.glaces-repository :as repo]
            [glace-api.utils.validation :as validate]))

(defn validate-id [repository id]
  (validate/validate-field-int id)
  (when-not (repo/exist-glace? repository id)
    (throw (ex-info "can't find element" {:type :not-found}))))

(defn create [repository name]
  (validate/validate-field-str name)
  (repo/create-glace repository name)
  {:success (str "create : " name)})

(defn delete [repository id]
  (validate-id repository id)
  (repo/delete-glace repository id)
  {:success (str "supprimé : " id)})

(defn update-state [repository id]
  (validate-id repository id)
  (let [glace (repo/get-glace repository id)]
    (when (>= (:state glace) 4)
      ;1 preparation
      ;2 freezing
      ;3 ready
      ;4 consumption in progress
      (throw (ex-info "State out of bound" {:type :validation}))))

  (repo/update-state-glace repository id)
  {:success (str "edited : " id)})

(defn get-glace [repository id]
  (validate-id repository id)
  {:success (repo/get-glace repository id)})

(defn get-all [repository]
  {:success (repo/get-all-glaces repository)})