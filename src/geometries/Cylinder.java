package geometries;

import primitives.Point;
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
    public Vector getNormal(Point point) {

        double tmp = axisRay.getDir().dotProduct(point.subtract(axisRay.getP0()));
        if (tmp == 0)//on the base
        {
            if (point == axisRay.getP0())//in the center of each base
                return axisRay.getDir();
            Point point2 = point.add((axisRay.getDir().scale(-1)));//-v
            return point2.subtract(point).normalize();
        }
        if (tmp==height)//on the upper base
        {
            if (point.subtract(axisRay.getP0()).length() == height)//in the center of each base
                return axisRay.getDir();
            Point point2 = point.add((axisRay.getDir()));//v
            return point2.subtract(point).normalize();
        }
        Point center = axisRay.getP0().add(axisRay.getDir().scale(tmp));//on the round surface
        return point.subtract(center).normalize();
    }
}
