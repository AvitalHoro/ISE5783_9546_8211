package primitives;

public class Ray {
    /** Ray class represents ray with Point and Vector
     * @param p0
     * @param dir
     */

    /**point to represent a ray*/
    final Point p0;

    /**vector to represent a ray*/
    final Vector dir;

    /**
     * constructor to initialize Ray based object with Point and Vector
     * @param p0
     * @param dir
     */
    public Ray(Point p0, Vector dir) {
        this.p0 = p0;
        this.dir = dir.normalize();
    }
    //get p0
    public Point getP0() {
        return p0;
    }

    //get dir
    public Vector getDir() {
        return dir;
    }

    @Override
    //return if two rays are equals
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Ray other)
            return this.p0.equals(other.p0) && this.dir.equals(other.dir);
        return false;
    }

    @Override
    //return the point and vector of ray
    public String toString() {
        return "Ray{" +
                "point: " + p0 +
                ", vector: " + dir +
                '}';
    }

    /**
     *
     * @param t - scalar to scale with
     * @return point
     */
    public Point getPoint(double t)
    {
        return p0.add(dir.scale(t));
    }
}
