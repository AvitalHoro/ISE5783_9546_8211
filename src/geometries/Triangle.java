package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

public class Triangle extends Polygon{

    @Override
    public List<Point> findIntersections(Ray ray) {
        return super.findIntersections(ray);
    }

    /** triangle class represents 3 points coordinates
     * @param p1
     * @param p2
     * @param p3
     */

    //parameters constructor
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1,p2,p3);
    }

}
