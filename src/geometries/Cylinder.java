package geometries;

import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube {

    double height;        /**height of cylinder*/

    public double getHeight() {    /**get of height*/
        return height;
    }

    public Cylinder(double radius, Ray ray, double height) {    /**parameters constructor*/
        super(radius, ray);
        this.height = height;
    }

    @Override
    public Vector getNormal() {
        return null;
    }
}
