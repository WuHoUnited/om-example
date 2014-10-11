(ns om-example.components.edit-table
  "Contains the definition of a component which allows us to view the data
  tabularly, but allows us to edit the data."
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.components.utility :as u]))

;; Note how the editable component is re-used.
;; Also note the similarity between edit-table and simple-table.
;; Could you see how to create a new function which would remove the need for
;; having an edit-table and simple-table function?
(defn edit-table
  "Generates an om component which contains an editable view of our data."
  [{:keys [data]}]
  (om/component
   (html
    [:table.edit-table
     [:thead
      [:tr
       [:th "Name"]
       [:th "Value"]]]
     [:tbody
      (map-indexed (fn [index {:keys [name value] :as d}]
                     [:tr {:key index}
                      [:td (om/build u/editable {:data d
                                                 :edit-key :name})]
                      [:td (om/build u/editable {:data d
                                                 :edit-key :value
                                                 :handle-fn u/to-float})]])
                   data)]])))
