package primitives;
import geometries.Intersectable.GeoPoint;
import java.util.List;

public class Ray {

    //parameter for size of first moving rays for shading rays
    private static final double DELTA = 0.1;

    /** Ray class represents ray with Point and Vector
     * @param p0
     * @param dir
     */

    /**point to represent a ray*/
    final Point p0;

    /**vector to represent a ray*/
    final Vector dir;

    /**
     * constructor to initialize Ray based object with Point and Vector
     * @param p0
     * @param dir
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }
    /**
     * Constructor that moves the ray by DELTA
     * @param p0 point
     * @param direction direction (must be normalized)
     * @param normal normal
     */
    public Ray(Point p0, Vector direction, Vector normal) {
        Vector delta = normal.scale(normal.dotProduct(direction) > 0 ? DELTA : - DELTA);
        this.p0 = p0.add(delta);
        this.dir = direction;
    }
    //get p0
    public Point getP0() {
        return p0;
    }

    //get dir
    public Vector getDir() {
        return dir;
    }

    @Override
    //return if two rays are equals
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Ray other)
            return this.p0.equals(other.p0) && this.dir.equals(other.dir);
        return false;
    }

    @Override
    //return the point and vector of ray
    public String toString() {
        return "Ray{" +
                "point: " + p0 +
                ", vector: " + dir +
                '}';
    }

    /**
     *
     * @param t - scalar to scale with
     * @return point
     */
    public Point getPoint(double t)
    {
        return p0.add(dir.scale(t));
    }


    /** Try to think of a better solution
     *  find the point that is the closet one to the head of the ray
     *
     * @param points
     * @return the closest point to the head of the ray
     */
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }
    //endregion
    /**
     * find the closest GeoPoint to the head of the ray
     * @param points a list of GeoPoints
     * @return the closest GeoPoint
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> points) {
        if (points == null)
            return null;

        GeoPoint closestPoint = points.get(0);
        double distance = closestPoint.point.distanceSquared(this.p0);

        for (GeoPoint geoPoint : points) {
            double d = geoPoint.point.distanceSquared(this.p0);
            if(distance > d)    // if there is a closer point then 'point', replace the values
            {
                closestPoint = geoPoint;
                distance = d;
            }
        }
        return closestPoint;
    }

//    /**
//     * Try to think of a better solution
//     *  find the point that is the closet one to the head of the ray
//     * @param intersections
//     * @return the closest geo-point to the head of the ray
//     */
//    public GeoPoint findClosestGeoPoint(List<GeoPoint> intersections) {
//        GeoPoint closestpoint = null;
//        double minDistance = Double.MAX_VALUE;
//        double ptDistance;
//
//        for (GeoPoint geoPoint : intersections) {
//            ptDistance = geoPoint.point.distanceSquared(p0);
//            if (ptDistance < minDistance) {
//                minDistance = ptDistance;
//                closestpoint = geoPoint;
//            }
//        }
//        return closestpoint;
//    }

}
