(ns circles.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 30)
  (let [r 10
        w (q/width)
        h (q/height)
        ;; create collection of y-vals: [0 10 20 ... h+r]
        y-vals (take-nth r (range (+ h r)))
        ;; create collection of x-vals based off of y-vals: [0 0 0 0 0 10 10 10 10 10 ... w+r]
        ;; `#()` shorthand for anonymous/lambda function
        x-vals (flatten
                (map #(repeat (count y-vals) %)
                     (take-nth r (range (+ w r)))))
        ;; `cycle` takes a collection and returns infinite lazy sequence repeated indefinitely
        ;; `interleave` takes multiple seqs and returns a lazy seq with elements interleaved
        ;; `partition` takes number `n` & returns lazy seq of lists each containing `n` items
        origins (partition 2 (interleave x-vals (cycle y-vals)))
        circles (map (fn [[x y]] {:x x :y y :r r}) origins)]
    {:radius r
     :circles circles}))


;; `update` fn takes a `map`, a `key`, & a `fn`
;; `state`: current state which is also a `map`
;; `:circles`: the `key` in the state map which holds a collection of circles
(defn update-state [state]
  ;; pass `:circles` to anonymous fn which maps over `circles`
  (update state :circles #(map (fn [circle]
                                 ;; `cx` & `cy`: midpoint of canvas
                                 ;; `x` & `y`: current circle's coordinates
                                 ;; `r`: radius state defined during setup
                                 ;; `t`: time in seconds
                                 (let [cx (/ (q/width) 2)
                                       cy (/ (q/height) 2)
                                       x (:x circle)
                                       y (:y circle)
                                       r (:radius state)
                                       t (/ (q/millis) 1000)]
                                   ;; `assoc`: given `map`, `key`, `val` returns a new map/vector
                                   ;; `map` == `circle`
                                   ;; `key` == `:r`
                                   ;; `val` == `(radius variance using `q/sin` & `q/sq`)`
                                   (assoc circle :r (* r (q/sin
                                                          (+ t
                                                             (+ (q/sq (- cx x))
                                                                (q/sq (- cy y))))))))) %)))

(defn draw-state [state]
  (q/background 0 0 0)
  (q/no-stroke)
  (doseq [c (:circles state)]
    (q/ellipse (:x c) (:y c) (:r c) (:r c))))

(defn -main [& args]
  (q/defsketch circle-visualization
    :title "Circles"
    :host "host"
    :setup setup
    :update update-state
    :draw draw-state
    :size [800 600]
    :middleware [m/fun-mode]))
