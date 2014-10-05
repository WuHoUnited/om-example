(ns om-example.components.utility
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]

            [sablono.core :as html :refer-macros [html]]))


(defn display [show]
  (if show
    #js {}
    #js {:display "none"}))

(defn end-edit [text owner]
  (om/set-state! owner :editing false))

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
                                  :onKeyPress #(when (== (.-keyCode %) 13)
                                                 (end-edit text owner))
                                  :onBlur (fn [e]
                                            (when (om/get-state owner :editing)
                                              (end-edit text owner)))})
                            (dom/button
                             #js {:style (display (not editing))
                                  :onClick #(om/set-state! owner :editing true)}
                             "Edit"))))))
