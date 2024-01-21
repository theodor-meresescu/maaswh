(ns maaswh.bezier
  (:require
    [maaswh.vector3 :as v3]))

(defn ^{:superseded-by "bernstein"} quadratic
  [points t]
  (let [u (- 1 t)]
    (v3/add (v3/scale-by (* u u) (first points))
            (v3/scale-by (* 2 u t) (second points))
            (v3/scale-by (* t t) (nth points 2)))))

(defn ^{:superseded-by "bernstein"} cubic
  [points t]
  (let [u (- 1 t)]
    (v3/add (v3/scale-by u (quadratic (take 3 points) t))
            (v3/scale-by t (quadratic (rest points) t)))))

(defn ^{:superseded-by "bernstein"} de-casteljau
  [curve t]
  (cond
    (empty? curve) {:x 0 :y 0 :z 0}
    (= (count curve) 1) (first curve)
    (= (count curve) 2) (v3/lerp (first curve) (second curve) t)
    (= (count curve) 3) (quadratic curve t)
    (= (count curve) 4) (cubic curve t)
    :else (recur (map #(cubic % t) (partition 4 1 curve)) t)))

(defn factorial
  [n]
  (if (<= n 1)
    1
    (* n (factorial (dec n)))))

(defn binomial-coefficient
  [n k]
  (/ (factorial n) (* (factorial k) (factorial (- n k)))))

(defn bernstein-polynomial [i n t]
  (* (binomial-coefficient n i)
     (Math/pow (- 1 t) (- n i))
     (Math/pow t i)))

(defn evaluate-bernstein-basis [n t]
  (map #(bernstein-polynomial % n t) (range (inc n))))

(defn bernstein
  [points t]
  (let [degree (dec (count points))]
    (apply v3/add
           (map #(v3/scale-by %1 %2)
                (evaluate-bernstein-basis degree t) points))))
