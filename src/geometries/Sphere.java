package geometries;

import primitives.Vector;

import primitives.Point;

public class Sphere extends RadialGeometry{
    Point center;    /**the center point of the sphere*/

    public Point getCenter() {  /**get center*/
        return center;
    }

    public Sphere(double radius, Point center) {  /**parameters constructor*/
        super(radius);
        this.center = center;
    }

    @Override
    public Vector getNormal(Point point) {
        //𝒏 = 𝒏𝒐𝒓𝒎𝒂𝒍𝒊𝒛𝒆(𝑷 − 𝑶)
        return point.subtract(center).normalize();
    }
}
