(ns om-example.core)

(defprotocol IDatum
  (get-name [this])
  (get-value [this]))

(defrecord Datum [name value]
  IDatum
  (get-name [this] name)
  (get-value [this] value))

(def DEFAULT_DATA (mapv ->Datum
                        ["New York" "Virginia" "Oklahoma"]
                        [20 30 5]))
