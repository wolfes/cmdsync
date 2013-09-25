(ns channel.private
  (:use [org.httpkit.server :only [open? send!]]
        [lamina.core :only [siphon]])
  (:require [clojure.data.json :as json]))

; Private channel bookkeeping methods.
; Enforces one client per unique channel name.

; Map private-channel-name to private channel info.
(def private-channels (atom {}))

(defn get-private-channel-by-name [channel-name]
  "Returns private channel for channel-name if one exists, else nil."
  (println "get-private-channel-by-name" channel-name)
  (-> @private-channels ((keyword channel-name)) :channel))

(defn private-channel-with-name? [channel-name]
  "Returns boolean for existance of open private channel with channel-name."
  (println "private-channel-with-name?" channel-name)
  (when-let [private-channel (get-private-channel-by-name channel-name)]
    (open? private-channel)))

(defn get-private-channels []
  private-channels)

(defn set-private-channel-by-name [channel-name channel]
  "Upserts private (httpkit) channel with channel-name."
  (println "set-priv-channel-by-name" (keyword channel-name) channel)
  (swap! private-channels
         (fn [priv-channels]
           (assoc priv-channels
                  (keyword channel-name)
                  {:channel channel}))))

(defn remove-private-channel-by-name [channel-name]
  "Removes private (httpkit) channel with channel-name."
  (println "remove-private-channel-by-name" channel-name)
    (swap! private-channels
           (fn [priv-channels]
             (dissoc priv-channels (keyword channel-name)))))

(defn send-msg-to-private-channel [private-channel msg]
  "Send json encoded message to private channel."
  (println "send-msg-to-private-channel" private-channel msg)
  (when (open? private-channel)
    (send! private-channel msg)))

(defn send-msg-to-private-channel-by-name [private-channel-name msg]
  "Json encode message and send to private channel by name."
  (when-let [private-channel (get-private-channel-by-name private-channel-name)]
    (when (open? private-channel)
      (send! private-channel (json/write-str msg)))))
