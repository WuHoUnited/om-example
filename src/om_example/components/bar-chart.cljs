(ns om-example.components.bar-chart
  "Generates an SVG bar-chart component for viewing our data."
  (:require [om.core :as om :include-macros true]

            [sablono.core :as html :refer-macros [html]]

            [om-example.core :as c]))

(enable-console-print!)

;; Our bar graph consist of horizontal bars with text on the left.
;; The SVG will dynamically resize as we change the underlying data.
;; The height will be based on the number of data points and the width will be based on
;; The state with the longest name. There may be better ways to do this but my SVG skills
;; are not so good and there are numerous difficulties mainly surrounding the varying lengths of data names.


;; Define a number of constants for computing how to draw our SVG.

(def BAR_HEIGHT "The height of a bar."
  10)
(def BAR_SPACING "The vertical distance between bars."
  5)

(def CANVAS_WIDTH "The width of the canvas on which the bars will be rendered."
  600)

(def TEXT_VERTICAL_OFFSET "This is used to attempt to vertically center the text for each bar."
  -3)

(def VALUE_TEXT_HORIZONTAL_OFFSET "This is used to horizentally position the value text."
  2)

;; Define approximations for guessing the width of an SVG String.

(def LETTER_WIDTH "This is an estimate of how wide an average letter appears to be in SVG."
  3.6)
(def LETTER_BASE "This is a fixed amount to use in estimating the width of an SVG String."
  5)

;; Define helper functions

(defn- get-max-value
  "Gets the maximum value for the :value key in data."
  [data]
  (->> data
       (map :value)
       (apply max)))

(defn- calculate-height
  "Calculates the total height of the SVG. This is based on the number of data points."
  [data]
  (let [bars (count data)]
    (+ (* bars BAR_HEIGHT)
       (* (inc bars) BAR_SPACING))))

(defn- draw-y-axis
  "Returns a sablono vector representing the y-axis for the bar-chart."
  [data]
  [:line.bar-axis {:x1 0
                   :x2 0
                   :y1 0
                   :y2 (calculate-height data)}])

(defn- calculate-word-width
  "This function estimates how wide the 'label' portion of the SVG should be.
  The longer the longest label, the wider that portion of the SVG has to be."
  [data]
  (let [longest-length (->> data
                            (map (comp count :name))
                            (apply max))]
    (+ LETTER_BASE
       (* LETTER_WIDTH longest-length))))

(defn- add-scale
  "This function adds a ::scaled-value keyword which indicates the fraction of
  each piece of data in comparison to the largest piece of data."
  [data]
  (let [max-value (get-max-value data)]
    (mapv (fn [{:keys [value] :as d}]
            (assoc-in d [::scaled-value] (/ value max-value)))
          data)))

(defn- draw-bars
  "Generates a sablono vector for the bars and acompanying text.
  Axis offset is the offset at which the bars should start being drawn.
  The axis is actually at the line x=0 with the labels on the left and the bars on the right.
  The component will actually be offset by its parent so this doesn't seem so strange."
  [{:keys [data axis-offset]}]
  (let [scaled-data (add-scale data)                                ; add the ratio of each datum to the largest
        bar-height-and-spacing (+ BAR_HEIGHT BAR_SPACING)]          ; cache this piece of data for later.
    (map (fn [{:keys [name value] scaled-value ::scaled-value} top] ; top is the top of the bar.
           (list [:rect.bar {:x 0                                   ; the bar
                             :y top
                             :width (* CANVAS_WIDTH scaled-value)
                             :height BAR_HEIGHT}]
                 [:text.bar-name {:x (- axis-offset)                ; the label
                                  :y (+ top BAR_HEIGHT TEXT_VERTICAL_OFFSET)
                                  ;:text-anchor "end" ; the text-anchor does not behave how i thought it would
                                  }
                  name]
                 [:text.bar-value {:x VALUE_TEXT_HORIZONTAL_OFFSET ; the value
                                   :y (+ top BAR_HEIGHT TEXT_VERTICAL_OFFSET)
                                   }
                  value]))
         scaled-data
         (iterate #(+ bar-height-and-spacing %) BAR_SPACING))))

;; Define bar-chart itself.

(defn bar-chart
  "Returns an om-component which creates an SVG bar-chart view of our data."
  [{:keys [data]}]
  (om/component
   (html
    (let [axis-offset (calculate-word-width data)]
      [:svg.bar-chart {;:width WIDTH
                       ;:height HEIGHT
                       ; We're using the viewBox and have to automatically calculate its width/height because they actually
                       ; depend on the data.
                       :viewBox (apply str (interpose \space [0 0 (+ axis-offset CANVAS_WIDTH) (calculate-height data)]))
                       :preserveAspectRatio "none" ; I actually probably do want to preserve the aspect ratio because of text.
                       :className "bar-graph"}
       ; draw-bars actually draws its axis at x=0 so we have to offset for the text
       [:g {:transform (str "translate("  axis-offset ",0)")}
        (draw-bars {:data data
                    :axis-offset axis-offset})
        (draw-y-axis data)]]))))
