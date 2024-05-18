(ns circles.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m])
  (:gen-class))

(defn setup []
  (q/frame-rate 30)
  {:circles (mapv (fn [_]
                    {:x (q/random (q/width))
                     :y (q/random (q/height))
                     :size (q/random 10 50)
                     :color [(q/random 255) (q/random 255) (q/random 255)]})
                  (range 10))})

(defn update-state [state]
  (update state :circles (fn [circles]
                           (mapv (fn [circle]
                                   (assoc circle
                                          :size (+ (:size circle) (q/random -1 1))))
                                 circles))))

(defn draw-state [state]
  (q/background 255)
  (doseq [circle (:circles state)]
    (q/fill (apply q/color (:color circle)))
    (q/ellipse (:x circle) (:y circle) (:size circle) (:size circle))))

(defn -main [& args]
  (q/defsketch circle-visualization
    :title "Circles"
    :host "localhost"
    :setup setup
    :update update-state
    :draw draw-state
    :size [800 600]
    :middleware [m/fun-mode]))
