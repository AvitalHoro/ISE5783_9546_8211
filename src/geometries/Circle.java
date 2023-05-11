package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;

/**
 * represent a Circle in 3D
 */
public class Circle extends RadialGeometry {
    Plane plane;

    Point center;

    public Circle(Point center, double radius, Vector normal) {
        super(radius);
        this.center = center;
        plane = new Plane(center, normal.normalize());
    }

    @Override
    public Vector getNormal(Point point) {
        return this.plane.getNormal(point);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> planeIntersection = this.plane.findIntersections(ray);
        if (planeIntersection == null)
            return null;

        Point p = planeIntersection.get(0);

        if (alignZero(p.distanceSquared(this.center) - this.radius * this.radius) >= 0)
            return null;

        planeIntersection = new ArrayList<>();
        planeIntersection.add(p);
        return planeIntersection;
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> planeIntersection = this.plane.findGeoIntersectionsHelper(ray);
        if (planeIntersection == null)
            return null;

        GeoPoint p = new GeoPoint(this, planeIntersection.get(0).point);

        if (alignZero(p.point.distanceSquared(this.center) - this.radius * this.radius) >= 0)
            return null;

        planeIntersection = new ArrayList<>();
        planeIntersection.add(p);
        return planeIntersection;    }
}