package renderer;

import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.List;

import static primitives.Util.alignZero;
import primitives.Ray;

/**
 * implementation of the abstract class RayTracerBase
 */
public class RayTracerBasic extends RayTracerBase {
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.0001;
    private static final double INITIAL_K = 1.0;
    private static final double EPS = 0.1;

    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    //region traceRay
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
    //endregion

    //region calcColor
    /**
     * calculate the color that needed to be returned from the pixel.
     *
     * @param gp  the point to calculate the color for.
     * @param ray the ray to pass to the function that summarise all the effects of the light sources.
     * @return the color to paint the pixel.
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        return scene.getAmbientLight().getIntensity()
                .add(calcColor(gp, ray, MAX_CALC_COLOR_LEVEL, new Double3(INITIAL_K)));
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
     * calculating a global effect color
     *
     * @param ray   the ray that intersects with the geometry.
     * @param level the remaining number of times to do the recursion.
     * @param k     the level of insignificance for the k.
     * @param kx    the attenuation factor of reflection or transparency
     * @return the calculated color.
     */
    private Color calcGlobalEffects(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = kx.product(k);
        //calculate the reflected ray, and the color contribution to the point.
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null || kkx.lowerThan(MIN_CALC_COLOR_K)) ? Color.BLACK
                : calcColor(gp, ray, level - 1, kkx).scale(kx);
    }

    /**
     * calculating the color in the global scene, tells what more points we need to
     * check for the color calculations.
     *
     * @param gp    the point of the intersection.
     * @param ray   the ray that intersects with the geometry.
     * @param level the remaining number of times to do the recursion.
     * @param k     the level of insignificance for the k.
     * @return the calculated color.
     */
    private Color calcGlobalEffects(GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector v = ray.getDir();
        Vector normal = gp.geometry.getNormal(gp.point);
        Material material = gp.geometry.getMaterial();

        Ray reflectedRay = constructReflectedRay(gp.point, v, normal);
        Ray refractedRay = constructRefractedRay(gp.point, v, normal);
        return calcGlobalEffects(reflectedRay, level, k, material.Kr)
                .add(calcGlobalEffects(refractedRay, level, k, material.Kt));
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
                Double3 ktr = transparency(geoPoint, lightVector, normal, lightSource);

                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(geoPoint.point).scale(ktr);
                    color = color.add(lightIntensity.scale(calcDiffusive(material, nl)), lightIntensity.
                            scale(calcSpecular(material, normal, lightVector, nl, vector)));
                }
            }
        }
        return color;
    }

    //region calcSpecular
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
    //endregion

    //region calcDiffusive
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
     * Determining how much light reach a certain point, blocked by other objects
     *
     * @param gp          the point we want to calculate the transparency for
     * @param l           vector from the light source
     * @param n           normal to the point
     * @param lightSource the light source
     * @return the transparency of the point
     */
    private Double3 transparency(GeoPoint gp, Vector l, Vector n, LightSource lightSource) {
        Vector lightDirection = l.scale(-1); // from point to light source
        //We make sure to move the object by DELTA
        //int the correct direction
        //Create a new ray to check shadiness
        Ray lightRay = new Ray(gp.point, lightDirection, n);

        Double3 ktr = Double3.ONE;
        //Find if any geometric object blocks the light
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(lightRay, lightSource.getDistance(lightRay.getP0()));
        if (intersections == null)
            return ktr;
        //For every geometric object in the list, scale by its transparency coefficient
        for (var geoPoint : intersections) {
            ktr = ktr.product(geoPoint.geometry.getMaterial().Kt);
            if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
        }
        return ktr;
    }
//    /**
//     * The method checks whether there is any object shading the light source from a
//     * point
//     *
//     * @param gp the point with its geometry
//     * @param ls light source
//     * @param l  direction from light to the point
//     * @param n normal vector to the surface of gp
//     * @param nv dotproduct between n and ray direction
//     * @return accumulated transparency attenuation factor
//     */
//
//    private boolean unshaded(GeoPoint gp, LightSource ls, Vector l, Vector n, double nv) {
//
//        Vector lightDirection = l.scale(-1); // from point to light source
//        double nl = n.dotProduct(lightDirection);
//
//        Vector delta = n.scale(nl > 0 ? EPS : -EPS);
//        Point pointRay = gp.point.add(delta);
//        Ray lightRay = new Ray(pointRay, lightDirection);
//
//        double maxDistance = ls.getDistance(gp.point);
//        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(lightRay, maxDistance);
//
//        if (intersections == null){
//            return true;
//        }
//
//        for (var item : intersections){
//            if (item.geometry.getMaterial().getKt().lowerThan(MIN_CALC_COLOR_K)){
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * The method checks whether there is any object shading the light source from a
//     * point
//     *
//     * @param gp the point with its geometry
//     * @param ls light source
//     * @param l  direction from light to the point
//     * @return accumulated transparency attenuation factor
//     */
//    private boolean unshaded(GeoPoint gp,LightSource ls, Vector l) {
//        Vector n = gp.geometry.getNormal(gp.point);
//
//        Vector lightDirection = l.scale(-1); // from point to light source
//        Ray lightRay = new Ray(gp.point, lightDirection, n);
//
//        double lightDistance = ls.getDistance(gp.point);
//        var intersections = scene.getGeometries().findGeoIntersections(lightRay, lightDistance);
//        if (intersections == null)
//            return true;
//
//        Double3 tr = Double3.ONE;
//        for (var geo : intersections) {
//            tr = tr.product(geo.geometry.getMaterial().getKt());
//            if (tr.lowerThan(MIN_CALC_COLOR_K))
//                return false;
//        }
//
//        return true;
//    }

}