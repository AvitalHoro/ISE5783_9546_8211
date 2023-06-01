package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import static geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.alignZero;

/**
 * RayTracerBasic class that extends the RayTracer class
 */
public class RayTracerBasic extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

    /**
     * constructor that calls super constructor
     *
     * @param scene the scene to trace through
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

        /**
     * Given a ray, find the closest point of intersection with the scene, and return the color of that point
     *
     * @param ray The ray that we're tracing.
     * @return The color of the closest point.
     */
    @Override
    public Color traceRay(Ray ray) {
        GeoPoint closestPoint = findClosestIntersection(ray);
        if (closestPoint == null)
            return scene.getBackground();

        return calcColor(closestPoint, ray);
    }

    /**
     * calculate the color that needed to be returned from the pixel.
     *
     * @param gp  the point to calculate the color for.
     * @param ray the ray to pass to the function that summarise all the effects of the light sources.
     * @return the color to paint the pixel.
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return scene.getAmbientLight().getIntensity()
                .add(calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }


    /**
     * the entrance function to the recursive process of calculating the reflective effect and refractive effect.
     *
     * @param gp    the point of intersection that need the color calculation.
     * @param ray   the ray from the camera to that point.
     * @param level the remaining number of times to do the recursion.
     * @param k     the level of insignificance for the k.
     * @return the color of the pixel with all the refractions and reflections.
     */
    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(gp, ray, k);
        return level == 1 ? color : color.add(calcGlobalEffects(gp, ray, level, k));
    }

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
        //returns closest point
        return ray.findClosestGeoPoint(intersections);
    }

    /**
     * function calculates local effects of color on point
     *
     * @param geoPoint geometry point to color
     * @param ray      ray that intersects
     * @param k        k value
     * @return color
     */
    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray, Double3 k) {
        Color color = geoPoint.geometry.getEmission();
        Vector vector = ray.getDir();
        Vector normal = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = alignZero(normal.dotProduct(vector));
        if (nv == 0)
            return color;
        Material material = geoPoint.geometry.getMaterial();
        for (LightSource lightSource : scene.getLights()) {
            Vector lightVector = lightSource.getL(geoPoint.point);
            double nl = alignZero(normal.dotProduct(lightVector));
            if (nl * nv > 0) {
                Double3 ktr = transparency(geoPoint, lightSource, lightVector, normal);

                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(geoPoint.point).scale(ktr);
                    color = color.add(lightIntensity.scale(calcDiffusive(material, nl)),
                            lightIntensity.scale(calcSpecular(material, normal, lightVector, nl, vector)));
                }
            }
        }
        return color;
    }

    /**
     * calculate the next level of the global effects if there are more intersections to check
     * @param ray the is used to intersect the geometries
     * @param level the current level
     * @param kx a color factor to reduce the color (according to the current level of recursion)
     * @param kkx the color factor for the next level of recursion
     * @return the new calculated color
     */
    private Color calcGlobalEffects(Ray ray, int level, Double3 kx, Double3 kkx) {
        GeoPoint gp = scene.getGeometries().findClosestIntersection(ray);
        return (gp == null ? scene.getBackground() : calcColor(gp, ray, level - 1, kkx)).scale(kx);
    }

    /**
     * calculate the color according to the k factor for the reflection and refraction effects
     * @param gp calculate the color of this point
     * @param ray the ray of intersection that 'hit' the point
     * @param level of the recursion
     * @param k the volume of the color
     * @return the calculated color
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Vector n = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();

        Double3 kkr = k.product(material.Kr);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) { // the color is effected by the reflection
            Ray centerReflectedRay = constructReflectedRay(gp, n, ray.getDir());
            double glossiness = material.glossiness;

            if (material.isGlossy()){ // glossiness = glossy reflection
                RayBeam rayBeam = new RayBeam(centerReflectedRay).setSize(glossiness);
                List<Ray> rayList = rayBeam.constructRayBeam();
                int beamSize = rayList.size();

                for (Ray r : rayList) {
                    double nr = n.dotProduct(r.getDir());
                    double nc = n.dotProduct(centerReflectedRay.getDir());
                    if(nr * nc > 0) // the ray has to be in the normal direction to be reflected correctly
                        color = color.add(calcGlobalEffects(r, level, material.Kr, kkr));
                    else
                        beamSize--;
                }
                color = color.reduce(beamSize);
            }
            else
                color = calcGlobalEffects(centerReflectedRay, level, material.Kr, kkr);
        }

        Double3 kkt = k.product(material.Kr);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {// the color is effected due to the transparency
            Ray centerRefractedRay = constructRefractedRay(gp, n, ray.getDir());
            double diffuseness = material.diffuseness;

            if (material.isDiffusive()){ // diffuseness = diffusive refraction
                RayBeam rayBeam = new RayBeam(centerRefractedRay).setSize(diffuseness);
                List<Ray> rayList = rayBeam.constructRayBeam();
                int beamSize = rayList.size();
                for (Ray r : rayList) {
                    double nr = n.dotProduct(r.getDir());
                    double nc = n.dotProduct(centerRefractedRay.getDir());
                    if(nr * nc > 0)// the ray has to be in the opposite direction of the normal refracted correctly
                        color = color.add(calcGlobalEffects(r, level, material.Kr, kkt));
                    else
                        beamSize--;
                }
                color = color.reduce(beamSize); // average the color
            }
            else
                color = color.add(calcGlobalEffects(centerRefractedRay, level, material.Kr, kkt));
        }
        return color;
    }

//    /**
//     * calculating a global effect color
//     *
//     * @param ray   the ray that intersects with the geometry.
//     * @param level the remaining number of times to do the recursion.
//     * @param k     the level of insignificance for the k.
//     * @param kx    the attenuation factor of reflection or transparency
//     * @return the calculated color.
//     */
//    private Color calcGlobalEffects(GeoPoint geoPoint, int level, Color color, Double3 kx, Double3 k, Ray ray) {
//        Double3 kkx = kx.product(k);
//        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
//        GeoPoint reflectedPoint = findClosestIntersection(ray);
//        if (reflectedPoint != null) {
//            color = color.add(calcColor(reflectedPoint, ray, level - 1, kkx).scale(kx));
//        }
//        return color;
//    }
//
//    /**
//     * calculating the color in the global scene, tells what more points we need to
//     * check for the color calculations.
//     *
//     * @param gp    the point of the intersection.
//     * @param ray   the ray that intersects with the geometry.
//     * @param level the remaining number of times to do the recursion.
//     * @param k     the level of insignificance for the k.
//     * @return the calculated color.
//     */
//        private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
//        Color color = Color.BLACK;
//        Material material = gp.geometry.getMaterial();
//        Ray reflectedRay = constructReflectedRay(gp, gp.geometry.getNormal(gp.point), ray.getDir());
//        Ray refractedRay = constructRefractedRay(gp, gp.geometry.getNormal(gp.point), ray.getDir());
//        return calcGlobalEffects(gp, level, color, material.Kr, k, reflectedRay)
//                .add(calcGlobalEffects(gp, level, color, material.Kt, k, refractedRay));
//    }

        /**
     * function calculates specular color
     *
     * @param material    material of geometry
     * @param normal      normal of geometry
     * @param lightVector light vector
     * @param nl          dot product of normal and light vector
     * @param vector      direction of ray
     * @return specular color
     */
    private Double3 calcSpecular(Material material, Vector normal, Vector lightVector, double nl, Vector vector) {
        Vector reflectedVector = lightVector.subtract(normal.scale(2 * nl));
        double cosTeta = alignZero(-vector.dotProduct(reflectedVector));
        return cosTeta <= 0 ? Double3.ZERO : material.Ks.scale(Math.pow(cosTeta, material.nShininess));

    }

    /**
     * function calculates diffusive color
     *
     * @param material material of geometry
     * @param nl       dot product of normal and light vector
     * @return diffusive color
     */
    private Double3 calcDiffusive(Material material, double nl) {
        return material.Kd.scale(Math.abs(nl));
    }

    /**
     * function will construct a reflection ray
     *
     * @param gp geometry point to check
     * @param normal   normal vector
     * @param vector   direction of ray to point
     * @return reflection ray
     */
    private Ray constructReflectedRay(GeoPoint gp, Vector normal, Vector vector) {
        Vector reflectedVector = vector.subtract(normal.scale(2 * vector.dotProduct(normal)));
        return new Ray(gp.point, reflectedVector, normal);
    }

    /**
     * Construct and return a refracted ray
     *
     * @param gp The GeoPoint of intersection between the ray and the object
     * @param v the vector from the point to the light source
     * @param n the normal vector of the point of intersection
     * @return The refracted ray.
     */
    private Ray constructRefractedRay(GeoPoint gp, Vector v, Vector n) {
        return new Ray(gp.point, n, v);
    }
        /**
     * function will return double that represents transparency
     *
     * @param geoPoint    geometry point to check
     * @param lightSource light source
     * @param l           light vector
     * @param n           normal vector
     * @return transparency value
     */
    private Double3 transparency(GeoPoint geoPoint, LightSource lightSource, Vector l, Vector n) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(lightRay);

        Double3 ktr = Double3.ONE;
        if (intersections == null) return ktr;

        double distance = lightSource.getDistance(geoPoint.point);

        for (GeoPoint intersection : intersections) {

            if (distance > intersection.point.distance(geoPoint.point)) {
                ktr = ktr.product(intersection.geometry.getMaterial().Kt);
            }
        }
        return ktr;
    }
}
