(ns om-example.components.simple-table
  "Holds the definition of a simple read-only Om table component
  for rendering our data."
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]))

(defn simple-table
  "Generates a read-only tabular view of data.
  data must be a vector with name and value keys."
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
