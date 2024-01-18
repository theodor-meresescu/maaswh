(ns maaswh.plane-test
  (:require
    [clojure.test :refer :all]
    [maaswh.plane :as p]))

;Given three points, subtracting two different pairs will produce
;two coplanar vectors. The cross product of these will determine
;a normal vector (a, b, c). The equation of the plane can be expressed
;in standard form ax+by+cz=d, and the constant, d, can be found by
;substituting any point into the equation.

(deftest plane-in-3d
  (testing "When the three points are in the same position,
  there is no plane."
    (is (= {:normal {:x 0 :y 0 :z 0} :constant 0}
           (p/plane {:x 1 :y 4 :z 5}
                    {:x 1 :y 4 :z 5}
                    {:x 1 :y 4 :z 5}))))
  (testing "When the three points are collinear,
  there is no plane."
    (is (= {:normal {:x 0 :y 0 :z 0} :constant 0}
           (p/plane {:x 1 :y 2 :z 3}
                    {:x 3 :y 4 :z 5}
                    {:x 5 :y 6 :z 7}))))
  (testing "When given three points, return a plane with a normal
  and a constant."
    (is (= {:normal {:x 15 :y 3 :z 4} :constant 47}
           (p/plane {:x 1 :y 4 :z 5}
                    {:x 2 :y -1 :z 5}
                    {:x 3 :y -2 :z 2})))))
