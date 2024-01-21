(ns maaswh.plane
  (:require
    [maaswh.vector3 :as v3]))

(defn plane
  [a b c]
  (let [[ab ac] (v3/displace-coplanar a b c)
        n (v3/cross-product ab ac)
        d (v3/dot-product n a)]
    (hash-map :normal n
              :constant d)))
