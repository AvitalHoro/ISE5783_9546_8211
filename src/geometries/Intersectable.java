package geometries;

import primitives.Point;
import primitives.Ray;

import java.util.List;

public interface Intersectable {
    /** Intersectable interface for geometry bodies to calculate intersections points between ray and body
     * @param ray
     * @return List<Point>
     */
    public List<Point> findIntersections(Ray ray);

 }
