package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.*;



public class Plane implements Geometry {
    /**Plane class represents a plane by a point and the normal vector
     * @param q0
     * @param normal
     */

    /**point and normal to representation plane*/
    Point q0;

    Vector normal;

    //point and vector constructor
    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }
    //points constructor
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
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getP0();
        Vector v = ray.getDir();

        Vector n = normal;

        if (q0.equals(p0))
            return null;

        Vector p0_q = q0.subtract(p0);

        double nemurator = alignZero(n.dotProduct(p0_q));

        if (isZero(nemurator))
            return null;

        double denominator = alignZero(n.dotProduct(v));

        if (isZero(denominator))
            return null;

        double t = alignZero(nemurator / denominator);

        if(t<=0)
            return null;

        Point intersection_point = ray.getPoint(t);


        return List.of(intersection_point);
    }

    @Override
    public Vector getNormal(Point point) {
        return normal;
    }

}
