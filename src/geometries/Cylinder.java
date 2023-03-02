package geometries;

import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube {

    double height;

    public Cylinder(double radius, Ray ray, double height) {
        super(radius, ray);
        this.height = height;
    }

    @Override
    public Vector getNormal() {
        return null;
    }
}
