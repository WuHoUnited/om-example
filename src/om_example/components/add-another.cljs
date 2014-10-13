(ns om-example.components.add-another
  "Namespace for a component which will add a new value to an array."
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]
            ))

(defn add-another
  "Component which will add a new value to the end of an array.
  value will be added to coll when clicked."
  [{:keys [coll value]}]
  (om/component
   (html [:button.add
          {:onClick (fn [_]
                      (om/transact! coll (fn [coll]
                                           (conj coll value))))}
          "Add Another"])))
