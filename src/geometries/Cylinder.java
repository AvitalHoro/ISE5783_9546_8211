package geometries;

import primitives.Vector;

public class Cylinder extends RadialGeometry {

    double h;

    public Cylinder(double radius, double h) {
        super(radius);
        this.h = h;
    }

    @Override
    public Vector getNormal() {
        return null;
    }
}
