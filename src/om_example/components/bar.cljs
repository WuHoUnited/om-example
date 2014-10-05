(ns om-example.components.bar
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.core :as c]))

(enable-console-print!)

(def WIDTH 100)
(def HEIGHT 100)
(def X_AXIS_OFFSET 10)
(def Y_AXIS_OFFSET 33)

(defn get-max-value [data]
  (:value (apply max-key :value data)))

(defn add-scale [data]
  (let [max-value (get-max-value data)]
    (mapv (fn [{:keys [value] :as d}]
            (assoc-in d [::scaled-value] (/ value max-value)))
          data)))

(defn draw-bars [data]
  (let [scaled-data (add-scale data)
        working-width (- WIDTH Y_AXIS_OFFSET)
        working-height (- HEIGHT X_AXIS_OFFSET)
        bar-height (/ working-height
                      (+ 1
                         (* 2 (count data))))
        double-bar-height (* bar-height 2)]
    (map (fn [{:keys [name value] scaled-value ::scaled-value} top]
           (list [:rect.bar {:x Y_AXIS_OFFSET
                             :y top
                             :width (* working-width scaled-value)
                             :height bar-height}]
                 [:text.bar-name {:x 0
                                  :y (+ top bar-height -5) }
                  name]))
         scaled-data
         (iterate #(+ double-bar-height %) bar-height))))

(defn draw-x-axis []
  [:line.bar-axis {:x1 X_AXIS_OFFSET
                   :x2 WIDTH
                   :y1 Y_AXIS_OFFSET
                   :y2 Y_AXIS_OFFSET}])

(defn draw-y-axis []
  [:line.bar-axis {:x1 Y_AXIS_OFFSET
                   :x2 Y_AXIS_OFFSET
                   :y1 0
                   :y2 HEIGHT}])

(defn bar [{:keys [data]}]
  (om/component
   (html
    [:svg {;:width WIDTH
           ;:height HEIGHT
           :viewBox (apply str (interpose \space [0 0 WIDTH HEIGHT]))
           :preserveAspectRatio "none"
           :className "bar-graph"}
     ;(draw-x-axis)
     (draw-bars data)
     (draw-y-axis)])))
