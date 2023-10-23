(ns maaswh.plane
  (:require
    [maaswh.vector3 :as v3]))

(defn plane
  [a b c]
  (let [[ab ac] (v3/coplanar a b c)
        normal (v3/cross-product ab ac)
        nXa (v3/dot-product normal a)]
    (hash-map :normal normal
              :constant nXa)))
