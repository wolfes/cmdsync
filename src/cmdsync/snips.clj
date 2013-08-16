(ns cmdsync.snips
  (:use [cmdsync.config :only [cfg]]
        [net.cgrand.enlive-html
         :only [deftemplate defsnippet content clone-for nth-child
                nth-of-type first-child do-> set-attr sniptest at emit*]]))

;; Enlive Snippets.

(def header-buttons
  [{:text "Tabspire" :href "/tabspire/docs"}
   {:text "Vimspire" :href "/vimspire/docs"}])
   ;{:text "About" :href "/about/docs"}
   ;{:text "Contact" :href "/contact/docs"}])

(defsnippet nav-bar-snip "html/nav-bar.html" [:#navbar-inner]
  []

  [:#main-nav-links :li]
  (clone-for [btn header-buttons]
             [:a]
             (do-> (content (:text btn))
                   (set-attr :href (str (:href btn))))))

(defsnippet docs-tabspire "html/tabspire-readme.html" [:body]
  [])

(defsnippet docs-vimspire "html/vimspire-readme.html" [:body]
  [])
