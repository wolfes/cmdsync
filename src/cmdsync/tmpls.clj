(ns cmdsync.tmpls
  (:use [cmdsync.config :only [cfg]]
        [net.cgrand.enlive-html
         :only [deftemplate defsnippet content substitute clone-for nth-child
                nth-of-type first-child do-> set-attr sniptest at emit*]]
        [me.shenfeng.mustache :only [gen-tmpls-from-resources]])
  (:require [cmdsync.snips :as snip]
            [me.raynes.laser :as l]
            [clojure.java.io :refer [file]]))

;; Laser by Raynes

(l/defdocument laser-landing (file "src/tmpls/landing.html")
  []
  (l/element= "title") (l/content "LASER")
  (l/id= "navbar") (l/content (snip/laser-nav-bar {}))

  (l/id= "main-content")
  ;(l/content (l/fragment (l/parse-fragment (file "src/snips/main-content.tpl")))))
  (l/content (snip/laser-main-content {})))


;; Enlive Templates.

(deftemplate landing "tmpls/landing.html"
  []
  [:title] (content "CmdSync")
  ;; Where we find dom elt with id navbar, replace content with snippet.
  [:#navbar] (content (snip/nav-bar {:current-link-idx 0}))
  [:#main-content] (content (snip/home-main-content)))

(deftemplate tabspire-docs "tmpls/landing.html"
  []
  [:title] (content "Tabspire")
  [:#navbar] (content (snip/nav-bar {:current-link-idx 1}))
  [:#main-content] (content (snip/docs-tabspire)))

(deftemplate vimspire-docs "tmpls/landing.html"
  []
  [:title] (content "Vimspire")
  [:#navbar] (content (snip/nav-bar {:current-link-idx 2}))
  [:#main-content] (content (snip/docs-vimspire)))


;; Mustache by me.shenfeng

(defn add-info [data]
  (assoc data
    :dev? (= (cfg :profile) :dev)
    :prod? (= (cfg :profile) :prod)))

;; Generate clojure functions from src/templates folder.
;(gen-tmpls-from-resources "templates" [".tpl"] add-info)
