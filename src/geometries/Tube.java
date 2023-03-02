package geometries;

import primitives.Ray;
import primitives.Vector;


public class Tube extends RadialGeometry {
    Ray ray;

    public Tube(double radius, Ray ray) {
        super(radius);
        this.ray = ray;
    }

    @Override
    public Vector getNormal() {
        return null;
    }
}
