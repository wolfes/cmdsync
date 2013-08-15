(ns cmdsync.handlers.app
  (:use [net.cgrand.enlive-html
         :only [deftemplate defsnippet content clone-for
                nth-of-type first-child do-> set-attr sniptest at emit*]])
  (:require [cmdsync.tmpls :as tmpl]))

(deftemplate landing-template "html/landing.html"
  []
  [:title] (content "CmdSync"))

(defn show-enlive-landing [req]
  {:body (landing-template)
   :status 200})

(defn show-landing [req]
  (tmpl/landing {:user-agent (get-in req [:headers "user-agent"])
                 :title "Welcome to cmdsync!"
                 :list ["Migrate README."
                        "Add Title Page!"]
                 :funny-url "<a href='http://www.nyan.cat/dub.php'>Nyan Cat</a>"
                 :funny-url-name "Lulz Synchrotron!"}))

(defn show-tabspire-docs [req]
  (tmpl/docs {:user-agent (get-in req [:headers "user-agent"])
                 :title "Welcome to cmdsync!"
                 :list ["Migrate README."
                        "Add Title Page!"]
                 :funny-url "<a href='http://www.nyan.cat/dub.php'>Nyan Cat</a>"
                 :funny-url-name "Lulz Synchrotron!"}))
