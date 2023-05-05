package lighting;

import primitives.*;

/**
 * Ambient light foe all graphic object
 * @author Avital & Ahuva
 */
public class AmbientLight {
    private final Color intensity; //light intensity as Color
    public static final AmbientLight NONE = new AmbientLight();

    /**
     * primary constructor
     * @param Ia basic intensity light
     * @param Ka attenuation factor
     */
    public AmbientLight(Color Ia, Double3 Ka){
        intensity = Ia.scale(Ka);
    }

    /**
     * default constructor
     */
    public AmbientLight(){
        intensity = Color.BLACK;
    }

    /**
     * getter for intensity
     * @return actual intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}