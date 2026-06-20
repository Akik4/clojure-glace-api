(ns glace-api.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.json :as middleware]
            [reitit.ring :as ring]
            [ring.middleware.params :as ring.middleware.params]
            [glace-api.services.glaces-service :as service]
            [glace-api.repositories.glaces-memory-repository :as mem-repo])
  (:gen-class)
  (:import (clojure.lang ExceptionInfo)))

(def port 8090)
(def repository (mem-repo/make-memory-repository))

(defn handle-exception [e]
  (let [data (ex-data e)
        type (:type data)]
    (cond
      (= type :validation) {:status 400 :body {:error (ex-message e)}}
      (= type :not-found)  {:status 404 :body {:error (ex-message e)}}
      :else                {:status 500 :body {:error "internal server error"}})))

(defn handle-error-middleware [handler]
  (fn [req]
    (try
      (handler req)
      (catch ExceptionInfo e
        (handle-exception e)))))

(defn make-handler [repo]
  (-> (ring/ring-handler
    (ring/router
    [["/glaces" {:get (fn [_] {:status 200
                       :body (service/get-all repo)})
                :post (fn [req]
                         (let [name (get-in req [:body :name])]
                           {:status 200
                            :body (service/create repo name)}))
                 :delete (fn [req]
                           (let [id (get-in req [:body :id])]
                             {:status 200
                           :body (service/delete repo id)}))}]])

    (ring/create-default-handler
      {:not-found (fn [_] {:status 404
                           :body {:error "not found"}})}))
      (handle-error-middleware)
      (middleware/wrap-json-response)
      (middleware/wrap-json-body {:keywords? true})
      (ring.middleware.params/wrap-params)))

(def handler (make-handler repository))

(defn -main
  [& args]
  (jetty/run-jetty handler {:port port
                     :join? false})
  (println "Listening on port " port))


