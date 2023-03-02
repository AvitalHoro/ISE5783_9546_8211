package geometries;

import primitives.Ray;
import primitives.Vector;


public class Tube extends RadialGeometry {
    Ray axisRay;

    public Tube(double radius, Ray axisRay) {
        super(radius);
        this.axisRay = axisRay;
    }

    @Override
    public Vector getNormal() {
        return null;
    }
}
