(ns om-example.components.edit-table
  "Contains the definition of a component which allows us to view the data
  tabularly, but allows us to edit the data."
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.components.utility :as u]))

(defn- to-float
  "converts a String s to a float.
  If s cannot be converted then 0 is returned.
  Perhaps nil would be a better choice."
  [s]
  (let [n (js/parseFloat s)]
    (if (js/isNaN n)
      0
      n)))


;; Note how the editable component is re-used.
;; Also note the similarity between edit-table and simple-table.
;; Could you see how to create a new function which would remove the need for
;; having an edit-table and simple-table function?
(defn edit-table
  "Generates an om component which contains an editable view of our data."
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
                      [:td (om/build u/editable d {:opts
                                                   {:edit-key :name}})]
                      [:td (om/build u/editable d {:opts
                                                   {:edit-key :value
                                                    :handle-fn to-float}})]])
                   data)]])))
