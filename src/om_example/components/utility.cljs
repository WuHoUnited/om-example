(ns om-example.components.utility
  "This namespace contains utility functionality which can be used by other components."
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]

            [sablono.core :as html :refer-macros [html]]))


;; Begin editable component.
;; The editable component will be a thin wrapper of a text input field,
;; which can do a very simple bit of validation.
;; The magic of the field will be in the CSS.

(defn- handle-change
  "function to call whenever the value for the editable component is attempted to be changed.
  e is the event, data is the cursor to update, edit-key is the key within data,
  handle-fn is a function which will be called on the new value to validate/change it (For instance we are going to only want
  numbers in certain fields)."
  [e owner data edit-key handle-fn]
  (let [handle-fn (or handle-fn identity)
        new-val (handle-fn (.. e -target -value))]
    (om/update! data edit-key new-val)
    (om/refresh! owner))) ; We have to tell the text input explicitly to refresh

(defn editable
  "generates a component which allows editing of a single value.
  edit-key is the key into data which should be used to update the value.
  handle-fn is a function which will be called on the value before it is updated."
  [{:keys [edit-key handle-fn data]} owner]
  (reify
    om/IRender
    (render [_]
            (let [text (edit-key data)]
              (dom/input
               #js {:value text
                    :onChange #(handle-change % owner data edit-key handle-fn)
                    :className "editable"})))))
