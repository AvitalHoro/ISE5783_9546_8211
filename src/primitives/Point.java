package primitives;

public class Point {
    final Double3 xyz;   /** Double3 represents a point*/

    Point(Double3 xyz) {    /**constructor with Double3*/
        this.xyz = xyz;
    }
    public Point(double x, double y, double z) {  /**constructor with 3 coordinates*/
        this.xyz=new Double3(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {   /**return if two points are equals*/
        if (this == obj) return true;
        if (obj instanceof Point other)
            return this.xyz.equals(other.xyz);
        return false;
    }

    @Override
    public String toString()    /**return xyz*/
    {
        return xyz.toString();
    }

    public Point add(Vector v)    /**add vector to point*/
    {
        return new Point(xyz.add(v.xyz));
    }

    public Vector subtract(Point other){   /**vector subtraction*/
        return new Vector(xyz.subtract(other.xyz));
    }

    double distanceSquared(Point other) {   /**distance between two points in a square*/
        return (other.xyz.d1-xyz.d1)*(other.xyz.d1-xyz.d1) +     /**calculation of the square distance*/
                (other.xyz.d2-xyz.d2)*(other.xyz.d2-xyz.d2) +
                (other.xyz.d3-xyz.d3)*(other.xyz.d3-xyz.d3);
    }

    double distance(Point other)   /**distance between two points*/
    {
        return Math.sqrt(distanceSquared(other));
    }
}
