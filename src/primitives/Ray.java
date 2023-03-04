package primitives;

public class Ray {
    final Point p0;   /**point to represent a ray*/
    final Vector dir;   /**vector to represent a ray*/

    public Ray(Point p0, Vector dir) {   /**parameters constructor*/
        this.p0 = p0;
        this.dir = dir.normalize();
    }
    public Point getP0() {    /**get p0*/
        return p0;
    }

    public Vector getDir() {   /**get dir*/
        return dir;
    }

    @Override
    public boolean equals(Object obj) {    /**return if two rays are equals*/
        if (this == obj) return true;
        if (obj instanceof Ray other)
            return this.p0.equals(other.p0) && this.dir.equals(other.dir);
        return false;
    }

    @Override
    public String toString() {   /**return the point and vector of ray*/
        return "Ray{" +
                "point: " + p0 +
                ", vector: " + dir +
                '}';
    }
}
