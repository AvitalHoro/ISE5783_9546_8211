package renderer;

import lighting.LightSource;
import primitives.*;
import scene.Scene;

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
    private double sizeGrid=4;

    public void setDistanceGrid(double distanceGrid) {
        this.distanceGrid = distanceGrid;
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
     * @param ray the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    @Override
    public Color traceRay(Ray ray) {
        GeoPoint clossestGeoPoint = findClosestIntersection(ray);
        if (clossestGeoPoint == null)
            return scene.getBackground();
        return calcColor(clossestGeoPoint, ray);
    }
    /**
     * Trace the ray and calculates the color of the point that interact with the geometries of the scene
     * @param rays the ray that came out of the camera
     * @return the color of the object that the ray is interact with
     */
    @Override
    public Color TraceRays(List<Ray> rays) {
        Color color=new Color(BLACK);
        for(Ray ray : rays)
        {
            GeoPoint clossestGeoPoint = findClosestIntersection(ray);
            if (clossestGeoPoint == null)
                color= color.add(scene.getBackground());
            else color= color.add(calcColor(clossestGeoPoint,ray));
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
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint reflectedPoint = findClosestIntersection(ray);
        if (reflectedPoint != null) {
            color = color.add(calcColor(reflectedPoint, ray, level - 1, kkx).scale(kx));
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
        Color color = Color.BLACK;
        Material material = gp.geometry.getMaterial();
        Ray reflectedRay = constructReflectedRay(gp, gp.geometry.getNormal(gp.point), ray.getDir());
        Ray refractedRay = constructRefractedRay(gp, gp.geometry.getNormal(gp.point), ray.getDir());
        return calcGlobalEffects(gp, level, color, material.Kr, k, reflectedRay)
                .add(calcGlobalEffects(gp, level, color, material.Kt, k, refractedRay));
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


    /**
     * Building a beam of rays for transparency and reflection
     * @param ray The beam coming out of the camera
     * @param direction the vector
     * @param glossy The amount of gloss
     * @param n normal
     * @return Beam of rays
     */
    List<Ray> raysGrid(Ray ray, int direction, double glossy, Vector n) {
        int numOfRowCol = isZero(glossy) ? 1 : (int) Math.ceil(Math.sqrt(glossinessRaysNum));
        if (numOfRowCol == 1) return List.of(ray);
        Vector Vup;
        double Ax = Math.abs(ray.getDir().getX()), Ay = Math.abs(ray.getDir().getY()), Az = Math.abs(ray.getDir().getZ());
        if (Ax < Ay)
            Vup = Ax < Az ? new Vector(0, -ray.getDir().getZ(), ray.getDir().getY()) :
                    new Vector(-ray.getDir().getY(), ray.getDir().getX(), 0);
        else
            Vup = Ay < Az ? new Vector(ray.getDir().getZ(), 0, -ray.getDir().getX()) :
                    new Vector(-ray.getDir().getY(), ray.getDir().getX(), 0);
        Vector Vright = Vup.crossProduct(ray.getDir()).normalize();
        Point pc = ray.getPoint(distanceGrid);
        double step = glossy / sizeGrid;
        Point pij = pc.add(Vright.scale(numOfRowCol / 2 * -step)).add(Vup.scale(numOfRowCol / 2 * -step));
        Vector tempRayVector;
        Point Pij1;

        List<Ray> rays = new ArrayList<>();
        rays.add(ray);
        for (int i = 1; i < numOfRowCol; i++) {
            for (int j = 1; j < numOfRowCol; j++) {
                Pij1 = pij.add(Vright.scale(i * step)).add(Vup.scale(j * step));
                tempRayVector = Pij1.subtract(ray.getP0());
                if (n.dotProduct(tempRayVector) < 0 && direction == 1) //refraction
                    rays.add(new Ray(ray.getP0(), tempRayVector));
                if (n.dotProduct(tempRayVector) > 0 && direction == -1) //reflection
                    rays.add(new Ray(ray.getP0(), tempRayVector));
            }
        }

        return rays;
    }

    /**
     * Checks the color of the pixel with the help of individual rays and averages between
     * them and only if necessary continues to send beams of rays in recursion
     * @param centerP center pixl
     * @param Width Length
     * @param Height width
     * @param minWidth min Width
     * @param minHeight min Height
     * @param cameraLoc Camera location
     * @param Vright Vector right
     * @param Vup vector up
     * @param prePoints pre Points
     * @return Pixel color
     */
    @Override
    public Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints) {
        if (Width < minWidth * 2 || Height < minHeight * 2) {
            return this.traceRay(new Ray(cameraLoc, centerP.subtract(cameraLoc))) ;
        }

        List<Point> nextCenterPList = new LinkedList<>();
        List<Point> cornersList = new LinkedList<>();
        List<primitives.Color> colorList = new LinkedList<>();
        Point tempCorner;
        Ray tempRay;
        for (int i = -1; i <= 1; i += 2){
            for (int j = -1; j <= 1; j += 2) {
                tempCorner = centerP.add(Vright.scale(i * Width / 2)).add(Vup.scale(j * Height / 2));
                cornersList.add(tempCorner);
                if (prePoints == null || !isInList(prePoints, tempCorner)) {
                    tempRay = new Ray(cameraLoc, tempCorner.subtract(cameraLoc));
                    nextCenterPList.add(centerP.add(Vright.scale(i * Width / 4)).add(Vup.scale(j * Height / 4)));
                    colorList.add(traceRay(tempRay));
                }
            }
        }


        if (nextCenterPList == null || nextCenterPList.size() == 0) {
            return primitives.Color.BLACK;
        }


        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        for (primitives.Color color : colorList) {
            if (!tempColor.isAlmostEquals(color))
                isAllEquals = false;
        }
        if (isAllEquals && colorList.size() > 1)
            return tempColor;


        tempColor = primitives.Color.BLACK;
        for (Point center : nextCenterPList) {
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width/2,  Height/2,  minWidth,  minHeight ,  cameraLoc, Vright, Vup, cornersList));
        }
        return tempColor.reduce(nextCenterPList.size());
    }

    /**
     * Find a point in the list
     * @param pointsList the list
     * @param point the point that we look for
     * @return
     */
    private boolean isInList(List<Point> pointsList, Point point) {
        for (Point tempPoint : pointsList) {
            if(point.equals(tempPoint))
                return true;
        }
        return false;
    }
}