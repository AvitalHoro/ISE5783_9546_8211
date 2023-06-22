package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;


public class Tube extends RadialGeometry {
    /** tube class extends RadialGeometry and represent by a ray
     * @param axisRay
     */

    /**
     * ray to representation tube
     */
    Ray axisRay;

    /** tube class extends RadialGeometry and represent by a ray
     * @return axisRay
     */
    public Ray getAxisRay() {
        return axisRay;
    }

    /** parameters constructor
     * @param radius
     * @param axisRay
     */
    public Tube(double radius, Ray axisRay) {
        super(radius);
        this.axisRay = axisRay;
    }

    /**
     * A method that receives a ray and checks the points of GeoIntersection of the ray with the tube
     * @param ray the ray received
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
            //if v and va are vertical
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

        // no intersections
        if (delta <= 0) {
            return null;
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

    /**
     * calculate the normal vector on specific point on the tube
     * @param point
     * @return Vector
     */
    @Override
    public Vector getNormal(Point point) {

        // 𝒕 = 𝒗 ∙ (𝑷 − 𝑷𝟎)
        // 𝑶 = 𝑷𝟎 + 𝒕 ∙ 𝒗
        //= 𝒏𝒐𝒓𝒎𝒂𝒍𝒊𝒛𝒆(𝑷 − 𝑶)
        double tmp = axisRay.getDir().dotProduct(point.subtract(axisRay.getP0()));
       //if the point on the axisRay
        if (tmp == 0)
            return point.subtract(axisRay.getP0()).normalize();
        Point center = axisRay.getP0().add(axisRay.getDir().scale(tmp));
        return point.subtract(center).normalize();

    }
}
