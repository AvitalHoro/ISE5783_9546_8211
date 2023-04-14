package geometries;

import primitives.Point;
import primitives.Vector;

//A class for representing a plane by a point and the normal vector
public class Plane implements Geometry {

    Point q0;

    Vector normal;

    public Plane(Point q0, Vector normal) {
        this.q0 = q0;
        this.normal = normal.normalize();
    }

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
}
