package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


public class Tube extends RadialGeometry {
    /** tube class extends RadialGeometry and represent by a ray
     * @paam axisRay
     */

    /**
     * ray to representation tube
     */
    Ray axisRay;

    //get axisRay
    public Ray getAxisRay() {
        return axisRay;
    }

    //parameters constructor
    public Tube(double radius, Ray axisRay) {
        super(radius);
        this.axisRay = axisRay;
    }

    /**
     * A method that receives a ray and checks the points of GeoIntersection of the ray with the tube
     *
     * @param ray the ray received
     *
     * @return null / list that includes all the GeoIntersection points (contains the geometry (shape) and the point in 3D)
     */
    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {

        /*
        The procedure is as follows:
        The equation for a tube of radius r oriented along a line pa + vat:
        (q - pa - (va,q - pa)va)2 - r2 = 0
        get intersections using formula : (p - pa + vt - (va,p - pa + vt)va)^2 - r^2 = 0
        reduces to at^2 + bt + c = 0
        with a = (v - (v,va)va)^2
             b = 2 * (v - (v,va)va,∆p - (∆p,va)va)
             c = (∆p - (∆p,va)va)^2 - r^2
        where  ∆p = p - pa
        */

        Vector v = ray.getDir();
        Vector va = this.getAxisRay().getDir();

        // if vectors are parallel then there is no intersections possible
        if (v.normalize().equals(va.normalize()))
            return null;

        // use of calculated variables to avoid vector ZERO
        double vva;
        double pva;
        double a;
        double b;
        double c;

        // check every variable to avoid ZERO vector
        if (ray.getP0().equals(this.getAxisRay().getP0())){
            vva = v.dotProduct(va);
            if (vva == 0){
                a = v.dotProduct(v);
            }
            else{
                a = (v.subtract(va.scale(vva))).dotProduct(v.subtract(va.scale(vva)));
            }
            b = 0;
            c = - getRadius() * getRadius();
        }
        else{
            Vector deltaP = ray.getP0().subtract(this.getAxisRay().getP0());
            vva = v.dotProduct(va);
            pva = deltaP.dotProduct(va);

            if (vva == 0 && pva == 0){
                a = v.dotProduct(v);
                b = 2 * v.dotProduct(deltaP);
                c = deltaP.dotProduct(deltaP) - getRadius() * getRadius();
            }
            else if (vva == 0){
                a = v.dotProduct(v);
                if (deltaP.equals(va.scale(deltaP.dotProduct(va)))){
                    b = 0;
                    c = - getRadius() * getRadius();
                }
                else{
                    b = 2 * v.dotProduct(deltaP.subtract(va.scale(deltaP.dotProduct(va))));
                    c = (deltaP.subtract(va.scale(deltaP.dotProduct(va))).dotProduct(deltaP.subtract(va.scale(deltaP.dotProduct(va))))) - this.getRadius() * this.getRadius();
                }
            }
            else if (pva == 0){
                a = (v.subtract(va.scale(vva))).dotProduct(v.subtract(va.scale(vva)));
                b = 2 * v.subtract(va.scale(vva)).dotProduct(deltaP);
                c = (deltaP.dotProduct(deltaP)) - this.getRadius() * this.getRadius();
            }
            else {
                a = (v.subtract(va.scale(vva))).dotProduct(v.subtract(va.scale(vva)));
                if (deltaP.equals(va.scale(deltaP.dotProduct(va)))){
                    b = 0;
                    c = - getRadius() * getRadius();
                }
                else{
                    b = 2 * v.subtract(va.scale(vva)).dotProduct(deltaP.subtract(va.scale(deltaP.dotProduct(va))));
                    c = (deltaP.subtract(va.scale(deltaP.dotProduct(va))).dotProduct(deltaP.subtract(va.scale(deltaP.dotProduct(va))))) - this.getRadius() * this.getRadius();
                }
            }
        }

        // calculate delta for result of equation
        double delta = b * b - 4 * a * c;

        if (delta <= 0) {
            return null; // no intersections
        }
        else {
            // calculate points taking only those with t > 0
            double t1 = alignZero((- b - Math.sqrt(delta)) / (2 * a));
            double t2 = alignZero((- b + Math.sqrt(delta)) / (2 * a));
            if (t1 > 0 && t2 > 0) {
                Point p1 = ray.getPoint(t1);
                Point p2 = ray.getPoint(t2);
                return List.of(new GeoPoint(this,p1),new GeoPoint(this, p2));
            }
            else if (t1 > 0) {
                Point p1 = ray.getPoint(t1);
                return List.of(new GeoPoint(this,p1));
            }
            else if (t2 > 0) {
                Point p2 = ray.getPoint(t2);
                return List.of(new GeoPoint(this,p2));
            }
        }
        return null;
    }

//    /**
//     *
//     * @param ray ray intersecting the geometry
//     * @return
//     */
//    @Override
//    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
//        Point p0 = ray.getP0();
//        Vector v = ray.getDir();
//        Point pa = this.axisRay.getP0();
//        Vector va = this.axisRay.getDir();
//
//        double a, b, c; //coefficients for quadratic equation ax^2 + bx + c
//
//        // a = (v-(v,va)va)^2
//        // b = 2(v-(v,va)va,delP-(delP,va)va)
//        // c = (delP-(delP,va)va)^2 - r^2
//        // delP = p0-pa
//
//        //note: (v,u) = v dot product u = vu = v*u
//
//        //Step 1 - calculates a:
//        Vector vecA = v;
//        try {
//            double vva = v.dotProduct(va); //(v,va)
//            if (!isZero(vva)) vecA = v.subtract(va.scale(vva)); //v-(v,va)va
//            a = vecA.lengthSquared(); //(v-(v,va)va)^2
//        } catch (IllegalArgumentException e) {
//            return null; //if a=0 there are no intersections because Ray is parallel to axisRay
//        }
//
//        //Step 2 - calculates deltaP (delP), b, c:
//        try {
//            Vector deltaP = p0.subtract(pa); //p0-pa
//            Vector deltaPMinusDeltaPVaVa = deltaP;
//            double deltaPVa = deltaP.dotProduct(va); //(delP,va)va)
//            if (!isZero(deltaPVa)) deltaPMinusDeltaPVaVa = deltaP.subtract(va.scale(deltaPVa)); //(delP-(delP,va)va)
//            b = 2 * (vecA.dotProduct(deltaPMinusDeltaPVaVa)); //2(v-(v,va)va,delP-(delP,va)va)
//            c = deltaPMinusDeltaPVaVa.lengthSquared() - this.radius; //(delP-(delP,va)va)^2 - r^2
//        } catch (IllegalArgumentException e) {
//            b = 0; //if delP = 0, or (delP-(delP,va)va = (0, 0, 0)
//            c = -1 * this.radius;
//        }
//
//        //Step 3 - solving the quadratic equation: ax^2 + bx + c = 0
//        double discriminator = alignZero(b * b - 4 * a * c); //discriminator: b^2 - 4ac
//        if (discriminator <= 0) return null; //there are no intersections because Ray is parallel to axisRay
//
//        //the solutions for the equation: (-b +- discriminator) / 2a
//        double sqrtDiscriminator = Math.sqrt(discriminator);
//        double t1 = alignZero(-b + sqrtDiscriminator) / (2 * a);
//        double t2 = alignZero(-b - sqrtDiscriminator) / (2 * a);
//
//        //if t1 or t2 are bigger than maxDistance they wll be set to negative value
//        //if (alignZero(t1 - maxDistance) > 0) t1 = -1;
//        //if (alignZero(t2 - maxDistance) > 0) t2 = -1;
//
//        //takes all positive solutions
//        if (t1 > 0 && t2 > 0)
//            return List.of(new GeoPoint(this, ray.getPoint(t1)),new GeoPoint(this, ray.getPoint(t2)));
//        if (t1 > 0) return List.of(new GeoPoint(this, ray.getPoint(t1)));
//        if (t2 > 0) return List.of(new GeoPoint(this, ray.getPoint(t2)));
//
//        return null; //if there are no positive solutions
//    }
//
//
    @Override
    public Vector getNormal(Point point) {

        // 𝒕 = 𝒗 ∙ (𝑷 − 𝑷𝟎)
        // 𝑶 = 𝑷𝟎 + 𝒕 ∙ 𝒗
        //= 𝒏𝒐𝒓𝒎𝒂𝒍𝒊𝒛𝒆(𝑷 − 𝑶)
        double tmp = axisRay.getDir().dotProduct(point.subtract(axisRay.getP0()));
        if (tmp == 0)
            return point.subtract(axisRay.getP0()).normalize();
        Point center = axisRay.getP0().add(axisRay.getDir().scale(tmp));
        return point.subtract(center).normalize();

    }
}
