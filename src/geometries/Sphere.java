package geometries;

import primitives.Vector;

import primitives.Point;

public class Sphere extends RadialGeometry{
    Point center;

    public Sphere(double radius, Point center) {
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal() {
        return null;
    }
}
