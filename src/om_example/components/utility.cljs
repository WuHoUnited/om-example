(ns om-example.components.utility
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]

            [sablono.core :as html :refer-macros [html]]))

(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn end-edit [owner]
  (om/set-state! owner :editing false))

(defn begin-edit [owner]
  (om/set-state! owner :editing true)
  (js/setTimeout #(.focus (om/get-node owner "input")) 0))

(defn handle-change [e data edit-key handle-fn owner]
  (let [handle-fn (or handle-fn identity)
        new-val (handle-fn (.. e -target -value))]
    (om/transact! data edit-key (constantly new-val))))

(defn editable [data owner {:keys [edit-key handle-fn] :as opts}]
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
                                    :onChange #(handle-change % data edit-key handle-fn owner)
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
