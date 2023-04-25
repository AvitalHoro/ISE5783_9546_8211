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

    @Override
    public List<Point> findIntersections(Ray ray) {

        Point p0 = ray.getP0();
        Vector v = ray.getDir();
        Point pa = this.axisRay.getP0();
        Vector va = this.axisRay.getDir();

        double a, b, c; //coefficients for quadratic equation ax^2 + bx + c

        // a = (v-(v,va)va)^2
        // b = 2(v-(v,va)va,delP-(delP,va)va)
        // c = (delP-(delP,va)va)^2 - r^2
        // delP = p0-pa

        //note: (v,u) = v dot product u = vu = v*u

        //Step 1 - calculates a:
        Vector vecA = v;
        try {
            double vva = v.dotProduct(va); //(v,va)
            if (!isZero(vva)) vecA = v.subtract(va.scale(vva)); //v-(v,va)va
            a = vecA.lengthSquared(); //(v-(v,va)va)^2
        } catch (IllegalArgumentException e) {
            return null; //if a=0 there are no intersections because Ray is parallel to axisRay
        }

        //Step 2 - calculates deltaP (delP), b, c:
        try {
            Vector deltaP = p0.subtract(pa); //p0-pa
            Vector deltaPMinusDeltaPVaVa = deltaP;
            double deltaPVa = deltaP.dotProduct(va); //(delP,va)va)
            if (!isZero(deltaPVa)) deltaPMinusDeltaPVaVa = deltaP.subtract(va.scale(deltaPVa)); //(delP-(delP,va)va)
            b = 2 * (vecA.dotProduct(deltaPMinusDeltaPVaVa)); //2(v-(v,va)va,delP-(delP,va)va)
            c = deltaPMinusDeltaPVaVa.lengthSquared() - this.radius; //(delP-(delP,va)va)^2 - r^2
        } catch (IllegalArgumentException e) {
            b = 0; //if delP = 0, or (delP-(delP,va)va = (0, 0, 0)
            c = -1 * this.radius;
        }

        //Step 3 - solving the quadratic equation: ax^2 + bx + c = 0
        double discriminator = alignZero(b * b - 4 * a * c); //discriminator: b^2 - 4ac
        if (discriminator <= 0) return null; //there are no intersections because Ray is parallel to axisRay

        //the solutions for the equation: (-b +- discriminator) / 2a
        double sqrtDiscriminator = Math.sqrt(discriminator);
        double t1 = alignZero(-b + sqrtDiscriminator) / (2 * a);
        double t2 = alignZero(-b - sqrtDiscriminator) / (2 * a);

        //if t1 or t2 are bigger than maxDistance they wll be set to negative value
        //if (alignZero(t1 - maxDistance) > 0) t1 = -1;
        //if (alignZero(t2 - maxDistance) > 0) t2 = -1;

        //takes all positive solutions
        if (t1 > 0 && t2 > 0)
            return List.of(ray.getPoint(t1),ray.getPoint(t2));
        if (t1 > 0) return List.of(ray.getPoint(t1));
        if (t2 > 0) return List.of(ray.getPoint(t2));

        return null; //if there are no positive solutions

    }


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
