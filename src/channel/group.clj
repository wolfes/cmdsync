(ns channel.group
  (:use [org.httpkit.server :only [open? send! on-receive on-close]]
        [lamina.core :only [channel channel? receive-all enqueue]])
  (:require [clojure.data.json :as json]))

; Group channel bookkeeping methods.
; Allows multiple clients to listen to a unique channel name.

; Map group-channel-name to group channel info.
(def ^:private group-channels (atom {}))

(defn group-channel-with-name? [channel-name]
  "Returns boolean for existance of group channel with channel-name."
  (contains? @group-channels (keyword channel-name)))

(defn get-group-channel-by-name [channel-name]
  "Returns group channel for channel-name if one exists, else nil."
  (-> @group-channels ((keyword channel-name)) :channel))

(defn- ensure-group-channel-exists [channel-name]
  "Creates new group (lamina) channel if none exist for channel-name."
  (dosync
    (when-not (group-channel-with-name? channel-name)
      (swap! group-channels
             (fn [grp-channels]
               (conj grp-channels
                     [(keyword channel-name)
                      {:channel (channel)}]))))))

(defn join-group-channel-by-name [channel-name req-channel]
  "Adds channel as listener to the group channel with channel-name."
  (ensure-group-channel-exists channel-name)
  (when-let [group-channel (get-group-channel-by-name channel-name)]
    ; Send data from client request's channel to group-channel to be heard by all.
    ; Feature unimplemented on client.
    ;(on-receive req-channel (fn [data] (enqueue group-channel data)))
    (receive-all group-channel (fn [data] (send! req-channel data)))))

(defn send-msg-to-group-channel [group-channel msg]
  (when (channel? group-channel)
    (enqueue group-channel msg)))

; TODO: Replace usage of above method with this.
(defn send-group-msg-by-name [group-name msg]
  "Jsonify and send msg to group with group-name."
  (when-let [group-channel (get-group-channel-by-name group-name)]
    (send-msg-to-group-channel group-channel (json/write-str msg))))
