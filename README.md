# clojure-geeklist

A simple wrapper to the Geeklist API in clojure.

## Usage

FIXME: write

## Example

```clojure
(ns whatever
  (:require [clojure-geeklist.core as :geeklist]
            [clojure-oauth as :oauth]))

(def consumer (oauth/make-consumer "consumer-key"
                                   "consumer-secret"
                                   "http://sandbox-api.geekli.st/oauth/request_token"
                                   "http://sandbox.geekli.st/oauth/access_token"
                                   "http://sandbox-api.geekli.st/oauth/authorize"
                                   :hmac-sha1))

; Get a specific user
(geeklist/user "username")

; Get 15 followers for a specific user
(geeklist/followers "username" {:count 15})
```

## License

Copyright (C) 2012 Xavier Perez

Distributed under the Eclipse Public License, the same as Clojure.
