(ns om-example.core
  "This namespace will contain the \"core\" functionality related to data.
  It is a fairly uninteresting namespace.")

(defprotocol IDatum
  "This protocol is used to represent a datum.
  It is not yet used, but I would like to have the views use these
  methods instead of keywords and then demonstrate representing the data as
  a vector with the name in the first slot and value in the second alongside using
  a Record."
  (get-name [this] "Retrieve the name of a piece of datum.")
  (get-value [this] "Retrieve the value associated with a piece of datum."))

(defrecord Datum [name value]
  IDatum
  (get-name [this] name)
  (get-value [this] value))
