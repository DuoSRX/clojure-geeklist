# clojure-geeklist

A simple wrapper to the Geeklist API in clojure.

## Installation

This library is on clojars so you can simply add `[clojure-geeklist "1.0.0-SNAPSHOT"]` to your dependencies in your project.clj.

See [clj-oauth documentation](http://github.com/mattrepl/clj-oauth) for basic usage of the oauth library.

## Example

```clojure
(ns whatever
  (:require [geeklist :as geeklist]
            [clojure-oauth :as oauth]))

(def consumer (geeklist/make-consumer "consumer-key"
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
