(defproject cmdsync "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-devel "1.2.0"]
                 [http-kit "2.0.0"]
                 [org.clojure/data.json "0.2.2"]]
  :main cmdsync.handler
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}})
