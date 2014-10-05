(ns om-example.components.table
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.components.utility :as u]))

(defn to-float [s]
  (println s)
  (let [n (js/parseFloat s)]
    (println n)
    (println (js/isNaN n))
    (if (js/isNaN n)
      0
      n)))

(defn table
  "Generates a table view of data"
  [{:keys [data]}]
  (om/component
   (html
    [:table
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

