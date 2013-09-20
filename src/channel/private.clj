(ns channel.private
  (:use [org.httpkit.server :only [open? send!]]
        [lamina.core :only [siphon]]))

; Private channel bookkeeping methods.
; Enforces one client per unique channel name.

; Map private-channel-name to private channel info.
(def private-channels (atom {}))

(defn private-channel-with-name? [channel-name]
  "Returns boolean for existance of private channel with channel-name."
  (println "private-channel-with-name?" channel-name)
  (contains? @private-channels (keyword channel-name)))

(defn get-private-channel-by-name [channel-name]
  "Returns private channel for channel-name if one exists, else nil."
  (println "get-private-channel-by-name" channel-name)
  (-> @private-channels ((keyword channel-name)) :channel))

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
  (println "send-msg-to-private-channel" private-channel msg)
  (when (open? private-channel)
    (send! private-channel msg)))
