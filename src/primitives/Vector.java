package primitives;

public class Vector extends Point {
    /**
     * package friendly constructor
     *
     * @param xyz Double3 value of vector head
     * @throws IllegalArgumentException in case of Vector(0,0,0)
     */
    Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector Zero not allowable");
    }

    /**
     * primary constructor for Vector
     * @param x value for x axis
     * @param y value for y axis
     * @param z value for z axis
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    @Override
    public String toString() {
        return "->" + super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector other)) return false;
        return super.equals(other);
    }

    public Vector add(Vector other) {
        return new Vector(xyz.add(other.xyz));
    }

    public Vector scale(double scalar) {   /**multiplication of vector with scalar*/
        return new Vector(xyz.scale(scalar));
    }

    public double dotProduct(Vector other)   /**scalar multiplication*/
    {
        return (xyz.d1 * other.xyz.d1) +   /**calculation of scalar multiplication with algebra*/
                (xyz.d2 * other.xyz.d2) +
                (xyz.d3 * other.xyz.d3);
    }

    public Vector crossProduct(Vector other)  /**vector multiplication*/
    {
        return new Vector((xyz.d2 * other.xyz.d3) - (xyz.d3 * other.xyz.d2),  /**calculation of vector multiplication in algebra*/
                (xyz.d3 * other.xyz.d1) - (xyz.d1 * other.xyz.d3),
                (xyz.d1 * other.xyz.d2) - (xyz.d2 * other.xyz.d1));
    }

    public double lengthSquared()  /**length of vector in a square*/
    {
        return dotProduct(this);
    }

    public double length() {    /**length of vector*/
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize() {   /**normalize of vector*/
        return scale(1 / length());
    }
}
