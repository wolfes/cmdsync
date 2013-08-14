(ns cmdsync.main
  (:use [cmdsync.config :only [app-configs cfg]]
        [clojure.tools.cli :only [cli]]
        [org.httpkit.server :only [run-server]]
        [cmdsync.routes :only [app-routes]]
        [clojure.tools.logging :only [info]]))

(defn- to-int [s] (Integer/parseInt s))

(defonce server (atom nil))

(defn start-server 
  "(Re)start server using app-configs."
  []
  ;; Stop previously running server to run -main from repl.
  (when-not (nil? @server) (@server))
  ;; Add database connection.
  (reset! server (run-server (app-routes) {:port (cfg :port)
                                           :thread (cfg :thread)})))

(defn -main 
  "Parse and save cli options and start server."
  [& args]
  (let [[options _ banner]
        (cli args
             ["-p" "--port" "Port to listen" :default 3000 :parse-fn to-int]
             ["--thread" "Http worker thread count" :default 4 :parse-fn to-int]
             ["--profile" "dev | prod" :default :dev :parse-fn keyword]
             ["--[no-]help" "Print this help"])]
    (when (:help options) (println banner) (System/exit 0))
    ;; Store configs: accessible with (cfg :key).
    (swap! app-configs merge options)
    (start-server)
    (info (str "Server started.  Listen on 0.0.0.0@" (cfg :port)))))
