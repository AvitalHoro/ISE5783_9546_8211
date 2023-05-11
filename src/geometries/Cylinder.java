package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;

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

    /**
     *
     * @param ray
     * @param maxDistance
     * @return
     */
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        List<GeoPoint> helpIntersections = super.findGeoIntersectionsHelper(ray);

        List<GeoPoint> pointList = new ArrayList<>();

        if(helpIntersections != null) {
            for (GeoPoint geoPoint : helpIntersections) {
                Point point = geoPoint.point;
                double projection = point.subtract(axisRay.getP0()).dotProduct(axisRay.getDir());
                if (alignZero(projection) > 0 && alignZero(projection - this.height) < 0)
                    pointList.add(new GeoPoint(this, point));
            }
        }

        // intersect with base
        Circle base = new Circle(axisRay.getP0(), radius, axisRay.getDir());
        helpIntersections = base.findGeoIntersectionsHelper(ray);
        if(helpIntersections != null)
            pointList.add(new GeoPoint(this, helpIntersections.get(0).point));

        base = new Circle(axisRay.getPoint(height), radius, axisRay.getDir());
        helpIntersections = base.findGeoIntersectionsHelper(ray);
        if(helpIntersections != null)
            pointList.add(new GeoPoint(this, helpIntersections.get(0).point));

        if (pointList.size() == 0)
            return null;
        return pointList;
    }
    //endregion

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
