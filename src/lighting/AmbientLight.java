package lighting;

import primitives.*;

/**
 *  this is a class that represents the environmental lightning in a scene
 */
public class AmbientLight
{
    /**
     *
     */
    Color intensity;

    /**
     * construct the ambient light using a color, and it's attenuation factor
     * @param Ia the base intensity of the light
     * @param Ka the attenuation factor of the intensity for each rgb color
     */
    public AmbientLight(Color Ia, Double3 Ka) {
        intensity = Ia.scale(Ka);
    }

    /**
     * construct the ambient light using a color, and it's attenuation factor
     * @param Ia
     * @param Ka
     */
    public AmbientLight(Color Ia, double Ka) {
        intensity = Ia.scale(Ka);
    }

    /**
     *
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);

    /**
     *
     * @return intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}