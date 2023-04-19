package unittest.geometries;
import static primitives.Util.*;
import geometries.Polygon;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {

    @Test

    /** Test method for {@link geometries.Sphere#getNormal(Point)} (primitives.Point)}. */

    public void testgetNormal()
    {
            // ============ Equivalence Partitions Tests ==============
            // TC01: There is a simple single test here - using a quad
            Sphere sphr = new Sphere(1, new Point(0, 0, 0));
            // ensure there are no exceptions
            assertDoesNotThrow(() -> sphr.getNormal(new Point(0, 0, 1)), "");
            // generate the test result
            Vector result = sphr.getNormal(new Point(0, 0, 1));
            // ensure |result| = 1
            assertEquals(1, result.length(), 0.00000001, "Sphere's normal is not a unit vector");
            // ensure the result is parallel to radius
            assertThrows(IllegalArgumentException.class, //
                () -> result.crossProduct((new Point(0, 0, 1).subtract(new Point(0, 0, 0)))),
                "Shpere's normal isn't parallel to radius");
    }

    /**
     * Test method for {@link Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(2, new Point (1, 0, 0));
        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-2, 0, 0), new Vector(2, 1, 0))),
                "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(-1,0,0);
        Point p2 = new Point(1, -2, 0);
        List<Point> result1 = sphere.findIntersections(new Ray(new Point(-2, 1, 0), new Vector(1, -1, 0)));
        assertEquals(2, result1.size(), "Wrong number of points");
        if (result.get(0).getX() > result1.get(1).getX())
            result = List.of(result1.get(1), result1.get(0));
        assertEquals(List.of(p1, p2), result1, "Ray crosses sphere in two points");

        // TC03: Ray starts inside the sphere (1 point)
        Point p3 = new Point(-1,0,0);
        List<Point> result2 = sphere.findIntersections(new Ray(new Point(1, 1, 0), new Vector(-2, -1, 0)));
        assertEquals(1, result2.size(), "Wrong number of points");
        assertEquals(List.of(p3), result2, "Ray crosses sphere in one point");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 3, 0), new Vector(0, 1, 0))),
                "Ray's line starts after the sphere");

        // =============== Boundary Values Tests ==================

        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        Point p4 = new Point(1,-2,0);
        List<Point> result3 = sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, -1, 0)));
        assertEquals(1, result3.size(), "Wrong number of points");
        assertEquals(List.of(p4), result3, "Ray starts at sphere and goes inside");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(-1, 1, 0))),
                "Ray's line starts after the sphere");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        Point p5 = new Point(3,0,0);
        Point p6 = new Point(-1,0,0);
        List<Point> result4 = sphere.findIntersections(new Ray(new Point(4, 0, 0), new Vector(-1, 0, 0)));
        assertEquals(2, result4.size(), "Wrong number of points");
        assertEquals(List.of(p5, p6), result4, "Ray starts before the sphere");

        // TC14: Ray starts at sphere and goes inside (1 point)
        Point p7 = new Point(-1,0,0);
        List<Point> result5 = sphere.findIntersections(new Ray(new Point(3, 0, 0), new Vector(-1, 0, 0)));
        assertEquals(1, result5.size(), "Wrong number of points");
        assertEquals(List.of(p7), result5, "Ray starts at sphere and goes inside");

        // TC15: Ray starts inside (1 point)
        Point p8 = new Point(-1,0,0);
        List<Point> result6= sphere.findIntersections(new Ray(new Point(2, 0, 0), new Vector(-1, 0, 0)));
        assertEquals(1, result6.size(), "Wrong number of points");
        assertEquals(List.of(p8), result6, "Ray starts inside");

        // TC16: Ray starts at the center (1 point)
        Point p9 = new Point(-1,0,0);
        List<Point> result7= sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1, 0, 0)));
        assertEquals(1, result7.size(), "Wrong number of points");
        assertEquals(List.of(p9), result7, "Ray starts at the center");

        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(-1, 0, 0))),
                "Ray's line starts at sphere and goes outside");

        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-2, 0, 0), new Vector(-1, 0, 0))),
                "Ray starts after sphere");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(-0.585786438, 4.4142213562, 0), new Vector(1, -1, 0))),
                "Ray starts before the tangent point");

        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2.4142213562, 1.4142213562, 0), new Vector(1, -1, 0))),
                "Ray starts at the tangent point");

        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(3.4142213562, 2.4142213562, 0), new Vector(1, -1, 0))),
                "Ray starts at the tangent point");

        // **** Group: Special cases
        // TC19: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(new Point(-2, 0, 0), new Vector(0, 1, 0))),
                "Ray starts at the tangent point");
    }
}