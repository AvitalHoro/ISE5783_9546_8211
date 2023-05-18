package geometries;

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
        Collections.addAll(shapes,geometries);
    }


    /**
     * If the ray intersects with one of the geometries, add the intersection points to the list of intersection points
     *
     * @param ray The ray that intersects the geometry.
     * @return A list of GeoPoints
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> intersection = null;
        for (var geometry : this.shapes) {
            List<GeoPoint> geometryIntersection = geometry.findGeoIntersections(ray);

            if (geometryIntersection != null) {
                if (intersection == null)
                    intersection = new LinkedList<>();
                intersection.addAll(geometryIntersection);
            }
        }
        return intersection;
    }

//    /**
//     * @param ray
//     * @return list of intersection points
//     */
//    @Override
//    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
//        List<GeoPoint> result = null;
//        for (Intersectable item : shapes) {
//            List<GeoPoint> itemList = item.findGeoIntersectionsHelper(ray);
//            if (itemList != null) {
//                if (result == null) {
//                    result = new LinkedList<>();
//                }
//                result.addAll(itemList);
//            }
//        }
//        return result;
//
//    }
}


