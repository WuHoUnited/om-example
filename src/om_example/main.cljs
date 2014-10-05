(ns om-example.main
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.core :as c]

            [om-example.components.table :as table]
            [om-example.components.bar :as bar]))

(enable-console-print!)

(def DEFAULT_DATA (mapv c/->Datum
                        ["New York" "Virginia" "Oklahoma"]
                        [20 30 5]))

(def app-state (atom {:data DEFAULT_DATA}))

(defn app-view [data owner]
  (om/component
   (html [:div
          (om/build table/table data)
          (om/build bar/bar data)
          ])))

(om/root
 ;table/table
 ;bar/bar
 app-view
 app-state
 {:target (. js/document (getElementById "app"))})
