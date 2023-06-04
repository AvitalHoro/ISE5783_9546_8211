package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
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

    /**
     *
     * @param ray
     * @param maxDistance
     * @return
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {
        // The procedure is as follows:
        // P1 and P2 in the cylinder, the center of the bottom and upper bases
        Point p1 = axisRay.getP0();
        Point p2 = axisRay.getPoint(height);
        Vector Va = axisRay.getDir();


        List<GeoPoint> list = super.findGeoIntersectionsHelper(ray,maxDistance);

        // the intersections with the cylinder
        List<GeoPoint> result = new LinkedList<>();

        // Step 1 - checking if the intersections with the tube are points on the cylinder
        if (list != null) {
            for (GeoPoint p : list) {
                if (Va.dotProduct(p.point.subtract(p1)) > 0 && Va.dotProduct(p.point.subtract(p2)) < 0)
                    result.add(0, p);
            }
        }

        // Step 2 - checking the intersections with the bases

        // cannot be more than 2 intersections
        if(result.size() < 2) {
            //creating 2 planes for the 2 bases
            Plane bottomBase = new Plane(p1, Va);
            Plane upperBase = new Plane(p2, Va);
            GeoPoint p;

            // ======================================================
            // intersection with the bases:

            // intersections with the bottom bases
            list = bottomBase.findGeoIntersections(ray);

            if (list != null) {
                p = list.get(0);
                // checking if the intersection is on the cylinder base
                if (p.point.distanceSquared(p1) < radius * radius)
                    result.add(p);
            }

            // intersections with the upper bases
            list = upperBase.findGeoIntersections(ray);

            if (list != null) {
                p = list.get(0);
                //checking if the intersection is on the cylinder base
                if (p.point.distanceSquared(p2) < radius * radius)
                    result.add(p);
            }
        }
        // return null if there are no intersections.
        return result.size() == 0 ? null : result;
    }

    /**
     *
     * @param point
     * @return
     */
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
