package lighting;

import primitives.Color;

/**
 *
 */
abstract class Light {
    /**
     * intensity of the light
     */
    private Color intensity;

    /**
     * constructor
     * @param intensity
     */
    protected Light(Color intensity) {
        this.intensity = intensity;
    }

    /**
     * get. intensity don't have set.
     * @return intensity
     */
    public Color getIntensity() {
        return intensity;
    }
}
