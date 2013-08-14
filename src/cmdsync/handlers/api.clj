(ns cmdsync.handlers.api
  (:use [org.httpkit.server :only [run-server with-channel websocket? open? send! on-receive on-close]]
        [clojure.repl :only [doc source]]
        [compojure.handler :only [site]]
        [compojure.core :only [defroutes GET POST]]
        [lamina.core :only [siphon channel? enqueue]]
        [channel.private :only 
         [private-channel-with-name?
          get-private-channel-by-name 
          set-private-channel-by-name
          send-msg-to-private-channel]]
         [channel.group :only
          [join-group-channel-by-name
           get-group-channel-by-name
           group-channel-with-name?
           send-msg-to-group-channel]])
  (:require [ring.middleware.reload :as reload]
            [compojure.route :as route]
            [clojure.data.json :as json]
            ;; [cmdsync.tmpls :as tmpl]
            [channel.private :as channel-private]))

;; Helper API Route Handlers

(defn process-existing-private-channel [ch channel-name]
  "Overwrite existing private channel for channel-name, grand theft channel style."
  ;(siphon ch ch) ; Echo incoming messages to sender, for client testing.
  (println "process-existing-private-channel")
  (set-private-channel-by-name channel-name ch))

(defn process-new-private-channel [ch channel-name]
  "Store a new private channel by name."
  (println "process-new-private-channel")
  (set-private-channel-by-name channel-name ch))

(defmulti process-websocket-request
  "Process websocket request by type of command."
  (fn [req req-channel channel-name cmd]
    (keyword cmd)))

(defmethod process-websocket-request :join-private [req req-channel channel-name cmd]
  "Add requestor's channel in private channel namespace."
  (println "process-websocket-request > join-private channel-name: " channel-name)
  (if (private-channel-with-name? channel-name)
    (process-existing-private-channel req-channel channel-name)
    (process-new-private-channel req-channel channel-name)))

(defmethod process-websocket-request :join-group [req req-channel channel-name cmd]
  "Add requestor's channel to listen to named channel group."
  (println "process-websocket-request > join-group channel-name: " channel-name)
  (join-group-channel-by-name channel-name req-channel))

(defn process-api-request-to-channel [req channel-messenger]
  "Process an API request for a target channel.
  Args:
    channel-messenger: Fn that sends [data] to a channel."
  (let [params (:route-params req)
        {:keys [channel-name cmd]} params
        cmd-data (:form-params req)
        tabspire-command (json/write-str
                           {:command cmd
                            :command-data cmd-data
                            :channel-name channel-name})]
    (println "API Request: (cmd: " cmd ", form-params: " (:form-params req) ").")
    (channel-messenger tabspire-command)
    {:status 200 :headers {"content-type" "text/html"}
     :body tabspire-command}))


;; Primary API Route Handlers

(defmulti route-tabspire-api-post
  "Route POST request to Tabspire API to the appropriate handler."
  (fn [req]
    "Returns request param's channel-type if available, else :private."
    (keyword (get (:form-params req) "channel-type" :private))))

(defmethod route-tabspire-api-post :private [req]
  "Process tabspire api request for a private channel."
  (let [channel-name (-> req :route-params :channel-name)
        target-channel (get-private-channel-by-name channel-name)]
    (process-api-request-to-channel req (partial send-msg-to-private-channel target-channel))))

(defmethod route-tabspire-api-post :group [req]
  "Process tabspire api request for a group channel."
  (let [channel-name (-> req :route-params :channel-name)
        target-channel (get-group-channel-by-name channel-name)]
    (println "\nTabspire API Post Cmd >> group channel: " channel-name)
    (process-api-request-to-channel req (partial send-msg-to-group-channel target-channel))))

(defn print-route-tabspire-cmd [is-websocket-request? channel-name cmd req]
    (println "\nTabspire API >> Type:"
             (if is-websocket-request? "Websocket" "HTTP API")
             "-- Channel:" channel-name
             "-- Cmd:" cmd
             "-- Remote Addr:" (:remote-addr req)))

(defn route-tabspire-cmd 
  "Process websocket connection/naming."
  [req]
  (println "route-tabspire-cmd")
  (let [params (:route-params req)
        {:keys [channel-name cmd]} params]
    (with-channel req req-channel
      (on-close req-channel (fn [status] (println "Channel Closed:" status)))
      (print-route-tabspire-cmd (websocket? req-channel) channel-name cmd req)
      (if (websocket? req-channel)
        (process-websocket-request req req-channel channel-name cmd)))))
