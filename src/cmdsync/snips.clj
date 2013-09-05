(ns cmdsync.snips
  (:use [cmdsync.config :only [cfg]]
        [net.cgrand.enlive-html
         :only [deftemplate defsnippet content clone-for nth-child
                nth-of-type first-child do-> set-attr sniptest at emit*]])
  (:require [clojure.java.io :refer [file]]
            [me.raynes.laser :as l]))

(def header-buttons
  [{:text "Tabspire" :href "/tabspire/docs"}
   {:text "Vimspire" :href "/vimspire/docs"}])

;; Laser Fragments.
;; Note: defragment must contain 1+ selector/transform.

; Node example matching "#main-nav-links :li"
;{
 ;:type :element,
 ;:attrs nil,
 ;:tag :li,
 ;:content [{
            ;:type :element,
            ;:attrs {:href /},
            ;:tag :a,
            ;:content [The Restaurant at the End of the World]}]}

(l/defragment laser-nav-bar (file "src/snips/nav-bar.tpl")
  [ctx]

  (l/child-of
    (l/id= "main-nav-links")
    (l/element= :li))
  (fn [node]
    (for [btn header-buttons]
      (l/at node ; At nodes "#main-nav-links > :li", apply sel/xform.
            (l/element= :a)
            #(l/on % ; Apply transforms to node matching selector.
                (l/content (:text btn))
                (l/attr :href (:href btn)))))))

    ; Also works, but requires hackish index to get first example li element.
    ;(for [btn header-buttons]
      ;(-> node
          ;(assoc-in [:content 0 :attrs :href] (:href btn))
          ;(assoc-in [:content 0 :content] [(:text btn)]))
      ;)))

(l/defragment laser-main-content (file "src/snips/main-content.tpl")
  [ctx]

  (l/id= "main-content-container")
  (l/insert (l/fragment (l/parse-fragment (file "src/snips/project-section.tpl"))) :right))

;; Enlive Snippets.

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


(defsnippet home-main-content  "snips/main-content.tpl" [:body]
  [])

(defsnippet docs-tabspire "html/tabspire-readme.html" [:body]
  [])

(defsnippet docs-vimspire "html/vimspire-readme.html" [:body]
  [])
