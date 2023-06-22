package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.isZero;

/**
 * The SpotLight object specifies an attenuated light source at a fixed point in space that radiates light
 * in a specified direction from the light source. A SpotLight has the same attributes as a PointLight node,
 * with the addition of the following:
 * <p>
 * Direction - The axis of the cone of light.
 * <p>
 * Concentration exponent - Specifies how quickly the light intensity attenuates.
 * It attenuates as a function of the angle of radiation as measured from the direction of radiation.
 * The light's intensity is highest at the center of the cone and is attenuated toward the edges of the cone
 * by the cosine of the angle between the direction of the light and the direction from the light
 * to the object being lit, raised to the power of the spot concentration exponent.
 * The higher the concentration value, the more focused the light source.
 * <p>
 * A spot light contributes to diffuse and specular reflections, which depend on the orientation and position
 * of an object's surface. A spot light does not contribute to ambient reflections.
 */
public class SpotLight extends PointLight {

    private final Vector direction;
    private double narrowBeam = 1d;

    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }

    /**
     * get intensify of the light
     * @param point
     * @return
     */
    @Override
    public Color getIntensity(Point point) {
        Color Ic = super.getIntensity(point);
        double projection = getL(point).dotProduct(direction);

        if (isZero(projection)) {
            return Color.BLACK;
        }

        double factor = Math.max(0,projection);

        factor = Math.pow(factor, narrowBeam);
        return Ic.scale(factor);
    }

    /**
     * get narrowBeam
     * @return narrowBeam
     */
    public double getNarrowBeam() {
        return narrowBeam;
    }

    /**
     * set narrowBeam
     * @param narrow
     * @return this object
     */
    public PointLight setNarrowBeam(double narrow) {
        narrowBeam = narrow;
        return this;
    }
}