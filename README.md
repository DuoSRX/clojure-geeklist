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
                                      "consumer-secret"))

; Get a specific user
(geeklist/with-oauth consumer
                     "access-token"
                     "access-token-secret"
    (geeklist/user "username"))
```

## License

Copyright (C) 2012 Xavier Perez

Distributed under the Eclipse Public License, the same as Clojure.
