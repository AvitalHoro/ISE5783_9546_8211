package primitives;
import geometries.Intersectable.GeoPoint;
import java.util.List;

public class Ray {
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
     * Try to think of a better solution
     *  find the point that is the closet one to the head of the ray
     * @param intersections
     * @return the closest geo-point to the head of the ray
     */
    public GeoPoint findClosestGeoPoint(List<GeoPoint> intersections) {
        GeoPoint closestpoint = null;
        double minDistance = Double.MAX_VALUE;
        double ptDistance;

        for (GeoPoint geoPoint : intersections) {
            ptDistance = geoPoint.point.distanceSquared(p0);
            if (ptDistance < minDistance) {
                minDistance = ptDistance;
                closestpoint = geoPoint;
            }
        }
        return closestpoint;
    }

}
