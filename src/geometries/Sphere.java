package geometries;

import primitives.Ray;
import primitives.Vector;

import primitives.Point;

import java.util.List;

public class Sphere extends RadialGeometry{
    /** sphere class extends RadialGeometry and represents the center in Point
     * @param center
     */

    /**the center point of the sphere*/
    Point center;

    //get center
    public Point getCenter() {
        return center;
    }

    //parameters constructor
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }

    @Override
    public Vector getNormal(Point point) {
        //𝒏 = 𝒏𝒐𝒓𝒎𝒂𝒍𝒊𝒛𝒆(𝑷 − 𝑶)
        return point.subtract(center).normalize();
    }
}
