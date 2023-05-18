package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

public abstract class Intersectable {
    protected Object intersectables;

    /** Intersectable interface for geometry bodies to calculate intersections points between ray and body
     * @param ray
     * @return List<Point>
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null : geoList.stream().map(gp -> gp.point).toList();
    }
    /**
     * This function returns a list of all the intersections of the ray with the geometry of the scene
     *
     * @param ray The ray to find intersections with.
     * @return A list of GeoPoints.
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
    }

    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        return findGeoIntersectionsHelper(ray, maxDistance);
    }

    /**
     * Finds the intersection points of the ray with the surface of the object
     *
     * @param ray The ray to intersect with the GeoPoint.
     * @param maxDistance The maximum distance from the source of the ray to intersect with.
     * @return A list of GeoPoints that are the intersections of the ray with the object.
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);


    /**
     * pds class
     */
    public static class GeoPoint {
        /**
         *
         */
        public Geometry geometry;

        /**
         *
         */
        public Point point;

        /**
         * GeoPoint constructor
         * @param geometry
         * @param point
         */
        public  GeoPoint(Geometry geometry, Point point)
        {
            this.geometry = geometry;
            this.point = point;
        }

        //return if two GeoPoints are equals
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof GeoPoint other)
                return this.geometry.equals(other.geometry) && this.point.equals(other.point);
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(geometry, point);
        }

        @Override
        public String toString() {
            return "GeoPoint{" +
                    "geometry=" + geometry +
                    ", point=" + point +
                    '}';
        }
    }


}
