package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.Objects;

public abstract class Intersectable {
    protected Object intersectables;

    /**
     * find all intersection points {@link Point}
     * that intersect with a specific ray{@link Ray}
     * @param ray ray pointing towards the graphic object
     * @return immutable list of intersection points {@link Point}
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(gp -> gp.point).toList();
    }

    /**
     * find all intersection points {@link Point}
     * that intersect with a specific ray{@link Ray} in a range of distance
     * @param ray ray pointing towards the graphic object
     * @param maxDistance the maximum distance between the point to the start of the ray
     * @return immutable list of intersection geo points {@link GeoPoint}
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray, double maxDistance) {
        return findGeoIntersectionsHelper(ray, maxDistance);
    }
    /**
     * find all intersection points {@link Point}
     * that intersect with a specific ray{@link Ray}
     * @param ray ray pointing towards the graphic object
     * @return immutable list of intersection geo points {@link GeoPoint}
     */
    public final List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersections(ray, Double.POSITIVE_INFINITY);
    }

    /**
     * helper of findGeoIntersections
     * @param ray ray pointing towards the graphic object
     * @param maxDistance the maximum distance between the point to the start of the ray
     * @return immutable list of intersection geo points {@link GeoPoint}
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance);

    /**
     * intersects the ray with the scene and finds the closest point the ray intersects
     * @param ray to intersect the scene with
     * @return the closest point
     */
    public GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersectionPoints = findGeoIntersections(ray);
        return ray.findClosestGeoPoint(intersectionPoints);
    }
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
