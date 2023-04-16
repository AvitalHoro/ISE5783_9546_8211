package primitives;

public class Vector extends Point {
    /**
     * package friendly constructor
     *
     * @param xyz Double3 value of vector head
     * @throws IllegalArgumentException in case of Vector(0,0,0)
     */

    /**
     * constructor to initialize Vector based object in Double3
     * @param xyz
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

    /** sum two vectors to a new Vector
     * @param other
     * @return result of add
     */
    public Vector add(Vector other) {
        return new Vector(xyz.add(other.xyz));
    }

    /** multiplier of a vector in a double scalar
     *
     * @param scalar
     * @return result of multiplication
     */
    public Vector scale(double scalar) {   /**multiplication of vector with scalar*/
        return new Vector(xyz.scale(scalar));
    }

    /** does scalar multiplication
     * @param other
     * @return result of scalar multiplication
     */
    public double dotProduct(Vector other)
    {
        //calculation of scalar multiplication
        return (xyz.d1 * other.xyz.d1) +
                (xyz.d2 * other.xyz.d2) +
                (xyz.d3 * other.xyz.d3);
    }

    /** does vector multiplication
     *
     * @param other
     * @return result of vector multiplication
     */
    public Vector crossProduct(Vector other)
    {
        //calculation of vector multiplication in algebra
        return new Vector((xyz.d2 * other.xyz.d3) - (xyz.d3 * other.xyz.d2),
                (xyz.d3 * other.xyz.d1) - (xyz.d1 * other.xyz.d3),
                (xyz.d1 * other.xyz.d2) - (xyz.d2 * other.xyz.d1));
    }

    /** calculates the length squared of vector
     * @return length squared of vector
     */
    public double lengthSquared()
    {
        return dotProduct(this);
    }

    /** calculates the length of vector
     *
     * @return length of vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * normalizes the vector
     * @return normalized vector
     */
    public Vector normalize() {
        return scale(1 / length());
    }
}
