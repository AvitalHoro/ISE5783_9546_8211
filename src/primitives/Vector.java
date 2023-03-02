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

    public Vector add(Vector other) {
        return new Vector(xyz.add(other.xyz));
    }

    public Vector scale(double scalar){
        return new Vector(xyz.scale(scalar));
    }

    public double dotProduct(Vector other)
    {
        return (xyz.d1*other.xyz.d1) +
                (xyz.d2*other.xyz.d2) +
                (xyz.d3*other.xyz.d3);
    }

    public Vector crossProduct(Vector other)
    {
        return new Vector((xyz.d2*other.xyz.d3) - (xyz.d3*other.xyz.d2),
                (xyz.d3*other.xyz.d1)-(xyz.d1*other.xyz.d3),
                (xyz.d1*other.xyz.d2)-(xyz.d2*other.xyz.d1));
    }
    public double lengthSquared()
    {
        return dotProduct(this);
    }

    public double length(){
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize(){
        return scale(1/length());
    }
}
