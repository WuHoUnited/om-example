(ns om-example.components.simple-table
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]))

(defn simple-table
  "Generates a table view of data"
  [{:keys [data]}]
  (om/component
   (html
    [:table.table
     [:thead
      [:tr
       [:th "Name"]
       [:th "Value"]]]
     [:tbody
      (map-indexed (fn [index {:keys [name value] :as d}]
                     [:tr {:key index}
                      [:td name]
                      [:td value]])
                   data)]])))
