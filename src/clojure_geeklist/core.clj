(ns clojure-geeklist.core
  (:use [clojure.data.json :only [read-json]])
  (:require [oauth.client :as oauth]
            [com.twinql.clojure.http :as http]))

(def base-url "http://sandbox-api.geekli.st/v1/")

(def oauth-consumer-key "")
(def oauth-consumer-secret "")
(def oauth-access-token "")
(def oauth-access-token-secret "")

(def consumer (oauth/make-consumer oauth-consumer-key
                                   oauth-consumer-secret
                                   "http://sandbox-api.geekli.st/oauth/request_token"
                                   "http://sandbox.geekli.st/oauth/access_token"
                                   "http://sandbox-api.geekli.st/oauth/authorize"
                                   :hmac-sha1))

(defn make-credentials
  [url method params]
  (oauth/credentials consumer
                     oauth-access-token
                     oauth-access-token-secret
                     method
                     url
                     params))

; TODO: move this into a macro ?
(defn make-request
  [endpoint params method & [query]]
  (let [url (str base-url (apply format endpoint params))
        credentials (make-credentials url method query)
        parameters (http/map->params {:use-expect-continue false})]
    ; TODO: DRY this up
    (if (= :GET method)
      (http/get url
                :query (merge credentials query)
                :parameters parameters
                :as :json)
      (http/post url
                 :query (merge credentials query)
                 :parameters parameters
                 :as :json))))

(defn user
  ([] (make-request "user" [] :GET))
  ([id] (make-request "users/%s" [id] :GET)))

(defn following
  [id & [options]]
  (make-request "users/%s/following" [id] :GET options))

(defn user-cards
  [id & [options]]
  (make-request "users/%s/cards" [id] :GET options))

(defn cards
  [& [options]]
  (make-request "user/cards" [] :GET options))

(defn card
  [id]
  (make-request "cards/%s" [id] :GET))

(defn create-card
  [headline]
  (make-request "cards" [] :POST {:headline headline}))

(defn micro
  [id]
  (make-request "micros/%s" [id] :GET options))

(defn user-micros
  [& [options]]
  (make-request "user/micros" [] :GET options))

(defn micros
  [id & [options]]
  (make-request "users/%s/micros" [id] :GET options))

(defn create-micro
  [status]
  (make-request "micros" [] :POST {:status status}))

(defn highfive
  [type id]
  (make-request "highfive" [] :POST {:type type :gfk id}))
