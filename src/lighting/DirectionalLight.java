package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * representation if a directional light that has a direction, intensity and no attenuation
 */
public class DirectionalLight extends Light implements LightSource{
    private final Vector direction;

    /**region constructor
     * @param intensity
     * @param direction
     */
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }

    /**region getIntensity
     * @param p
     * @return
     */
    @Override
    public Color getIntensity(Point p) {
        return this.getIntensity();
    }

    /**region getL
     * @param point
     * @return
     */
    @Override
    public Vector getL(Point point){
        return this.direction;
    }

    /**
     * Returns the distance from the light source to the given point.
     *
     * @param point The point to which the distance is to be calculated.
     * @return The distance between the point and the light source.
     */
    @Override
    public double getDistance(Point point) {
        return Double.POSITIVE_INFINITY;
    }
}
