package primitives;

public class Point {
    /** point class represents by Double3
     * @param xyz
     */

    /** Double3 represents a point*/
    final Double3 xyz;

    /** constructor to initialize Point based Double3
     * @param xyz
     */
    public Point(Double3 xyz) {
        this.xyz = xyz;
    }


    /** constructor to initialize Point based object with its three number values
     *
     * @param x
     * @param y
     * @param z
     */
    public Point(double x, double y, double z) {
        this.xyz=new Double3(x, y, z);
    }

    /**
     * get of x
     * @return d1
     */
    public double getX(){
        return this.xyz.d1;
    }

    /**
     * get of y
     * @return d2
     */
    public double getY(){
        return this.xyz.d2;
    }

    /**
     * get of z
     * @return d3
     */
    public double getZ(){
        return this.xyz.d3;
    }

    @Override
    //return if two points are equals
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Point other)
            return this.xyz.equals(other.xyz);
        return false;
    }

    @Override
    //return xyz
    public String toString()
    {
        return xyz.toString();
    }

    /**
     * sum point and vector to a new point
     * @param v
     * @return  result of add
     */
    public Point add(Vector v)
    {
        return new Point(xyz.add(v.xyz));
    }

    /** subtract two points and return the vector result
     *
     * @param other
     * @return result of subtract
     */
    public Vector subtract(Point other){
        return new Vector(xyz.subtract(other.xyz));
    }

    /** calculates the squared distance of two points
     *
     * @param other
     * @return result of squared distance
     */
    public double distanceSquared(Point other) {
        return (other.xyz.d1-xyz.d1)*(other.xyz.d1-xyz.d1) +
                (other.xyz.d2-xyz.d2)*(other.xyz.d2-xyz.d2) +
                (other.xyz.d3-xyz.d3)*(other.xyz.d3-xyz.d3);
    }

    /**
     * calculates the distance between two points
     * @param other
     * @return result of distance
     */
    public double distance(Point other)
    {
        return Math.sqrt(distanceSquared(other));
    }
}
