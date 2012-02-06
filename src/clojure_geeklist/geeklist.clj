(ns geeklist
  (:use [clojure.data.json :only [read-json]])
  (:require [oauth.client :as oauth]
            [com.twinql.clojure.http :as http]))

(def base-url "http://sandbox-api.geekli.st/v1/")

(def ^:dynamic oauth-consumer-key "")
(def ^:dynamic oauth-consumer-secret "")
(def ^:dynamic oauth-access-token "")
(def ^:dynamic oauth-access-token-secret "")

(def ^:dynamic consumer nil)

(defn make-consumer
  [consumer-key consumer-secret]
  (oauth/make-consumer consumer-key
                       consumer-secret
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
  (make-request "micros/%s" [id] :GET))

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

(defn reply-to-micro
  "Create a micro replying to another micro"
  [micro-id status]
  (make-request "micros" [] :POST {:status status
                                   :type "micro"
                                   :in_reply_to micro-id}))

(defn reply-to-card
  "Create a micro replying to a card"
  [card-id status]
  (make-request "micros" [] :POST {:status status
                                   :type "card"
                                   :in_reply_to card-id}))

(defn highfive
  "Highfive an item (user, card ... etc)"
  [type id]
  (make-request "highfive" [] :POST {:type type :gfk id}))

(defn activity
  "Get the authenticated user activity"
  [& [options]]
  (make-request "user/activity" [] :GET options))

(defn user-activity
  "Get a specific user activity"
  [id & [options]]
  (make-request "users/%s/activity" [id] :GET options))

(defn all-activity
  "Get all the activity"
  [& [options]]
  (make-request "activity" [] :GET options))
