package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {

    /**
     * A private list of intersectable.
     */
    private List<Intersectable> shapes;

    /**
     * empty constructor
     */
    public Geometries() {
        shapes = new LinkedList();
    }

    /**
     * constructor with param. get list of geometries
     *
     * @param geometries
     */
    public Geometries(Intersectable... geometries) {
        shapes = new LinkedList(List.of(geometries));
    }

    /**
     * @param geometries
     */
    public void add(Intersectable... geometries) {
        Collections.addAll(shapes, geometries);
    }


    /**
     * find all intersection points {@link Point}
     * that intersect with a specific ray {@link Ray}
     *
     * @param ray ray pointing towards the shapes
     * @return immutable list of intersection geo points
     */
    @Override
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double max) {
        List<GeoPoint> intersections = null;
        for (var geo : shapes) {
            var geoIntersections = geo.findGeoIntersections(ray, max);
            if (geoIntersections != null) {
                if (intersections == null)
                    intersections = new LinkedList<>(geoIntersections);
                else
                    intersections.addAll(geoIntersections);
            }
        }
        return intersections;
    }
}


