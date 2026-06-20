(ns glace-api.repositories.glaces-repository)

(defprotocol GlacesRepository
  (create-glace [this name])
  (delete-glace [this id])
  (glace->exist [this id])
  (get-all [this]))