package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

/**
 * representation if a directional light that has a direction, intensity and no attenuation
 */
public class DirectionalLight extends Light implements LightSource{
    private final Vector direction;

    //region constructor
    public DirectionalLight(Color intensity, Vector direction) {
        super(intensity);
        this.direction = direction.normalize();
    }
    //endregion

    //region getIntensity
    @Override
    public Color getIntensity(Point p) {
        return this.getIntensity();
    }
    //endregion

    //region getL
    @Override
    public Vector getL(Point point){
        return this.direction;
    }
    //endregion
}
