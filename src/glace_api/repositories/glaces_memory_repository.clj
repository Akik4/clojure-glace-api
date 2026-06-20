(ns glace-api.repositories.glaces-memory-repository
  (:require [glace-api.repositories.glaces-repository :as repo])
  )

(defrecord Glaces [id name state created_at])

(defn make-glace [id name]
            (->Glaces id
                      name
                      1
                      (java.time.Instant/now)))

(defn glace->map [glace]
            {:id (:id glace)
             :name (:name glace)
             :state (:state glace)
             :created_at (str (:created_at glace))})

(defrecord GlaceMemoryRepository [glaces, next-id]
  repo/GlacesRepository

  (create-glace [this name]
    (let [id (swap! next-id inc)
          glace (make-glace id name)]
      (swap! glaces conj glace)
      glace ))

  (delete-glace [this id]
    (swap! glaces (fn [current]
                    (remove #(= (:id %) id) current))))

  (update-state [this id]
    (swap! glaces (fn [current]
                  (mapv #(if (= (:id %) id)
                             (assoc % :state (inc (:state %))) %) current))))

  (glace->exist [this id]
    (boolean (some #(= (:id %) id) @glaces)))

  (glace->get [this id]
    (when-let [glace (first (filter #(= (:id %) id) @glaces))]
      (glace->map glace)))

  (get-all [this]
    (map glace->map @glaces))
  )



(defn make-memory-repository []
  (->GlaceMemoryRepository (atom []) (atom 0)))
