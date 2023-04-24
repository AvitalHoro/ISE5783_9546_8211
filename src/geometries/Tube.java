package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;


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

        Point o = ray.getP0();
        Vector d = ray.getDir();

        // vectors that span a space with the axis ray
        Vector local_z = axisRay.getDir().normalize();
        Vector local_x;
        try {
            local_x = d.crossProduct(local_z).normalize();
        } catch (IllegalArgumentException x) { // ray.dir == axisRay.dir
            return null;
        }
        Vector local_y = local_z.crossProduct(local_x).normalize();

        Vector w = o.subtract(axisRay.getP0());

        // projection of o and d on all 3 spanning vectors
        Vector o_local = new Vector(w.dotProduct(local_x), w.dotProduct(local_y), w.dotProduct(local_z));
        Vector d_local = new Vector(d.dotProduct(local_x), d.dotProduct(local_y), d.dotProduct(local_z));

        // parameters for the root formula
        double a = d_local.getY() * d_local.getY();
        double b = 2 * d_local.getY() * o_local.getY();
        double c = o_local.getY() * o_local.getY() + o_local.getX() * o_local.getX() - radius * radius;


        if (a == 0) {   // ray is parallel to axis ray - check the bases!!
            return null;
        }

        // root parameters
        double e = b * b - 4 * a * c;

        // complex solution - what does it mean??
        if (e < 0) {
            return null;
        }
        e = Math.sqrt(e);

        List<Point> pointList = new ArrayList<>();
        double t1 = (-b - e) / (2 * a);
        double t2 = (-b + e) / (2 * a);

        if (alignZero(t1) > 0) {
            pointList.add(ray.getPoint(t1));
        }
        if (alignZero(t2) > 0) {
            pointList.add(ray.getPoint(t2));
        }

        if (pointList.size() == 0)
            return null;
        return pointList;
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
