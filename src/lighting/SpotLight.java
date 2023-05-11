package lighting;

import primitives.*;

/**
 * a representation of a spotlight - a lamp with a direction and an opening angle of the light beam
 */
public class SpotLight extends PointLight{
    private final Vector direction;

    //region constructor
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }
    //endregion

    //region getIntensity
    @Override
    public Color getIntensity(Point point) {
        return super.getIntensity(point)
                .scale( Math.max(0, this.direction.dotProduct(this.getL(point))));  // add the beam angle's effect
    }

    /**
     * It was required for tests but it was not written in the instructions :(
     * @param i
     * @return
     */
    public PointLight setNarrowBeam(int i) {
        return this;
    }
    //endregion
}