package primitives;

import static primitives.Util.isZero;

/**
 * this class represents the different materials of the surfaces
 * and the reflection of a light component on it,
 * in three known values: diffusion, specular, and shininess.
 */
public class Material {

    /**
     *  Kd - diffuse component, represents the scattering of light rays to all directions from the surface
     */
    public Double3 Kd = Double3.ZERO;

    /**
     *  Ks - specular component, represents the reflectance of the light source over the surface
     */
    public Double3 Ks = Double3.ZERO;

    /**
     *  Kt - transparency component
     * 0.0 is opaque
     * 1.0 is clear
     */
    public Double3 Kt = Double3.ZERO;

    /**
     *  Kr - reflection component
     * 0.0 is matte
     * 1.0 is very reflexive
     */
    public Double3 Kr = Double3.ZERO;

    /**
     *  Shininess - how shiny the material is
     */
    public int nShininess = 0;

    /**
     *  glossiness - how glossy the material is
     */
    public double glossiness = 0;

    /**
     *  Shininess - how diffuse the material is
     */
    public double diffuseness = 0;


    //*********Setters*********

    /**
     * set (According to the builder design template)
     * @param ks typed double
     * @return this material
     */
    public Material setKs(double ks) {
        Ks = new Double3(ks);
        return this;
    }

    /**
     * set (According to the builder design template)
     * @param kd typed double
     * @return this material
     */
    public Material setKd(double kd) {
        this.Kd = new Double3(kd);
        return this;
    }

    /**
     * set (According to the builder design template)
     * @param ks typed Double3
     * @return this material
     */
    public Material setKs(Double3 ks) {
        Ks = ks;
        return this;
    }

    /**
     * set (According to the builder design template)
     * @param kd typed Double3
     * @return this material
     */
    public Material setKd(Double3 kd) {
        this.Kd = kd;
        return this;
    }

    /**
     * set (According to the builder design template)
     * @param nShininess
     * @return this material
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /**
     * set (According to the builder design template)
     * @param kt typed double
     * @return this material
     */
    public Material setKt(double kt) {
        this.Kt = new Double3(kt);
        return this;
    }

     /** set (According to the builder design template)
     * @param kr typed double
     * @return this material
     */
    public Material setKr(double kr) {
        this.Kr = new Double3(kr);
        return this;
    }

    /**
     * set (According to the builder design template)
     * @param kt typed Double3
     * @return this material
     */
    public Material setKt(Double3 kt) {
        this.Kt = kt;
        return this;
    }

     /**
     * set (According to the builder design template)
     * @param kr typed Double3
     * @return this material
     */
    public Material setKr(Double3 kr) {
        this.Kr = kr;
        return this;
    }

    public Material setGlossiness(double glossiness) {
        this.glossiness = glossiness;
        return this;
    }

    public Material setDiffuseness(double diffuseness) {
        this.diffuseness = diffuseness;
        return this;
    }

    //*********Getters*********

    public Double3 getKs() {
        return Ks;
    }

    public Double3 getKd() {
        return Kd;
    }

    public int getShininess() {
        return nShininess;
    }

    public Double3 getKt() {
        return Kt;
    }

    public Double3 getKr() {
        return Kr;
    }

    public boolean isDiffusive() {
        return !isZero(diffuseness);
    }

    public boolean isGlossy() {
        return !isZero(glossiness);
    }

}


