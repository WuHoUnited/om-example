(ns om-example.components.utility
  "This namespace contains utility functionality which can be used by other components."
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]

            [sablono.core :as html :refer-macros [html]]))

;; Define an editable component which can display like a span or a text-box.
;; It is based closely on David Nolen's editable component from the Basic Om tutorial,
;; It initial displays with a span and button. When the button is clicked, both disappear
;; and a textbox appears. When the textbox loses focus, it goes back to looking how it did initially.
;; however this component has some problems and I believe that it could be better implemented
;; using just a text box and swapping the CSS to make it look like a span when it isn't being edited.

(defn- display
  "This simple function returns a js map to use for CSS for
  an element based on whether it should be displayed (show)."
  [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn- end-edit
  "function to call when the editable component is going from edit mode to
  non-edit mode."
  [owner]
  (om/set-state! owner :editing false))

(defn- begin-edit
  "function to call when hte editable component is going to be edited."
  [owner]
  (om/set-state! owner :editing true)
  (js/setTimeout #(.focus (om/get-node owner "input")) 0)) ; The timeout is because we can't seem to focus a hidden element.

(defn- handle-change
  "function to call whenever the value for the editable component is attempted to be changed.
  e is the event, data is the cursor to update, edit-key is the key within data,
  handle-fn is a function which will be called on the new value to validate/change it (For instance we are going to only want
  numbers in certain fields)."
  [e data edit-key handle-fn]
  (let [handle-fn (or handle-fn identity)
        new-val (handle-fn (.. e -target -value))]
    (om/transact! data edit-key (constantly new-val))))

(defn editable
  "generates a component which allows editing of a single value.
  edit-key is the key into data which should be used to update the value.
  handle-fn is a function which will be called on the value before it is updated."
  [data owner {:keys [edit-key handle-fn] :as opts}]
  (reify
    om/IInitState
    (init-state [_]
                {:editing false})
    om/IRenderState
    (render-state [_ {:keys [editing]}]
                  (let [text (get data edit-key)]
                    (dom/span nil
                              (dom/span #js {:style (display (not editing))} text)
                              (dom/input
                               #js {:style (display editing)
                                    :value text
                                    :onChange #(handle-change % data edit-key handle-fn)
                                    :onKeyDown #(when (== (.-keyCode %) 13)
                                                  (end-edit owner))
                                    :onBlur (fn [e]
                                              (when (om/get-state owner :editing)
                                                (end-edit owner)))
                                    :ref "input"})
                              (dom/button
                               #js {:style (display (not editing))
                                    :onClick #(begin-edit owner) }
                               "Edit"))))))
