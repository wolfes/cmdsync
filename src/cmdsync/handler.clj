(ns cmdsync.handler
  (:use [org.httpkit.server :only [run-server]]
        [compojure.core :only [defroutes GET POST]])
  (:require [ring.middleware.reload :as reload]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.data.json :as json]))

(defroutes app-routes
  (GET ["/"] {} "MOTD: <a href='http://www.nyan.cat/dub.php'>Nyan Cat Tabbyspire!</a>")
  (route/resources "/")
  (route/not-found "Not Found"))

(defn in-dev? [args] 
  "TODO: Implement before creating production version."
  true)

(defn -main [& args]
  (let [handler (if (in-dev? args)
                  (reload/wrap-reload (handler/site #'app-routes)) ; Hot reload dev server.
                  (handler/site app-routes))]
    (run-server handler {:port 3334})))

