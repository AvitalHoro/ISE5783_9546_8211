package primitives;

public class Vector extends Point {
    Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector Zero not allowable");
    }

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector Zero not allowable");
    }

    @Override
    public String toString() {
        return "Vector{" +
                xyz +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Vector add(Vector other) {
        return new Vector(xyz.add(other.xyz));
    }

    public Vector scale(double scalar){   /**multiplication of vector with scalar*/
        return new Vector(xyz.scale(scalar));
    }

    public double dotProduct(Vector other)   /**scalar multiplication*/
    {
        return (xyz.d1*other.xyz.d1) +   /**calculation of scalar multiplication with algebra*/
                (xyz.d2*other.xyz.d2) +
                (xyz.d3*other.xyz.d3);
    }

    public Vector crossProduct(Vector other)  /**vector multiplication*/
    {
        return new Vector((xyz.d2*other.xyz.d3) - (xyz.d3*other.xyz.d2),  /**calculation of vector multiplication in algebra*/
                (xyz.d3*other.xyz.d1)-(xyz.d1*other.xyz.d3),
                (xyz.d1*other.xyz.d2)-(xyz.d2*other.xyz.d1));
    }
    public double lengthSquared()  /**length of vector in a square*/
    {
        return dotProduct(this);
    }

    public double length(){    /**length of vector*/
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize(){   /**normalize of vector*/
        return scale(1/length());
    }
}
