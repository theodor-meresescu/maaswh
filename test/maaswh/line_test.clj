(ns maaswh.line-test
  (:require [clojure.test :refer :all]
            [maaswh.line :as l]))

;Line is defined by two points on the line. Subtracting the two
;points will produce a vector that represents the direction of the line.

(deftest line-in-3d
  (testing "Given two points in the same position, there is
  no line, only the base position."
    (is (= {:base-position {:x 1 :y 0 :z 0}
            :direction     {:x 0 :y 0 :z 0}}
           (l/line {:x 1 :y 0 :z 0}
                   {:x 1 :y 0 :z 0}))))
  (testing "Given two points on the x-axis and y-axis, the
  line will be parallel to the z-axis."
    (is (= {:base-position {:x 1 :y 1 :z 0}
            :direction     {:x 2 :y 0 :z 0}}
           (l/line {:x 1 :y 1 :z 0}
                   {:x 3 :y 1 :z 0}))))
  (testing "Given two points, return a line representation."
    (is (= {:base-position {:x -2 :y 1 :z 5}
            :direction     {:x 7 :y 1 :z -8}}
           (l/line {:x -2 :y 1 :z 5}
                   {:x 5 :y 2 :z -3})))))

;To find the intersection point between the line and the plane, substitute
;the expression for the line into the equation of the plane:
;ax+by+cz+d=0
;ax(t)+by(t)+cz(t)+d=0
;This gives you an equation involving the parameter t. Solve for t, and
;then substitute back into the line equation L(t) = P + t * D to find
;the intersection point.

(deftest line-plane-intersection
  (testing "When line has intersection with plane."
    (is (= {:x 64/103 :y 1N :z 893/103}
           (l/intersect-with-plane {:base-position {:x -2 :y 1 :z 5}
                                    :direction     {:x 5 :y 0 :z 7}}
                                   {:normal   {:x 15 :y 3 :z 4}
                                    :constant 47}))))
  (testing "When line is parallel to plane."
    (is (= nil
           (l/intersect-with-plane {:base-position {:x -2 :y 1 :z 5}
                                    :direction     {:x 0 :y 0 :z -2}}
                                   {:normal   {:x 5 :y 1 :z 0}
                                    :constant 0}))))
  (testing "When line is on plane."
    (is (= {:x 1, :y 4, :z 5}
           (l/intersect-with-plane {:base-position {:x 1, :y 4, :z 5}
                                    :direction     {:y -6, :z -3, :x 2}}
                                   {:normal   {:y 3, :z 4, :x 15}
                                    :constant 47})))))
