(ns glace-api.glaces-repository
  )

(defrecord Glaces [id name state created_at])
(def glaces (atom []))
(def next-id (atom 0))

(defn make-glace [id name]
  (->Glaces id
            name
            :preparation
            (java.time.Instant/now)))

(defn create-glace [name]
  (let [id (swap! next-id inc)
        glace (make-glace id name)]
    (swap! glaces conj glace)
    glace ))

(defn delete-glace [id]
  (swap! glaces (fn [current]
                  (remove #(= (:id %) id) current))))

(defn glace->map [glace]
  {:id (:id glace)
   :name (:name glace)
   :state (:state glace)
   :created_at (str (:created_at glace))})

(defn glace->exist [id]
  (boolean (some #(= (:id %) id) @glaces)))

(defn get-all []
  (map glace->map @glaces))