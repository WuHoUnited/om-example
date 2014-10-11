(ns om-example.components.html-bar-chart
  "Generates an HTML bar-chart component for viewing our data."
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.core :as c]
            [om-example.components.utility :as u]))


;; The overall plan for this component is that it will be rendered using table style CSS.
;; This will allow the left and right-hand columns to maintain the same width.

(defn- build-row
  "Build a single row of the 'table'"
  [{:keys [key]
    {:keys [name value]
     scaled-value :om-example.components.utility/scaled-value :as datum} :datum}]
  [:div.html-bar-row {:key key}
   [:div.html-bar-first (om/build u/editable {:edit-key :name
                                              :data datum})]
   [:div.html-bar-second
    [:div.html-bar
     {:style {:width (str (* scaled-value 100)
                          "%")}}
     (om/build u/editable {:edit-key :value
                           :data datum
                           :handle-fn u/to-float})]]])

(defn html-bar-chart
  "Returns an om-component which creates an HTML bar-chart view of our data
  which is editable."
  [{:keys [data]}]
  (om/component
   (html
    (let [scaled-data (u/add-scale data)]
      [:div.html-bar-table
       [:div.html-bar-body
        (map-indexed (fn [index datum]
                       (build-row {:key index
                                   :datum datum})) scaled-data)]]))))
