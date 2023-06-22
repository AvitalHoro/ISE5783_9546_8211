package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Triangle extends Polygon{

    /**
     * constructor
     * @param p1
     * @param p2
     * @param p3
     *
     */
    public Triangle(Point p1, Point p2, Point p3) {
        super(p1,p2,p3);
    }

    /**
     * Finds the intersection points of the ray with the surface of the Triangle
     *
     * @param ray The ray to intersect with the GeoPoint.
     * @param maxDistance The maximum distance from the source of the ray to intersect with.
     * @return A list of GeoPoints that are the intersections of the ray with the object.
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        // Gets all intersections with the plane
        var result = plane.findGeoIntersections(ray, maxDistance);

        // if there is no intersections with the whole plane,
        // then is no intersections with the triangle
        if (result == null) {
            return null;
        }

        Point P0 = ray.getP0();
        Vector v = ray.getDir();

        Vector v1 = this.vertices.get(0).subtract(P0),
                v2 = this.vertices.get(1).subtract(P0),
                v3 = this.vertices.get(2).subtract(P0);

        Vector n1 = v1.crossProduct(v2).normalize(),
                n2 = v2.crossProduct(v3).normalize(),
                n3 = v3.crossProduct(v1).normalize();

        double a = alignZero(v.dotProduct(n1)),
                b = alignZero(v.dotProduct(n2)),
                c = alignZero(v.dotProduct(n3));

        // if all the points have the same sign(+/-),
        // all the points are inside the triangle
        if (a < 0 && b < 0 && c < 0 || a > 0 && b > 0 && c > 0)
            return List.of(new GeoPoint(this,result.get(0).point));

        return null;
    }

}


