package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries extends Intersectable {

    /**
     *
     */
    List<Intersectable> shapes;

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
     * @param ray
     * @return list of intersection points
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> result = null;
        for (Intersectable item : shapes) {
            List<GeoPoint> itemList = item.findGeoIntersectionsHelper(ray);
            if (itemList != null) {
                if (result == null) {
                    result = new LinkedList<>();
                }
                result.addAll(itemList);
            }
        }
        return result;

    }
}


