(ns maaswh.vector3)

(defn- add-op
  [item1 item2]
  (hash-map :x (+ (:x item1) (:x item2))
            :y (+ (:y item1) (:y item2))
            :z (+ (:z item1) (:z item2))))

(defn add
  [& items]
  (reduce (fn [acc item]
            (if (nil? item) acc
                            (add-op acc item)))
          {:x 0 :y 0 :z 0} items))

(defn- multiply-op
  [item1 item2]
  (hash-map :x (* (:x item1) (:x item2))
            :y (* (:y item1) (:y item2))
            :z (* (:z item1) (:z item2))))

(defn- multiply
  [& items]
  (reduce (fn [acc item]
            (if (nil? item) acc
                            (multiply-op acc item)))
          {:x 1 :y 1 :z 1} items))

(defn scale-by
  [scalar item]
  (multiply item {:x scalar :y scalar :z scalar}))

(defn displace
  ([initial terminal] (add terminal (scale-by -1 initial)))
  ([p] (displace {:x 0 :y 0 :z 0} p)))

(defn displace-coplanar
  [initial & points] (map #(displace initial %) points))

(defn dot-product
  [& items]
  (reduce + (vals (apply multiply items))))

(defn cross-product
  [item1 item2]
  (hash-map :x (- (* (:y item1) (:z item2)) (* (:z item1) (:y item2)))
            :y (- (* (:z item1) (:x item2)) (* (:x item1) (:z item2)))
            :z (- (* (:x item1) (:y item2)) (* (:y item1) (:x item2)))))
