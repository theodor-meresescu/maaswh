(ns maaswh.bezier-test
  (:require
    [clojure.test :refer :all]
    [maaswh.bezier :as bezier]))

(deftest find-point-on-bezier-curve
  (testing "When given a scalar and no points return zero vector."
    (is (= {:x 0 :y 0 :z 0} (bezier/bernstein [] 0.5))))
  (testing "When given a scalar and one point return the point."
    (is (= {:x 5.0 :y 2.0 :z -3.0} (bezier/bernstein
                                     [{:x 5 :y 2 :z -3}] 0.5))))
  (testing "When given a scalar two points return linear interpolation."
    (is (= {:x 2.2 :y 4.4 :z 5.4} (bezier/bernstein [{:x 2 :y 4 :z 6}
                                                      {:x 3 :y 6 :z 3}]
                                                     0.2))))
  (testing "When given a scalar and three points return a point on the
   quadratic bezier curve."
    (is (= {:x 1.5 :y 0.75 :z 0.125} (bezier/bernstein
                                       [{:x 0 :y 0 :z 2}
                                        {:x 1 :y 2 :z 0}
                                        {:x 2 :y 0 :z 0}]
                                       0.75))))
  (testing "When given a scalar and four points return a point on the
   cubic bezier curve."
    (is (= {:x 4.265625 :y 6.4765625 :z 0.0} (bezier/bernstein
                                          [{:x 3 :y 0.5 :z 0}
                                           {:x 9 :y 4 :z 0}
                                           {:x 5 :y 8 :z 0}
                                           {:x 2 :y 6 :z 0}]
                                          0.75))))
  (testing "When given a scalar and any number of points return a point
  on the bezier curve."
    (is (= {:x 2.3671875 :y 6.14111328125 :z 4.75} (bezier/bernstein
                                                    [{:x 3 :y 0.5 :z 1}
                                                     {:x 9 :y 4 :z 2}
                                                     {:x 5 :y 8 :z 3}
                                                     {:x 2 :y 6 :z 4}
                                                     {:x 2 :y 6 :z 5}
                                                     {:x 2 :y 6 :z 6}]
                                                    0.75)))))
