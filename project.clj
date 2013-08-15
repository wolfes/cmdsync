(defproject cmdsync "0.1.0-SNAPSHOT"
  :description "Server for connecting Tabspire & Vimspire."
  :url "http://example.com/FIXME"
  :main cmdsync.main
  :uberjar-name "cmdsync-standalone.jar"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.cli "0.2.2"]
                 [org.clojure/data.json "0.2.2"]
                 [org.clojure/tools.logging "0.2.6"]
                 ;; Template: Still undecided vs Enlive & others.
                 [enlive "1.1.1"]
                 [me.shenfeng/mustache "1.1"]
                 ;; [net.cgrand.enlive-html "???"]
                 ;; Awesome channels library.
                 [lamina "0.5.0-rc3"]
                 ;; Routers, HTTP Adapters, HTTP Servers
                 [compojure "1.1.5"]
                 [ring/ring-core "1.2.0"]
                 [ring/ring-devel "1.2.0"]
                 [http-kit "2.0.0"]]
  :profiles {:dev {:dependencies [[ring-mock "0.1.5"]]}}
  :repl-options {;; Specify the string to print when prompting for input.
                 ; :prompt (fn [ns] (str ns "=>"))
                 ; Specify the ns to start the REPL in (override :main).
                 ; :init-ns cmdsync.handler
                 :init (use '[clojure.repl :only [doc find-doc source]])})
