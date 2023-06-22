package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;



public class Plane extends Geometry {
    /**Plane class represents a plane by a point and the normal vector
     * @param q0
     * @param normal
     */

    /**point on the plane*/
    Point q0;

    /**normal to the plane*/
    Vector normal;

    /**
     * point and vector constructor
     * @param q0
     * @param normal
     */
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    /**
     * points constructor
     * @param p1
     * @param p2
     * @param p3
     */
    public Plane(Point p1, Point p2, Point p3) {
        q0 = p1;

        //𝑣1 = 𝑃2 − 𝑃1
        //𝑣2 = 𝑃3 − 𝑃1
        //𝑛 = 𝑛𝑜𝑟𝑚𝑎𝑙𝑖𝑧𝑒(𝑣1 × 𝑣2)
        Vector v1 = p2.subtract(p1);
        Vector v2 = p3.subtract(p1);
        normal=(v1.crossProduct(v2)).normalize();
    }
    @Override
    public Vector getNormal(Point point) {
        return normal;
    }


    /**
     * Finds the intersection points of the ray with the surface of the object
     * @param ray The ray to intersect with the GeoPoint.
     * @param maxDistance The maximum distance from the source of the ray to intersect with.
     * @return A list of GeoPoints that are the intersections of the ray with the object.
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();
        Vector n = normal;

        // ray begins at q0 of the plane
        if (q0.equals(P0)) {
            return null;
        }

        // ray is laying in the plane axis
        double nv = n.dotProduct(v);
        //ray direction cannot be parallel to plane orientation
        if (isZero(nv)) {
            return null;
        }

        Vector P0_Q0 = q0.subtract(P0);

        // numerator
        double nQMinusP0 = alignZero(n.dotProduct(P0_Q0));

        // t should be > 0
        if (isZero(nQMinusP0)) {
            return null;
        }

        double t = alignZero(nQMinusP0 / nv);

        // t should be > 0
        if (t < 0 || alignZero(t - maxDistance) > 0) {
            return null;
        }

        // return immutable List
        return List.of(new GeoPoint(this, ray.getPoint(t)));
    }
}



