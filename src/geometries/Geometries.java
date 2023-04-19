package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

public class Geometries implements Intersectable {
    List<Intersectable> bodies;

    public Geometries(){

    }
    public Geometries(Intersectable... geometries){

    }
    public void add(Intersectable... geometries){

    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
