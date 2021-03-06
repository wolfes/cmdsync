(ns cmdsync.routes
  (:use [compojure.core :only [defroutes GET POST context]]
        [compojure.handler :only [site]]
        [cmdsync.middleware :only [wrap-failsafe wrap-request-logging-in-dev
                                   wrap-reload-in-dev JGET JPUT JPOST JDELETE]])
  (:require [ring.middleware.reload :as reload]
            [cmdsync.config :as conf]
            [cmdsync.handlers.app :as app]
            [cmdsync.handlers.api :as api]
            [compojure.route :as route]))

;; TODO(wstyke): Extract this.
(def allowed-channel-name-regex #"[\:\_\-a-zA-Z0-9]+")

;; Main route table.
(defroutes routes
  ; ms.shenfeng mustache
  ;(GET ["/"] {} app/show-landing)
  ;(GET ["/tabspire/docs"] {} app/show-tabspire-docs)
  (GET ["/"] {} app/show-enlive-landing)
  (GET ["/tabspire/docs"] {} app/show-docs-tabspire)
  (GET ["/vimspire/docs"] {} app/show-docs-vimspire)
  (context "/tabspire/api/0" []
           (GET ["/:channel-name/:cmd",
                 :channel-name , allowed-channel-name-regex
                 :cmd allowed-channel-name-regex] {}
                api/route-tabspire-cmd)
           (POST ["/:channel-name/:cmd"
                  :channel-name allowed-channel-name-regex
                  :cmd allowed-channel-name-regex] {}
                 api/route-tabspire-api-post))
  (route/resources "/")
  ;; Static files under ./public folder, prefix /static
  ;; Ex. "/static/css/style.css""
  (route/files "/static")
  (route/not-found "<h1>404: Not Found</h1>"))

(defn app-routes
  "The middleware wrapped master routing table, ready for server creation."
  []
  (let [wrapped-routes (if (= (conf/cfg :profile) :dev)
                         ; Dev
                         (-> #'routes
                             wrap-request-logging-in-dev
                             wrap-reload-in-dev
                             wrap-failsafe
                             reload/wrap-reload
                             site)
                         ; Not Dev (ie: Prod)
                         (site routes))] 
    wrapped-routes))
