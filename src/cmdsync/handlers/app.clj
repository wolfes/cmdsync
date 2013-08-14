(ns cmdsync.handlers.app
  (:require [cmdsync.tmpls :as tmpl]))

(defn show-landing [req]
  (tmpl/landing {:user-agent (get-in req [:headers "user-agent"])
                 :title "Welcome to cmdsync!"
                 :list ["Migrate README."
                        "Add Title Page!"]
                 :funny-url "<a href='http://www.nyan.cat/dub.php'>Nyan Cat</a>"
                 :funny-url-name "Lulz Synchrotron!"}))
