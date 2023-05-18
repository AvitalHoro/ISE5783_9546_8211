package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * implementation of the abstract class RayTracerBase
 */
public class RayTracerBasic extends RayTracerBase {
    private static final double DELTA = 0.1;
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    //region traceRay
    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersectionPoints = scene.geometries.findGeoIntersections(ray);
        if (intersectionPoints == null)
            return this.scene.background;
        GeoPoint geoPoint = ray.findClosestGeoPoint(intersectionPoints);
        return calcColor(geoPoint, ray);
    }
    //endregion

    //region calcColor

    /**
     * calculating the color of a specific point, taking into account the lightning,
     * transparency of the point itself and other affects of the surrounding are of the point in space
     *
     * @param geoPoint = GeoPoint calculate the color of this point
     * @param ray
     * @return for now - the ambient light's intensity
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity()        // the intensity of the ambient light
                .add(geoPoint.geometry.getEmission())   // the intensity of the geometry
                .add(calcLocalEffects(geoPoint, ray));  // the light sources effect on the intensity
    }

    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray) {
        Vector v = ray.getDir ();                                   // the vec from the camera to the geometry
        Vector n = geoPoint.geometry.getNormal(geoPoint.point);     // the normal to the geometry

        double nv = alignZero(n.dotProduct(v));
        if (nv == 0)                                                 // dot product resulted zero - the vectors are orthogonal
            return Color.BLACK;                                      // the camera doesn't see the light

        // the shine, diffuse and specular factors
        int nShininess = geoPoint.geometry.getMaterial().nShininess;
        Double3 kd = geoPoint.geometry.getMaterial().Kd;
        Double3 ks = geoPoint.geometry.getMaterial().Ks;
        Color color = Color.BLACK;                                  // the base color

        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(geoPoint.point);            // vec from the lightSource to the geometry
            double nl = alignZero(n.dotProduct(l));

            if (nl * nv > 0) { // sign(nl) == sing(nv) ->
                // the camera and the light source are on the same side of the surface
                Color lightIntensity = lightSource.getIntensity(geoPoint.point);    // the base intensity from the light source
                color = color.add(
                        calcDiffusive(kd, l, n, lightIntensity),                    // add the diffusion effect
                        calcSpecular(ks, l, n, v, nShininess, lightIntensity));     // add the specular effect
            }
        }
        return color;
    }

    //region calcSpecular
    /**
     * the specular effect on the object according to the phong reflection model
     * @param ks specular factor
     * @param l vec from the light source to a point on the geometry
     * @param n normal vec to the point on the geometry
     * @param v vec from the camera to the geometry = the camera's eye
     * @param nShininess shininess factor
     * @param lightIntensity intensity of the light
     * @return calculated intensity with the specular effect
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, Vector v, int nShininess, Color lightIntensity) {
        Vector r = l.subtract(n.scale(2 * l.dotProduct(n))).normalize();    // the specular ray

        // the phong model formula for the specular effect: ks ∙ ( 𝒎𝒂𝒙 (𝟎, −𝒗 ∙ 𝒓) ^ 𝒏𝒔𝒉 ) ∙ 𝑰
        return lightIntensity
                .scale(ks.scale(alignZero( Math.pow( Math.max(0, v.scale(-1).dotProduct(r)),
                        nShininess))));
    }
    //endregion

    //region calcDiffusive
    /**
     * the diffusion effect on the object according to the phong reflection model
     * @param kd diffusive factor
     * @param l vec from the light source to a point on the geometry
     * @param n vec from the light source to a point on the geometry
     * @param lightIntensity intensity of the light
     * @return calculated intensity with the diffusive effect
     */
    private Color calcDiffusive(Double3 kd, Vector l, Vector n, Color lightIntensity) {
        // the phong model formula for the diffusive effect: 𝒌𝑫 ∙| 𝒍 ∙ 𝒏 |∙ 𝑰
        return lightIntensity.scale((kd.scale(Math.abs(n.dotProduct(l)))));
    }
    //endregion

    /**
     * function will check if point is unshaded
     *
     * @param gp geometry point to check
     * @param l  light vector
     * @param n  normal vector
     * @return true if unshaded
     */
    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource, double nv) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Vector epsVector = n.scale(nv < 0 ? DELTA : -DELTA);
        Point point = gp.point.add(epsVector);
        Ray lightRay = new Ray(point, lightDirection);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        if (intersections != null) {
            double distance = lightSource.getDistance(gp.point);
            for (GeoPoint intersection : intersections) {
                if (intersection.point.distance(gp.point) < distance)
                    return false;
            }
        }

        return true;
    }

}