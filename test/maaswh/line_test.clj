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

;Linear interpolation is a method used to find values that lie between
;two known values [0, 1]. In the context of 3D points, linear interpolation
;can be applied to interpolate between two sets of 3D coordinates.
;Algebraically lerp is: a + (b - a) * t (reduced to: a * (1-t) + b*t).
;t is the scalar parameter between 0 and 1

(deftest vector3-lerp
  (testing "When t = 0, returns item1."
    (is (= (l/lerp {:x 1 :y 1 :z 1}
                    {:x 2 :y 2 :z 2}
                    0)
           {:x 1 :y 1 :z 1})))
  (testing "When t = 1, returns item2."
    (is (= (l/lerp {:x 1 :y 1 :z 1}
                    {:x 2 :y 2 :z 2}
                    1)
           {:x 2 :y 2 :z 2})))
  (testing "When t = 0.5, returns the point midway between item1 and item2."
    (is (= (l/lerp {:x 1 :y 1 :z 1}
                    {:x 2 :y 2 :z 2}
                    0.5)
           {:x 1.5 :y 1.5 :z 1.5})))
  (testing "When t is out of range, throws exception."
    (is (thrown? Exception (l/lerp {:x 1 :y 1 :z 1}
                                    {:x 2 :y 2 :z 2}
                                    2)))))