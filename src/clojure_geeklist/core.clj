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
  "Get the authenticated user or a specific user, given its id"
  ([] (make-request "user" [] :GET))
  ([id] (make-request "users/%s" [id] :GET)))

(defn followers
  "Get the followers of the specified user"
  [id & [options]]
  (make-request "users/%s/followers" [id] :GET options))

(defn following
  "Get the followings of the specified user"
  [id & [options]]
  (make-request "users/%s/following" [id] :GET options))

(defn follow
  "Follow an user"
  [id]
  (make-request "follow" [] :POST {:user id :action "follow"}))

(defn unfollow
  "Unfollow an user"
  [id]
  (make-request "follow" [] :POST {:user id}))

(defn user-cards
  "Get the given user cards"
  [id & [options]]
  (make-request "users/%s/cards" [id] :GET options))

(defn cards
  "Get the authenticated user cards"
  [& [options]]
  (make-request "user/cards" [] :GET options))

(defn card
  "Get a specific card"
  [id]
  (make-request "cards/%s" [id] :GET))

(defn create-card
  "Create a card"
  [headline]
  (make-request "cards" [] :POST {:headline headline}))

(defn micro
  "Get a specific micro"
  [id]
  (make-request "micros/%s" [id] :GET options))

(defn user-micros
  "Get the authenticated user micros"
  [& [options]]
  (make-request "user/micros" [] :GET options))

(defn micros
  "Get a specific user micros"
  [id & [options]]
  (make-request "users/%s/micros" [id] :GET options))

(defn create-micro
  "Create a micro"
  [status]
  (make-request "micros" [] :POST {:status status}))

(defn highfive
  "Highfive an item (user, card ... etc)"
  [type id]
  (make-request "highfive" [] :POST {:type type :gfk id}))
