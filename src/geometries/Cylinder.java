package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

public class Cylinder extends Tube {
    /**cylinder class represents height in double
     * @param height
     */
    /**height of cylinder*/
    double height;
    /**get of height*/
    public double getHeight() {
        return height;
    }
    //parameters constructor
    public Cylinder(double radius, Ray ray, double height) {
        super(radius, ray);
        this.height = height;
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return super.findIntersections(ray);
    }

    @Override
    public Vector getNormal(Point point) {

        double tmp = axisRay.getDir().dotProduct(point.subtract(axisRay.getP0()));
        //on the base
        if (tmp == 0)
        {
            //in the center of each base
            if (point == axisRay.getP0())
                return axisRay.getDir();
            //v to -v
            Point point2 = point.add((axisRay.getDir().scale(-1)));
            return point2.subtract(point).normalize();
        }
        //on the upper base
        if (tmp==height)
        {
            //in the center of each base
            if (point.subtract(axisRay.getP0()).length() == height)
                return axisRay.getDir();
            Point point2 = point.add((axisRay.getDir()));//v
            return point2.subtract(point).normalize();
        }
        //on the round surface
        Point center = axisRay.getP0().add(axisRay.getDir().scale(tmp));
        return point.subtract(center).normalize();
    }
}
