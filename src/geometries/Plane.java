package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry {

    Point q0;    /**point that represents a plane*/

    /**get of point*/
     public Point getQ0() {
        return q0;
    }

    Vector normal;    /**normal to the plane*/

    public Plane(Point q0, Vector normal) {  /**parameters constructor*/
        this.q0 = q0;
        this.normal = normal.normalize();
    }

    public Plane(Point p1, Point p2, Point p3) {    /**parameters constructor with point*/
        q0 = p1;
        normal=getNormal();
    }

    @Override
    public Vector getNormal() {
        return null;
    }
}
