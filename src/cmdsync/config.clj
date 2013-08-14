(ns cmdsync.config)

(defonce app-configs (atom {:profile :dev}))

(defn cfg 
  "Look up app config by key.  Optionally provide default value if not found."
  [key & [default]]
  (if-let [v (or (key @app-configs) default)]
    v
    (when-not (contains? @app-configs key)
      (throw (RuntimeException. (str "unknow config for key " (name key)))))))
