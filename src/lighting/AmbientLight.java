package lighting;

import primitives.*;

/**
 *  this is a class that represents the environmental lightning in a scene
 */
public class AmbientLight extends Light
{
    /**
     * construct the ambient light using a color, and it's attenuation factor with dad constructor.
     * @param Ia the base intensity of the light
     * @param Ka the attenuation factor of the intensity for each rgb color
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        super(Ia.scale(Ka));
    }
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

}