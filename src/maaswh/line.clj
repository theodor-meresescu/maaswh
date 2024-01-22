(ns maaswh.line
  (:require [maaswh.vector3 :as v3]))

(defn lerp
  [item1 item2 t]
  (if (<= 0 t 1)
    (v3/add (v3/scale-by (- 1 t) item1)
            (v3/scale-by t item2))
    (throw
      (ex-info "Argument out of range: [0, 1]" {:cause t}))))

(defn line
  [point-a point-b]
  (let [ab (v3/displace point-a point-b)]
    (hash-map :base-position point-a
              :direction ab)))

;Plane equation: Ax + By + Cz = D

;Parametric equation for line:
;x = x0 + at
;y = y0 + bt
;z = z0 + ct

;Substitute parametric line in plane equation:
;A(x0 + at) + B(y0 + bt) + C(z0 + ct) = D

;Expand:
;Ax0 + Aat + By0 + Bbt + Cz0 + Cct = D

;Rearrange and reduce:
;(Aa + Bb + Cc)t = D - Ax0 - By0 - Cz0 => t = D - (Ax0 - By0 - Cz0) / Aa + Bb + Cc

;Which can be written as:
;t = D - (Ax0 + By0 + Cz0) / Aa + Bb + Cc => refactor dot products and you have:
;t = plane constant - dot product of plane normal and a point on the line /
;dot product of plane normal and direction vector of the line

;Substitute t in the parametric equation for the line and
;you have the point of intersection given that dot product of plane normal
;and line direction is non zero, I.e. line is not parallel to the plane

(defn intersect-with-plane
  [{:keys [direction base-position] :as line}
   {:keys [normal constant] :as plane}]
  (let [projection (v3/dot-product normal direction)
        ref-pos (v3/dot-product normal base-position)]
    (if (zero? projection)
      (if (= ref-pos constant) base-position nil)
      (let [t (/ (- constant ref-pos) projection)]
        (v3/add base-position (v3/scale-by t direction))))))
