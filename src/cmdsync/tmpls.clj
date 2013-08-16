(ns cmdsync.tmpls
  (:use [cmdsync.config :only [cfg]]
        [net.cgrand.enlive-html
         :only [deftemplate defsnippet content clone-for nth-child
                nth-of-type first-child do-> set-attr sniptest at emit*]]
        [me.shenfeng.mustache :only [gen-tmpls-from-resources]])
  (:require [cmdsync.snips :as snip]))

;; Enlive Templates.

(deftemplate landing "tmpls/landing.html"
  []
  [:title] (content "CmdSync")
  ;; Where we find dom elt with id navbar, replace content with snippet.
  [:#navbar] (content (snip/nav-bar-snip))
  [:#main-nav-links (nth-child 0)] (set-attr :class "active"))

(deftemplate tabspire-docs "tmpls/landing.html"
  []
  [:title] (content "Tabspire")
  [:#navbar] (content (snip/nav-bar-snip))
  [:#main-nav-links (nth-child 1)] (set-attr :class "active")
  [:#main-content] (content (snip/docs-tabspire)))

(deftemplate vimspire-docs "tmpls/landing.html"
  []
  [:title] (content "Vimspire")
  [:#navbar] (content (snip/nav-bar-snip))
  [:#main-nav-links (nth-child 2)] (set-attr :class "active")
  [:#main-content] (content (snip/docs-vimspire)))


;; Mustache by me.shenfeng

(defn add-info [data]
  (assoc data
    :dev? (= (cfg :profile) :dev)
    :prod? (= (cfg :profile) :prod)))

;; Generate clojure functions from src/templates folder.
(gen-tmpls-from-resources "templates" [".tpl"] add-info)
