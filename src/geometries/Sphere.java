package geometries;

import primitives.Ray;
import primitives.Vector;

import primitives.Point;

import java.util.List;

import static primitives.Util.*;

public class Sphere extends RadialGeometry
{
    /** sphere class extends RadialGeometry and represents the center in Point
     * @param center - the center point of the sphere
     */
    Point center;

    /**
     * get center point
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
     * calculate the normal vector in the specific point on the sphere
     * @param point
     * @return the normal
     */
    @Override
    public Vector getNormal(Point point) {
        //𝒏 = 𝒏𝒐𝒓𝒎𝒂𝒍𝒊𝒛𝒆(𝑷 − 𝑶)
        return point.subtract(center).normalize();
    }

    /**
     * Finds the intersection points of the ray with the surface of the sphere
     * @param ray The ray to intersect with the GeoPoint.
     * @param maxDistance The maximum distance from the source of the ray to intersect with.
     * @return A list of GeoPoints that are the intersections of the ray with the object.
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        Point P0 = ray.getP0();
        Vector v = ray.getDir();

        if(P0.equals(center)){
            if(alignZero(this.radius - maxDistance) > 0)
                return null;
            return List.of(new GeoPoint(this, center.add(v.scale(radius))));
            //throw new IllegalArgumentException("p of Ray is the center of the sphere");
        }

        Vector u = center.subtract(P0);

        double tm = alignZero(u.dotProduct(v));
        double d = alignZero(Math.sqrt(u.lengthSquared() - (tm * tm) ));

        // no intersections : the ray direction is above the sphere
        if(d >= radius){
            return null;
        }

        double th = alignZero(Math.sqrt( (radius * radius) - (d * d) ));

        double t1 = alignZero(tm - th);
        double t2 = alignZero(tm + th);

        if(t1 > 0 && t2 > 0 && alignZero(t1 - maxDistance) <= 0 && alignZero(t2 - maxDistance) <= 0){
            GeoPoint p1 = new GeoPoint(this,ray.getPoint(t1));
            GeoPoint p2 =  new GeoPoint(this,ray.getPoint(t2));
            return List.of(p1, p2);
        }

        if(t1 > 0 && alignZero(t1 - maxDistance) <= 0)
            return List.of(new GeoPoint(this, ray.getPoint(t1)));

        if(t2 > 0 && alignZero(t2 - maxDistance) <= 0)
            return List.of(new GeoPoint(this, ray.getPoint(t2)));

        // no intersections at all
        return null;
    }
}
