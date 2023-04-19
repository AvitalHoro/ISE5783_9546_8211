package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;


public class Tube extends RadialGeometry {
    /** tube class extends RadialGeometry and represent by a ray
     * @paam axisRay
     */

    /**ray to representation tube*/
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
        return null;
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
