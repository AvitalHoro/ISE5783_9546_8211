package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.LinkedList;
import java.util.List;

public class Geometries implements Intersectable {

    /**
     *
     */
    List<Intersectable> bodies;

    /**
     * empty constructor
     */
    public Geometries() {
        bodies = new LinkedList();
    }

    /**
     * constructor with param. get list of geometries
     *
     * @param geometries
     */
    public Geometries(Intersectable... geometries) {
        bodies = new LinkedList(List.of(geometries));
    }

    /**
     * @param geometries
     */
    public void add(Intersectable... geometries) {
        bodies.add((Intersectable) List.of(geometries));
    }

    /**
     * @param ray
     * @return list of intersection points
     */
    @Override
    public List<Point> findIntersections(Ray ray) {

        boolean is_have_intersections = false;

        for (Intersectable intersect : bodies) {
            if (intersect.findIntersections(ray) != null) {
                is_have_intersections = true;
                break;
            }
        }

        if (is_have_intersections) {
            List<Point> listIntersect = new LinkedList();

            for (Intersectable intersect : bodies) {
                for (Point point : intersect.findIntersections(ray))
                    if (point != null) {
                        listIntersect.add(point);
                    }
            }
            return listIntersect;
        }
        return null;
    }

}
