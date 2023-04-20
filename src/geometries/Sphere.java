package geometries;

import primitives.Ray;
import primitives.Vector;

import primitives.Point;

import java.util.List;

import static primitives.Util.*;

public class Sphere extends RadialGeometry{
    /** sphere class extends RadialGeometry and represents the center in Point
     * @param center - the center point of the sphere
     */
    Point center;

    /**
     *
      * @return center point of sphere
     */
    public Point getCenter() {
        return center;
    }

    /**
     * constructor get param. use for radial geometry constructor
     * @param radius
     * @param center
     */
    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    /**
     *
     * @param point
     * @return the normal
     */
    @Override
    public Vector getNormal(Point point) {
        //𝒏 = 𝒏𝒐𝒓𝒎𝒂𝒍𝒊𝒛𝒆(𝑷 − 𝑶)
        return point.subtract(center).normalize();
    }

    /**
     *
     * @param ray
     * @return intersection if they exist
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        Point p0 = ray.getP0();
        Vector v = ray.getDir();

        if(p0.equals(center))
            return List.of(center.add(v.scale(radius)));
        Vector u = center.subtract(p0);

        double tm = alignZero(v.dotProduct(u));
        double d = alignZero(Math.sqrt(u.lengthSquared() - tm * tm));

        if(d>=radius)
            return null;

        double th = alignZero(Math.sqrt(radius*radius -d*d));
        if (th<=0)
            return null;

        double t1 = alignZero(tm + th);
        double t2 = alignZero(tm - th);

        if (t1 > 0 && t2 > 0)
        {
            Point p1 = p0.add(v.scale(t1));
            Point p2 = p0.add(v.scale(t2));
            return List.of(p1,p2);
        }
        if (t1 > 0)
        {
            Point p1 = p0.add(v.scale(t1));
            return List.of(p1);
        }
        if (t2 > 0)
        {
            Point p2 = p0.add(v.scale(t2));
            return List.of(p2);
        }

        return null;

    }
}
