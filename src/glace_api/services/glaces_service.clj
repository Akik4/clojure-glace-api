(ns glace-api.services.glaces-service
  (:require [glace-api.repositories.glaces-repository :as repo]
            [glace-api.utils.validation :as validate]))

(defn create [repository name]
  (validate/validate-field-str name)
  (repo/create-glace repository name)
  {:message (str "create : " name)})

(defn delete [repository id]
  (validate/validate-id repository id)
  (repo/delete-glace repository id)
  {:message (str "supprimé : " id)})

(defn update-state [repository id]
  (validate/validate-id repository id)
  (let [glace (repo/glace->get repository id)]
    (when (>= (:state glace) 4)
      "1 preparation
      2 freezing
      3 ready
      4 consumption in progress"
      (throw (ex-info "State out of bound" {:type :validation}))))

  (repo/update-state repository id)
  {:message (str "edited : " id)})

(defn get-all [repository]
  (repo/get-all repository))