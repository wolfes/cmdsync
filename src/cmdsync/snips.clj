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

(defsnippet nav-bar "snips/nav-bar.html" [:#navbar-inner]
  [ctx]

  [:#main-nav-links :li]
  (clone-for [btn header-buttons]
             [:a]
             (do-> (content (:text btn))
                   (set-attr :href (:href btn))))

  [:#main-nav-links (nth-child (:current-link-idx ctx))]
  (set-attr :class "active")
  )


(defsnippet home-main-content  "snips/main-content.html" [:body]
  [])

(defsnippet docs-tabspire "html/tabspire-readme.html" [:body]
  [])

(defsnippet docs-vimspire "html/vimspire-readme.html" [:body]
  [])
