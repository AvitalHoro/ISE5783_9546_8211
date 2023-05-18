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
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final double DELTA = 0.1;
    private static final Double3 INITIAL_K = Double3.ONE;
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
        return geoPoint == null ? scene.background : calcColor(geoPoint, ray);
    }
    //endregion

    //region calcColor

    /**
     * It calculates the color of a point on a surface, by calculating the color of the point, and adding the ambient light
     * to it
     *
     * @param closestPoint The closest point to the ray's head.
     * @param ray the ray that was sent from the camera to the scene
     * @return The color of the closest point.
     */
    private Color calcColor(GeoPoint closestPoint, Ray ray) {
        return calcColor(closestPoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K)
                .add(scene.ambientLight.getIntensity());
    }


    /**
     * It calculates the color of a point on the scene by calculating the local effects (diffuse, specular, ambient) and
     * adding them to the global effects (reflection and refraction)
     *
     * @param intersection The point of intersection between the ray and the object.
     * @param ray the ray that intersects the object
     * @param level the recursion level.
     * @param k how much to take the calculated color
     * @return The color of the intersection point.
     */
    private Color calcColor(GeoPoint intersection, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(intersection, ray);
        return 1 == level ? color : color.add(calcGlobalEffects(intersection, ray.getDir(), level, k));
    }

    /**
     * It calculates the color of the point by calculating the color of the reflected ray and the color of the refracted
     * ray
     *
     * @param gp The closest intersection point.
     * @param v the ray's direction
     * @param level the recursion level.
     * @param k how much to take the calculated color
     * @return The color of the point.
     */
    private Color calcGlobalEffects(GeoPoint gp, Vector v, int level, Double3 k) {
        Color color = Color.BLACK;
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();
        Double3 kkr = material.getKr().product(k);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K))
            color = calcGlobalEffect(constructReflectedRay(gp.point, v, n), level, material.getKr(), kkr);
        Double3 kkt = material.getKt().product(k);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K))
            color = color.add(
                    calcGlobalEffect(constructRefractedRay(gp.point, v, n), level, material.getKt(), kkt));
        return color;
    }

    /**
     * If there's no intersection, return the background color, otherwise return the color of the intersection point,
     * scaled by the kx(kt/kr) coefficient.
     *
     * The function is recursive, and the recursion stops when the level reaches 0
     *
     * @param ray the ray that we're currently tracing
     * @param level the recursion level.
     * @param kx how much to take the calculated color
     * @param kkx the attenuation factor of the light source
     * @return The color of the closest intersection point.
     */
    private Color calcGlobalEffect(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background : calcColor(gp, ray, level-1, kkx)
        ).scale(kx);
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
     * It finds the closest intersection point of a ray with the scene's geometries
     *
     * @param ray The ray that we want to find the closest intersection to.
     * @return The closest intersection point.
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(ray);
        if(intersections == null)
            return null;
        return ray.findClosestGeoPoint(intersections); //returns closest point
    }

    /**
     * Construct and return a reflected ray
     *
     * @param point The point of intersection between the ray and the object
     * @param v the vector from the point to the light source
     * @param n the normal vector of the point of intersection
     * @return The reflected ray.
     */
    private Ray constructReflectedRay(Point point, Vector v, Vector n) {
        double vn = v.dotProduct(n);

        if (vn == 0){
            return null;
        }

        // r = v - 2 *(v*n)*n
        Vector r = v.subtract(n.scale(2*vn));
        return new Ray(point, n, r);
    }

    /**
     * Construct and return a refracted ray
     *
     * @param point The point of intersection between the ray and the object
     * @param v the vector from the point to the light source
     * @param n the normal vector of the point of intersection
     * @return The refracted ray.
     */
    private Ray constructRefractedRay(Point point, Vector v, Vector n) {
        return new Ray(point, n, v);
    }

    /**
     * The function calculates the transparency of the point.
     *
     * @param gp          The point on the surface of the geometry
     * @param lightSource The light source
     * @param l           The vector from the point to the light source
     * @param n           The normal vector of the point
     * @return The transparent level of the point, between 0 and 1.
     */
    private Double3 transparency(GeoPoint gp, LightSource lightSource, Vector l, Vector n) {
        Vector lightDirection = l.scale(-1); // from point to light source

        Point point = gp.point;

        // This is a ray that is sent from the point to the light source.
        Ray lightRay = new Ray(point, n, lightDirection);

        // Calculates the maximum distance from the ray to the surface
        double maxDistance = lightSource.getDistance(point);

        // Get the intersections
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(lightRay, maxDistance);

        if (intersections == null)
            return Double3.ONE;

        Double3 ktr = Double3.ONE;
        // loop over intersections and for each intersection which is closer to the
        // point than the light source multiply ktr by 𝒌𝑻 of its geometry.
        // Performance: if you get close to 0 – it’s time to get out (return 0)
        for (var geo : intersections) {
            ktr = ktr.product(geo.geometry.getMaterial().getKt());
            if (ktr.lowerThan(MIN_CALC_COLOR_K))
                return Double3.ZERO;
        }
        return ktr;
    }


    /**
         * function will check if point is unshaded
         *
         * @param gp geometry point to check
         * @param l  light vector
         * @param n  normal vector
         * @return true if unshaded
         */
    private boolean unshaded(GeoPoint gp, LightSource lightSource, Vector l, Vector n) {
        Point point = gp.point;
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(point, n, lightDirection);

        // Calculates the maximum distance from the ray to the surface
        double maxDistance = lightSource.getDistance(point);

        // Get the intersections
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(lightRay, maxDistance);

        if (intersections == null)
            return true;

        for (var intersection: intersections) {
            if(intersection.geometry.getMaterial().getKt().lowerThan(MIN_CALC_COLOR_K))
                return false;
        }
        return true;
    }

}