package primitives;

public class Point {
    final Double3 xyz;

    Point(Double3 xyz) {
        this.xyz = xyz;
    }
    public Point(double x, double y, double z) {
        this.xyz=new Double3(x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Point other)
            return this.xyz.equals(other.xyz);
        return false;
    }

    @Override
    public String toString()
    {
        return xyz.toString();
    }

    public Point add(Vector v)
    {
        return new Point(xyz.add(v.xyz));
    }

    public Vector subtract(Point other){
        return new Vector(xyz.subtract(other.xyz));
    }

    double distanceSquared(Point other) {
        return (other.xyz.d1-xyz.d1)*(other.xyz.d1-xyz.d1) +
                (other.xyz.d2-xyz.d2)*(other.xyz.d2-xyz.d2) +
                (other.xyz.d3-xyz.d3)*(other.xyz.d3-xyz.d3);
    }

    double distance(Point other)
    {
        return Math.sqrt(distanceSquared(other));
    }
}
