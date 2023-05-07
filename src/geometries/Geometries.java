package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

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
    public List<Point> findIntersections(Ray ray) {

        List<Point> result  = null;

        for (Intersectable intersect : shapes) {
            List<Point> pointsList = intersect.findIntersections(ray);
            if(pointsList != null){
                if(result == null){
                    result = new LinkedList<>();
                }
                result.addAll(pointsList);
            }
        }

        return result;
    }

}
