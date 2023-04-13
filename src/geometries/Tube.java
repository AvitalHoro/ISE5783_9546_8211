package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;


public class Tube extends RadialGeometry {
    Ray axisRay;   /**ray to representation tube*/

    public Ray getAxisRay() {   /**get axisRay*/
        return axisRay;
    }

    public Tube(double radius, Ray axisRay) {  /**parameters constructor*/
        super(radius);
        this.axisRay = axisRay;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
