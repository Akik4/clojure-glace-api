(ns glace-api.repositories.glaces-repository)

(defprotocol GlacesRepository
  (create-glace [this name])
  (delete-glace [this id])
  (glace->exist [this id])
  (update-state [this id])
  (glace->get [this id])
  (get-all [this]))