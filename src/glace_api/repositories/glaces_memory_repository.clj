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
    (let [id (swap! (:next-id this) inc)
          glace (make-glace id name)]
      (swap! (:glaces this) conj glace)
      glace ))

  (delete-glace [this id]
    (swap! (:glaces this) (fn [current]
                    (remove #(= (:id %) id) current))))

  (update-state-glace [this id]
    (swap! (:glaces this) (fn [current]
                  (mapv #(if (= (:id %) id)
                             (assoc % :state (inc (:state %))) %) current))))

  (exist-glace? [this id]
    (boolean (some #(= (:id %) id) @(:glaces this))))

  (get-glace [this id]
    (when-let [glace (first (filter #(= (:id %) id) @(:glaces this)))]
      (glace->map glace)))

  (get-all-glaces [this]
    (map glace->map @(:glaces this)))
  )



(defn make-memory-repository []
  (->GlaceMemoryRepository (atom []) (atom 0)))
