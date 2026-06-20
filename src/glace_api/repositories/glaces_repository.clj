(ns glace-api.repositories.glaces-repository)

(defprotocol GlacesRepository
  (create-glace [this name])
  (delete-glace [this id])
  (exist-glace? [this id])
  (update-state-glace [this id])
  (get-glace [this id])
  (get-all-glaces [this]))