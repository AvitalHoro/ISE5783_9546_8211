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
    public Geometries(){
        bodies = new LinkedList();
    }

    /**
     * constructor with param. get list of geometries
     * @param geometries
     */
    public Geometries(Intersectable... geometries){
        bodies = new LinkedList(List.of(geometries));
    }

    /**
     *
     * @param geometries
     */
    public void add(Intersectable... geometries){
        bodies.add((Intersectable) List.of(geometries));
    }

    /**
     *
     * @param ray
     * @return
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
