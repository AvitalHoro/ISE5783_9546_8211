package geometries;

//Interface for a circular geometric body (Temporary)
public abstract class RadialGeometry extends Geometry {
    /** RadialGeometry abstract class represents circular geometric body
     * @param radius
     */

    /**radius of circular radial geometry*/
    protected double radius;


    /**
     *
     * @return radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * constructor
     * @param radius
     */
    public RadialGeometry(double radius) {
        this.radius = radius;
    }
}
