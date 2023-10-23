(ns maaswh.line
  (:require [maaswh.vector3 :as v3]))

(defn line
  [point-a point-b]
  (let [direction (v3/direction point-b point-a)]
    (hash-map :base-position point-a
              :direction direction)))

(defn intersect-with-plane
  [{:keys [direction base-position] :as line}
   {:keys [normal constant] :as plane}]
  (let [nXd (v3/dot-product normal direction)
        nXb (v3/dot-product normal base-position)]
    (if (zero? nXd)
      (if (= nXb constant) base-position nil)
      (let [t (/ (- constant nXb) nXd)]
        (v3/add base-position (v3/scale-by t direction))))))
