(ns cmdsync.test.handler
  (:use clojure.test
        ring.mock.request  
        channel.private)
  (:require [lamina.core :only [channel channel? receive-all enqueue]]))


(deftest test-private-channel
  (testing "create private channel"
    (let [channel-name "foo"]
    (set-private-channel-by-name channel-name (channel))
    (is (private-channel-with-name? channel-name)))))
