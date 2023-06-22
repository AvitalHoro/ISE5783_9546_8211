package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * interface to light source
 */
public interface LightSource {
    /**
     *
     * @param p
     * @return Intensity
     */
    public Color getIntensity(Point p);

    /**
     * get L
     * @param p
     * @return Vector
     */
    public Vector getL(Point p);


    /**
     * Returns the distance from the light source to the given point.
     *
     * @param point The point to which the distance is to be calculated.
     * @return The distance between the point and the light source.
     */
    double getDistance(Point point);

}
