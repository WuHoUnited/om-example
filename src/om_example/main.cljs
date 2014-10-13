(ns om-example.main
  "Namespace with the main entry point.
  This namespace will be responsible for instantiating new data and
  beginning to render to the DOM."
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.core :as c]

            [om-example.components.edit-table :as edit-table]
            [om-example.components.bar-chart :as bar-chart]
            [om-example.components.html-bar-chart :as html-bar-chart]
            [om-example.components.simple-table :as simple-table]
            [om-example.components.add-another :as add-another]
            ))

;; Standard setup of contants

(enable-console-print!)

(def ^:const DEFAULT_DATA
  "This represents the initial data to be rendered.
  Currently it holds the number of electoral college votes for the Northeastern states
  sorted by number/name."
  (->> (map c/->Datum
            ["Maine" "New Hampshire" "Vermont" "Massachusetts" "New York" "Rhode Island" "Connecticut"]
            [4 4 3 11 29 4 7])
       (sort-by (fn [d] [((comp - :value) d) (:name d)]))
       vec))

(def ^:const INSTRUCTIONS
  "Instructions for users viewing the screen."
  "This page is interactive. Some text boxes such as those in one of the tables are editable.
  If you click on them, they will turn into text fields which you can edit.
  Notice how changes to the fields are reflected immediately and automatically in other parts of the page.")

(def app-state
  "This will hold the entire state of the application and is what
  Om will reference."
  (atom {:data DEFAULT_DATA}))

;; Setup Main DOM views

(defn instruction-view
  "A component which contains the instructions for the user viewing the web-page."
  [{:keys [text]} owner]
  (om/component
   (html [:div.instructions
          INSTRUCTIONS])))

;; app-view takes in the app-state. Notice how the app-state passes
;; The exact same data value to the edit-table, simple-table and bar-chart components.
(defn app-view
  "Overall view which contains everything."
  [data owner]
  (om/component
   (html [:div
          (om/build instruction-view {:text INSTRUCTIONS})
          (om/build html-bar-chart/html-bar-chart data)
          (om/build add-another/add-another
                    {:coll (:data data)
                     :value c/default-datum})
          (om/build edit-table/edit-table data)
          (om/build simple-table/simple-table data)
          (om/build bar-chart/bar-chart data)
          ])))

;; Begin Rendering to the DOM

(om/root
 app-view
 app-state
 {:target (. js/document (getElementById "app"))})

