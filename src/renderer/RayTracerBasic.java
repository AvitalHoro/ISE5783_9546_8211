package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

import java.util.Random;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static geometries.Intersectable.GeoPoint;
import static java.awt.Color.BLACK;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * RayTracerBasic class that extends the RayTracer class
 */
public class RayTracerBasic extends RayTracerBase {

    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;
    private int glossinessRaysNum = 36;
    private double distanceGrid = 25;
    private double sizeGrid = 4;

    /**
     * set of distanceGrid
     * @param distanceGrid
     */
    public void setDistanceGrid(double distanceGrid) {
        this.distanceGrid = distanceGrid;
    }

    /**
     * set of distanceGrid
     * @param glossinessRaysNum
     */
    public void setGlossinessRaysNum(int glossinessRaysNum) {
        this.glossinessRaysNum = glossinessRaysNum;
    }

    /**
     * constructor that calls super constructor
     *
     * @param scene the scene to trace through
     */
    public RayTracerBasic(Scene scene) {
        super(scene);
    }

    /**
     * Trace the ray and calculates the color of the point that interact with the geometries of the scene
     *
     * @param ray the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    @Override
    public Color traceRay(Ray ray) {
        GeoPoint clossestGeoPoint = findClosestIntersection(ray);
        //if there is no closest GeoPoint between the ray and the point
        if (clossestGeoPoint == null)
            return scene.getBackground();
        //else- calculation the color of the point
        return calcColor(clossestGeoPoint, ray);
    }

    /**
     * Trace the list of ray and calculates the color of the point that interact with the geometries of the scene
     *
     * @param rays the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    @Override
    public Color TraceRays(List<Ray> rays) {
        Color color = new Color(BLACK);
        //over all the rays
        for (Ray ray : rays) {
            GeoPoint clossestGeoPoint = findClosestIntersection(ray);
            //if there is no closest intersection point with the shape
            if (clossestGeoPoint == null)
                color = color.add(scene.getBackground());
            //if there is intersection point
            else color = color.add(calcColor(clossestGeoPoint, ray));
        }
        return color.reduce(rays.size());
    }


    /**
     * calculate the color that needed to be returned from the pixel.
     *
     * @param gp  the point to calculate the color for.
     * @param ray the ray to pass to the function that summarise all the effects of the light sources.
     * @return the color to paint the pixel.
     */
    private Color calcColor(GeoPoint gp, Ray ray) {
        //ambient light + recursive calculation of the more color
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
        //calculate the local effects
        Color color = calcLocalEffects(gp, ray, k);
        //if level != 1 add global effects
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
        //if there is no GeoPoint that intersect with the ray
        if (intersections == null)
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
        //if the ray is parallel to the surface
        if (nv == 0)
            return color;
        Material material = geoPoint.geometry.getMaterial();
        //over about all the light sources
        for (LightSource lightSource : scene.getLights()) {
            Vector lightVector = lightSource.getL(geoPoint.point);
            double nl = alignZero(normal.dotProduct(lightVector));
            //the light source find in the same side of the surface as the ray
            if (nl * nv > 0) {
                //calculate the transparency factor
                Double3 ktr = transparency(geoPoint, lightSource, lightVector, normal);

                if (!ktr.product(k).lowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(geoPoint.point).scale(ktr);
                    //calculate the diffusive factor
                    color = color.add(lightIntensity.scale(calcDiffusive(material, nl)),
                            lightIntensity.scale(calcSpecular(material, normal, lightVector, nl, vector)));
                }
            }
        }
        return color;
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
        //there is no initial global lighting effect
        Color color = Color.BLACK;
        //the material properties
        Material material = gp.geometry.getMaterial();
        //building reflected ray
        Ray reflectedRay = constructReflectedRay(gp, gp.geometry.getNormal(gp.point), ray.getDir());
        //building refracted ray
        Ray refractedRay = constructRefractedRay(gp, gp.geometry.getNormal(gp.point), ray.getDir());

        //Returning the final color after calculating the reflection and refraction
        return calcGlobalEffects(gp, level, color, material.Kr, k, reflectedRay)
                .add(calcGlobalEffects(gp, level, color, material.Kt, k, refractedRay));
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
    private Color calcGlobalEffects(GeoPoint geoPoint, int level, Color color, Double3 kx, Double3 k, Ray ray) {
        Double3 kkx = kx.product(k);
        //if the reflection is negligible
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint reflectedPoint = findClosestIntersection(ray);
        //if there is intersection point between the ray and GeoPoint
        if (reflectedPoint != null) {
            color = color.add(calcColor(reflectedPoint, ray, level - 1, kkx).scale(kx));
        }
        return color;
    }

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
        //the reflection direction vector
        Vector reflectedVector = lightVector.subtract(normal.scale(2 * nl));
        //calculation of the cosine of the angle between the vector and the reflection vector
        double cosTeta = alignZero(-vector.dotProduct(reflectedVector));
        //if the angle > 90 => there is reflection
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
     * @param gp     geometry point to check
     * @param normal normal vector
     * @param vector direction of ray to point
     * @return reflection ray
     */
    private Ray constructReflectedRay(GeoPoint gp, Vector normal, Vector vector) {
        Vector reflectedVector = vector.subtract(normal.scale(2 * vector.dotProduct(normal)));
        //return of the reflected ray at this point
        return new Ray(gp.point, reflectedVector, normal);
    }

    /**
     * Construct and return a refracted ray
     *
     * @param gp The GeoPoint of intersection between the ray and the object
     * @param v  the vector from the point to the light source
     * @param n  the normal vector of the point of intersection
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
        // from point to the light source
        Vector lightDirection = l.scale(-1);
        Ray lightRay = new Ray(geoPoint.point, lightDirection, n);
        //find the intersections between scene and objects
        List<GeoPoint> intersections = scene.getGeometries().findGeoIntersections(lightRay);

        //initialize the transparency variable
        Double3 ktr = Double3.ONE;
        //if there is no intersections between the ray to point
        if (intersections == null) return ktr;

        double distance = lightSource.getDistance(geoPoint.point);

        //over about all the intersection with GeoPoint
        for (GeoPoint intersection : intersections) {

            //if the intersection blocks the light source
            if (distance > intersection.point.distance(geoPoint.point)) {
                ktr = ktr.product(intersection.geometry.getMaterial().Kt);
            }
        }
        return ktr;
    }

    /**
     * Checks the color of the pixel with the help of individual rays and averages between
     * them and only if necessary continues to send beams of rays in recursion
     * (credit to Rivki&Efrat)
     * @param centerP   center pixel
     * @param Width     Length
     * @param Height    width
     * @param minWidth  min Width
     * @param minHeight min Height
     * @param cameraLoc Camera location
     * @param Vright    Vector right
     * @param Vup       vector up
     * @param prePoints pre Points
     * @return Pixel color
     */
    @Override
    public Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight,
                                          Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints) {
        //check if the
        if (Width < minWidth * 2 || Height < minHeight * 2) {
            return this.traceRay(new Ray(cameraLoc, centerP.subtract(cameraLoc)));
        }

        //initialize list of following subpixel center points
        List<Point> nextCenterPList = new LinkedList<>();
        //initialize list of corners points
        List<Point> cornersList = new LinkedList<>();
        //initialize list of colors
        List<primitives.Color> colorList = new LinkedList<>();
        Point tempCorner;
        Ray tempRay;

        //over about all four corners
        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                //calculate the corner place
                tempCorner = centerP.add(Vright.scale(i * Width / 2)).add(Vup.scale(j * Height / 2));
                //add this corner to list of corners
                cornersList.add(tempCorner);
                //if this point is empty or does not exist
                if (prePoints == null || !isInList(prePoints, tempCorner)) {
                    //add the corner point and its color
                    tempRay = new Ray(cameraLoc, tempCorner.subtract(cameraLoc));
                    nextCenterPList.add(centerP.add(Vright.scale(i * Width / 4)).add(Vup.scale(j * Height / 4)));
                    colorList.add(traceRay(tempRay));
                }
            }
        }

        //if not all colors are equal
        if (nextCenterPList == null || nextCenterPList.size() == 0) {
            return primitives.Color.BLACK;
        }

        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        //over all the point in colorList
        for (primitives.Color color : colorList) {
            //if all the colors almost equals
            if (!tempColor.isAlmostEquals(color))
                isAllEquals = false;
        }
        //if all the colors does not equals ant there are some color
        if (isAllEquals && colorList.size() > 1)
            return tempColor;


        tempColor = primitives.Color.BLACK;
        //over about all the corner points
        for (Point center : nextCenterPList) {
            //recursive call to AdaptiveSuperSamplingRec
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width / 2, Height / 2,
                    minWidth, minHeight, cameraLoc, Vright, Vup, cornersList));
        }
        //return the average color
        return tempColor.reduce(nextCenterPList.size());
    }

    public Color RegularSuperSampling(Point centerP, double Width, double Height, double minWidth, double minHeight,
                                      Point cameraLoc, Vector Right, Vector Vup, List<Point> prePoints) {
        //initialize list of colors
        List<Color> colorList = new ArrayList<>();

        //calculate num of sub pixels
        int numSubPixelsX = (int) Math.ceil(Width / minWidth);
        int numSubPixelsY = (int) Math.ceil(Height / minHeight);

        //initialize random number
        Random random = new Random();

        //over all the sub pixels
        for (int i = 0; i < numSubPixelsY; i++) {
            for (int j = 0; j < numSubPixelsX; j++) {
                //calculate the coordinates of the place of this subpixel
                double offsetX = minWidth * j;
                double offsetY = minHeight * i;

                //calculate a random point in this subpixel
                double randomX = offsetX + random.nextDouble() * minWidth;
                double randomY = offsetY + random.nextDouble() * minHeight;

                //calculate the place of this subpixel
                Point subPixelPoint = centerP.add(Right.scale(randomX - Width / 2)).add(Vup.scale(randomY - Height / 2));

                //if this point is empty or does not exist
                if (prePoints == null || !isInList(prePoints, subPixelPoint)) {
                    //build ray from  camera to subpixel
                    Ray ray = new Ray(cameraLoc, subPixelPoint.subtract(cameraLoc));
                    colorList.add(traceRay(ray));
                }
            }
        }

        //if no colors are received
        if (colorList.isEmpty()) {
            //initialize black color
            return primitives.Color.BLACK;
        }

        Color averageColor = Color.BLACK;
        //over about all the colorList
        for (Color color : colorList) {
            //calculate the average color
            averageColor = averageColor.add(color);
        }
        //return the average color
        return averageColor.reduce(colorList.size());
    }

    /**
     * Find a point in the list
     *
     * @param pointsList the list
     * @param point      the point that we look for
     * @return
     */
    private boolean isInList(List<Point> pointsList, Point point) {
        //over about all the list of the points
        for (Point tempPoint : pointsList) {
            //if point == desired point
            if (point.equals(tempPoint))
                return true;
        }
        return false;
    }
}