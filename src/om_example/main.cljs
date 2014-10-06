(ns om-example.main
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.core :as c]

            [om-example.components.edit-table :as edit-table]
            [om-example.components.bar-chart :as bar-chart]
            [om-example.components.simple-table :as simple-table]
            ))

(enable-console-print!)

(def DEFAULT_DATA
  (->> (map c/->Datum
            ["Maine" "New Hampshire" "Vertmont" "Massachusetts" "New York" "Rhode Island" "Connecticut"]
            [4 4 3 11 29 4 7])
       (sort-by (comp - :value))
       vec))

(def INSTRUCTIONS
  "This page is interactive. Some text boxes such as those in the table are editable.
  If you click on them, they will turn into text fields which you can edit.
  Notice how changes to the fields are reflected immediately and automatically in other parts of the page.")

(def app-state (atom {:data DEFAULT_DATA}))

(defn instruction-view [{:keys [text]} owner]
  (om/component
   (html [:div.instructions
          INSTRUCTIONS])))

(defn app-view [data owner]
  (om/component
   (html [:div
          (om/build instruction-view {:text INSTRUCTIONS})
          (om/build edit-table/edit-table data)
          (om/build simple-table/simple-table data)
          (om/build bar-chart/bar-chart data)
          ])))

(om/root
 app-view
 app-state
 {:target (. js/document (getElementById "app"))})
