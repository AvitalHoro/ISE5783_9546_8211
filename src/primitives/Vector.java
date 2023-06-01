package primitives;
import org.ejml.data.SimpleMatrix;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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

    /** calculate ant return orthogonal vector
     * @return result of vector multiplication
     */    public Vector getOrthogonal(){
        Vector orthogonal;
        if(!isZero(this.getX()) || !isZero(this.getY()))
            orthogonal = new Vector(this.getY(),-this.getX(), 0);
        else
            orthogonal = new Vector(0,-this.getZ(), this.getY());
        return orthogonal;
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

    /**
     * rotating a vector around a requested vector, in an angle requested
     * using the rotation matrix method
     * the calling vector is the vector that will be moved due to the rotation
     * @param rotationVector the vector the space will rotate around, orthogonal to the calling vector
     * @param angle the size of the rotation - angle in degrees
     * @return vectorToMove normalized and rotated to the left with the requested angle
     */
    public Vector moveClockwiseAround(Vector rotationVector, double angle) {
        if (!isZero(this.dotProduct(rotationVector)))
            throw new IllegalArgumentException("the vectors are not orthogonal");

        // convert the angle to radians
        angle = Math.toRadians(angle);
        // the third vector to span the space to rotate
        Vector orthogonalVector = this.crossProduct(rotationVector);

        SimpleMatrix matrixP = new SimpleMatrix(3,3);
        // fill the P matrix -
        // the conversion matrix between the standard base (E) to our base (F)
        matrixP.set(0,0, rotationVector.getX());
        matrixP.set(1,0, rotationVector.getY());
        matrixP.set(2,0, rotationVector.getZ());
        matrixP.set(0,1, this.getX());
        matrixP.set(1,1, this.getY());
        matrixP.set(2,1, this.getZ());
        matrixP.set(0,2, orthogonalVector.getX());
        matrixP.set(1,2, orthogonalVector.getY());
        matrixP.set(2,2, orthogonalVector.getZ());

        SimpleMatrix matrixA = new SimpleMatrix(3,3);
        // fill the A matrix -
        // the copy matrix in our base (F)
        matrixA.set(0,0, 1);
        matrixA.set(1,0, 0);
        matrixA.set(2,0, 0);
        matrixA.set(0,1, 0);
        matrixA.set(1,1, alignZero(Math.cos(angle)));
        matrixA.set(2,1, alignZero(Math.sin(angle)));
        matrixA.set(0,2, 0);
        matrixA.set(1,2, -alignZero(Math.sin(angle)));
        matrixA.set(2,2, alignZero(Math.cos(angle)));

        // the invertible matrix -
        // the conversion matrix on the opposite way from matrixP,
        // from our base (F) to the standard base (E)
        SimpleMatrix matrixInvertP = matrixP.invert();

        // the full copy matrix from the standard base (E) to the standard base (E)
        SimpleMatrix copyMatrix = matrixP.mult(matrixA).mult(matrixInvertP);

        // convert the vector that is going to be copied from a vector to a matrix ot execute the matrix multiplication
        SimpleMatrix matrixVectorToMove = new SimpleMatrix(3,1);
        matrixVectorToMove.set(0,0,this.getX());
        matrixVectorToMove.set(1,0,this.getY());
        matrixVectorToMove.set(2,0,this.getZ());

        // the converted vector in a form of a matrix
        matrixVectorToMove = copyMatrix.mult(matrixVectorToMove);

        // convert the matrix to a vector
        return new Vector(matrixVectorToMove.get(0,0),
                matrixVectorToMove.get(1,0),
                matrixVectorToMove.get(2,0)).normalize();
    }
}
