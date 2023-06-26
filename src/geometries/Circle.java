package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 * represent a Circle in 3D
 */
public class Circle extends RadialGeometry {
    Plane plane;

    Point center;

    /**
     * parameters constructor circle
     * @param center
     * @param radius
     * @param normal
     */
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
    /**
     * A method that receives a ray and checks the points of GeoIntersection of the ray with the circle
     * @param ray
     * @param maxDistance
     * @return null / list that includes all the GeoIntersection points (contains the geometry (shape) and the point in 3D)
     */
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        //Finds the intersection points between the ray and the plane of the circle
        List<GeoPoint> planePoints = plane.findGeoIntersectionsHelper(ray, maxDistance);
        //if there not are intersection points between the ray and the plane
        if (planePoints == null)
            return null;
        //remove the intersection points that are not in the circle
        planePoints.removeIf(gp -> gp.point.distance(center) > radius);
        //if there not are intersection points between the ray and the circle
        if (planePoints.isEmpty())
            return null;
        //return list of the intersection points
        return List.of(new GeoPoint(this, planePoints.get(0).point));
    }
}