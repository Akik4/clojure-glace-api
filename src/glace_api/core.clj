
(ns glace-api.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as middleware]
            [reitit.ring :as ring]
            [ring.middleware.params :as ring.middleware.params]
            [glace-api.glaces_service :as service])
  (:gen-class))

(def port 8090)

(def handler
  (-> (ring/ring-handler
    (ring/router
    [["/glaces" {:get (fn [_] {:status 200
                       :body (service/get-all)})
                :post (fn [req]
                         (let [name (get-in req [:body :name])]
                           {:status 200
                            :body (service/create name)}))
                 :delete (fn [req]
                           (let [id (get-in req [:body :id])]
                             {:status 200
                           :body (service/delete id)}))}]])

    (ring/create-default-handler
      {:not-found (fn [_] {:status 404
                           :body {:error "not found"}})}))
      (middleware/wrap-json-response)
      (middleware/wrap-json-body {:keywords? true})
      (ring.middleware.params/wrap-params)))

(defn -main
  [& args]
  (jetty/run-jetty handler {:port port
                     :join? false})
  (println "Listening on port " port))


