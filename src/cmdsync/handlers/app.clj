(ns cmdsync.handlers.app
  (:use [net.cgrand.enlive-html
         :only [deftemplate defsnippet content clone-for nth-child
                nth-of-type first-child do-> set-attr sniptest at emit*]])
  (:require [cmdsync.tmpls :as tmpl]))


;; Enlive

(def header-buttons
  [{:text "Tabspire" :href "/tabspire/docs"}
   {:text "Vimspire" :href "/vimspire/docs"}])
   ;{:text "About" :href "/about/docs"}
   ;{:text "Contact" :href "/contact/docs"}])

;; Snippet for nav-bar
;; name snipper-html-source selector-gets-only-part-of-source-html
(defsnippet nav-bar-snip "html/nav-bar.html" [:#navbar-inner]
  []

  [:#main-nav-links :li]
  (clone-for [btn header-buttons]
             [:a]
             (do-> (content (:text btn))
                   (set-attr :href (str (:href btn)))))
  )

(defsnippet docs-tabspire "html/tabspire-readme.html" [:body]
  [])

(defsnippet docs-vimspire "html/vimspire-readme.html" [:body]
  [])

(deftemplate landing-template "html/landing.html"
  []
  [:title] (content "CmdSync")
  ;; Where we find dom elt with id navbar, replace content with snippet.
  [:#navbar] (content (nav-bar-snip))
  [:#main-nav-links (nth-child 1)] (set-attr :class "active"))

(deftemplate tabspire-docs-template "html/landing.html"
  []
  [:title] (content "Tabspire")
  [:#navbar] (content (nav-bar-snip))
  [:#main-nav-links (nth-child 2)] (set-attr :class "active")
  [:#main-content] (content (docs-tabspire)))

(deftemplate vimspire-docs-template "html/landing.html"
  []
  [:title] (content "Vimspire")
  [:#navbar] (content (nav-bar-snip))
  [:#main-nav-links (nth-child 3)] (set-attr :class "active")
  [:#main-content] (content (docs-vimspire)))

(defn show-enlive-landing [req]
  {:body (landing-template)
   :status 200})

(defn show-docs-tabspire [req]
  (let [landing (tabspire-docs-template)]
  {:body landing
   :status 200}))

(defn show-docs-vimspire [req]
  (let [landing (vimspire-docs-template)]
  {:body landing
   :status 200}))

;; Mustache
;(defn show-landing [req]
  ;(tmpl/landing {:user-agent (get-in req [:headers "user-agent"])
                 ;:title "Welcome to cmdsync!"
                 ;:list ["Migrate README."
                        ;"Add Title Page!"]
                 ;:funny-url "<a href='http://www.nyan.cat/dub.php'>Nyan Cat</a>"
                 ;:funny-url-name "Lulz Synchrotron!"}))

;(defn show-tabspire-docs [req]
  ;(tmpl/docs {:user-agent (get-in req [:headers "user-agent"])
                 ;:title "Welcome to cmdsync!"
                 ;:list ["Migrate README."
                        ;"Add Title Page!"]
                 ;:funny-url "<a href='http://www.nyan.cat/dub.php'>Nyan Cat</a>"
                 ;:funny-url-name "Lulz Synchrotron!"}))
