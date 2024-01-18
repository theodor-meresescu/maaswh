(ns maaswh.vector3-test
  (:require
    [clojure.test :refer :all]
    [maaswh.vector3 :as v3]))

(deftest vector3-addition
  (testing "Existence of identity: for every position (x, y, z) there is
  an origin (x0, y0, z0)."
    (is (= {:x 1 :y 2 :z 3} (v3/add {:x 1 :y 2 :z 3} {:x 0 :y 0 :z 0}))))
  (testing "Existence of inverse: addition by scalar opposite yields a
  vector (0, 0, 0)."
    (is (= {:x 0 :y 0 :z 0} (v3/add {:x 1 :y 2 :z 3} {:x -1 :y -2 :z -3}))))
  (testing "Commutativity: addition is commutative."
    (is (= (v3/add {:x 2 :y 3 :z 4} {:x 1 :y 2 :z 3})
           (v3/add {:x 1 :y 2 :z 3} {:x 2 :y 3 :z 4}))))
  (testing "Distributive property."
    (is (= (v3/add {:x 2 :y 3 :z 4} {:x 1 :y 2 :z 3} {:x 3 :y 4 :z 5})
           (v3/add {:x 1 :y 2 :z 3} {:x 3 :y 4 :z 5} {:x 2 :y 3 :z 4})))))

(deftest vector3-scale-by
  (testing "Existence of identity."
    (is (= {:x 1 :y 2 :z 3} (v3/scale-by 1 {:x 1 :y 2 :z 3}))))
  (testing "Multiplicative property of -1."
    (is (= {:x -1 :y -2 :z -3} (v3/scale-by -1 {:x 1 :y 2 :z 3}))))
  (testing "Multiplicative property of 0."
    (is (= {:x 0 :y 0 :z 0} (v3/scale-by 0 {:x 1 :y 2 :z 3}))))
  (testing "Distributive property."
    (is (= (v3/add (v3/scale-by 2 {:x 1 :y 2 :z 3})
                   (v3/scale-by 3 {:x 1 :y 2 :z 3}))
           (v3/scale-by (+ 2 3) {:x 1 :y 2 :z 3})))))

;A vector has a direction defined by the difference between
;two points in space (x1, y1, z1) and (x2, y2, z2).

(deftest vector3-displace
  (testing "When given two points, displace positional vector AB."
    (is (= '({:y -5, :z 0, :x 1})
           (v3/displace {:x 1 :y 4 :z 5}
                        {:x 2 :y -1 :z 5}))))
  (testing "When given 3 points, displace two position vectors AB/AC."
    (is (= '({:x 1 :y -5 :z 0} {:x 2 :y -6 :z -3})
           (v3/displace {:x 1 :y 4 :z 5}
                        {:x 2 :y -1 :z 5}
                        {:x 3 :y -2 :z 2}))))
  (testing "Given any number of points, displace position vectors."
    (is (= '({:y -5, :z 0, :x 1} {:y -6, :z -3, :x 2} {:y -7, :z 1, :x 3})
           (v3/displace {:x 1 :y 4 :z 5}
                        {:x 2 :y -1 :z 5}
                        {:x 3 :y -2 :z 2}
                        {:x 4 :y -3 :z 6})))))

;The cross product of two vectors yields a vector that
;is perpendicular to the plane containing the two given vectors.
;In parametric form, the cross product of two vectors is:
;x = y1z2 - z1y2
;y = z1x2 - x1z2
;z = x1y2 - y1x2

(deftest vector3-cross-product-of-two-vectors
  (testing "When two vectors are on the same plane, the cross product
  is perpendicular to the plane."
    (is (= {:x 0 :y 0 :z 1} (v3/cross-product {:x 1 :y 0 :z 0}
                                              {:x 0 :y 1 :z 0})))))

;The dot product of two vectors yields a scalar that is
;the product of the magnitudes of the two vectors and the cosine
;of the angle between them. Given these properties and the fact that
;the dot product is commutative, we can expand the dot product a⋅b in
;terms of components.
;Algebraically the dot product of two vectors is equal to the sum of
;the products of the individual components of the two vectors.

(deftest vector3-dot-product
  (testing "When two vectors are parallel to the plane,
  they don't form an angle."
    (is (= 0 (v3/dot-product {:x 1 :y 0 :z 0}
                             {:x 0 :y 1 :z 0}))))
  (testing "Since the dot product is a positive number,
  you can infer that the vectors would form an acute angle."
    (is (pos? (v3/dot-product {:x 1 :y 2 :z 3}
                              {:x 4 :y -5 :z 6}))))
  (testing "Since the dot product is a negative number,
  you can infer that the vectors would form an obtuse angle."
    (is (neg? (v3/dot-product {:x 2 :y -3 :z 7}
                              {:x -4 :y -3 :z -4})))))

;Linear interpolation is a method used to find values that lie between
;two known values [0, 1]. In the context of 3D points, linear interpolation
;can be applied to interpolate between two sets of 3D coordinates.
;Algebraically lerp is: a + (b - a) * t (reduced to: a * (1-t) + b*t).
;t is the scalar parameter between 0 and 1

(deftest vector3-lerp
  (testing "When t = 0, returns item1."
    (is (= (v3/lerp {:x 1 :y 1 :z 1}
                    {:x 2 :y 2 :z 2}
                    0)
           {:x 1 :y 1 :z 1})))
  (testing "When t = 1, returns item2."
    (is (= (v3/lerp {:x 1 :y 1 :z 1}
                    {:x 2 :y 2 :z 2}
                    1)
           {:x 2 :y 2 :z 2})))
  (testing "When t = 0.5, returns the point midway between item1 and item2."
    (is (= (v3/lerp {:x 1 :y 1 :z 1}
                    {:x 2 :y 2 :z 2}
                    0.5)
           {:x 1.5 :y 1.5 :z 1.5})))
  (testing "When t is out of range, throws exception."
    (is (thrown? Exception (v3/lerp {:x 1 :y 1 :z 1}
                                    {:x 2 :y 2 :z 2}
                                    2)))))