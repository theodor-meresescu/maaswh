(ns maaswh.bezier
  (:require
    [maaswh.vector3 :as v3]))

(defn factorial
  [n]
  (reduce * (range 1 (inc n))))

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
  [curve t]
  (let [degree (dec (count curve))]
    (apply v3/add
           (map #(v3/scale-by %1 %2)
                (evaluate-bernstein-basis degree t) curve))))
