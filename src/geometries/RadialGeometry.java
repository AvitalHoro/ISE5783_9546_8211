package geometries;

//Interface for a circular geometric body (Temporary)
public abstract class RadialGeometry implements Geometry {
    protected double radius;     /**radius of Radial Geometry*/

    public double getRadius() {   /**get radius*/
        return radius;
    }

    public RadialGeometry(double radius) {   /**parameters constructor*/
        this.radius = radius;
    }
}
