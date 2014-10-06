(ns om-example.components.bar-chart
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.core :as c]))

(enable-console-print!)

(def BAR_HEIGHT 10)
(def BAR_SPACING 5)

(def CANVAS_WIDTH 600)

(def LETTER_WIDTH 3.6)
(def LETTER_BASE 0.2)

(def TEXT_VERTICAL_OFFSET -3)
(def VALUE_TEXT_HORIZONTAL_OFFSET 1)

(defn get-max-value [data]
  (:value (apply max-key :value data)))

(defn calculate-height [data]
  (let [bars (count data)]
    (+ (* bars BAR_HEIGHT)
       (* (inc bars) BAR_SPACING))))

(defn draw-y-axis [data]
  [:line.bar-axis {:x1 0
                   :x2 0
                   :y1 0
                   :y2 (calculate-height data)}])

(defn calculate-word-width [data]
  (let [search-fn (comp count :name)
        longest-val (apply max-key search-fn data)
        longest-length (search-fn longest-val)]
    (+ LETTER_BASE
       (* LETTER_WIDTH longest-length))))

(defn add-scale [data]
  (let [max-value (get-max-value data)]
    (mapv (fn [{:keys [value] :as d}]
            (assoc-in d [::scaled-value] (/ value max-value)))
          data)))

(defn draw-bars [{:keys [data axis-offset]}]
  (let [scaled-data (add-scale data)
        bar-height-and-spacing (+ BAR_HEIGHT BAR_SPACING)]
    (map (fn [{:keys [name value] scaled-value ::scaled-value} top]
           (list [:rect.bar {:x 0
                             :y top
                             :width (* CANVAS_WIDTH scaled-value)
                             :height BAR_HEIGHT}]
                 [:text.bar-name {:x (- axis-offset)
                                  :y (+ top BAR_HEIGHT TEXT_VERTICAL_OFFSET)
                                  ;:text-anchor "end" ; the text-anchor does not behave how i thought it would
                                  }
                  name]
                 [:text.bar-value {:x VALUE_TEXT_HORIZONTAL_OFFSET
                                   :y (+ top BAR_HEIGHT TEXT_VERTICAL_OFFSET)
                                   }
                  value]))
         scaled-data
         (iterate #(+ bar-height-and-spacing %) BAR_SPACING))))

(defn bar-chart [{:keys [data]}]
  (om/component
   (html
    (let [axis-offset (calculate-word-width data)]
      [:svg.bar {;:width WIDTH
                 ;:height HEIGHT
                 :viewBox (apply str (interpose \space [0 0 (+ axis-offset CANVAS_WIDTH) (calculate-height data)]))
                 :preserveAspectRatio "none"
                 :className "bar-graph"}
       ;(draw-x-axis)
       [:g {:transform (str "translate("  axis-offset ",0)")}
        (draw-bars {:data data
                    :axis-offset axis-offset})
        (draw-y-axis data)]]))))
