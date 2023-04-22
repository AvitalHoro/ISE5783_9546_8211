package unittest.geometries;
import static primitives.Util.*;
import geometries.Polygon;
import geometries.Sphere;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SphereTest {


    /** Test method for {@link geometries.Sphere#getNormal(Point)} (primitives.Point)}. */
    @Test
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
            assertThrows(IllegalArgumentException.class,
                () -> result.crossProduct((new Point(0, 0, 1).subtract(new Point(0, 0, 0)))),
                "Shpere's normal isn't parallel to radius");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(primitives.Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1d, new Point (1, 0, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),
                "Ray's line out of sphere");

        // TC02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(0.0651530771650466, 0.355051025721682, 0);
        Point p2 = new Point(1.53484692283495, 0.844948974278318, 0);
        List<Point> result = sphere.findIntersections(new Ray(new Point(-1, 0, 0),
                new Vector(3, 1, 0)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere at two points");

        // TC03: Ray starts inside the sphere (1 point)
        Point p3 = new Point(1.5773502692, 1.5773502692, 1.5773502692);
        List<Point> result1 = sphere.findIntersections(new Ray(new Point(0.5, 0.5, 0.5),
                new Vector(1.077350269, 1.077350269, 1.077350269)));
        assertEquals(1, result1.size(), "Wrong number of points");
        assertEquals(List.of(p3), result1, "Ray crosses sphere at one point");
        //...

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(-3, -1, 0))),
                "Ray's line out of sphere");
        //...

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        List<Point> result2 = sphere.findIntersections(new Ray(new Point(1.267261242, 0.5345224838, 0.8017837257),
                new Vector(0.3100890272, 0.04282778539, -0.2244334565)));
        assertEquals(1, result2.size(), "Wrong number of points");
        assertEquals(List.of(p3), result2, "Ray crosses sphere at one point");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(p3, new Vector(1, 1, 1))),
                "Ray's line out of sphere");

        // **** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        Point p4 = new Point(1, 0, -1);
        Point p5 = new Point(1, 0, 1);
        List<Point> result3 = sphere.findIntersections(new Ray(new Point(1, 0, -2),
                new Vector(0, 0, 1)));
        assertEquals(2, result3.size(), "Wrong number of points");
        if (result3.get(0).getX() > result3.get(1).getX())
            result3 = List.of(result3.get(1), result3.get(0));
        assertEquals(List.of(p4, p5), result3, "Ray crosses sphere at two points");

        // TC14: Ray starts at sphere and goes inside (1 point)
        List<Point> result4 = sphere.findIntersections(new Ray(p4, new Vector(0, 0, 1)));
        assertEquals(1, result4.size(), "Wrong number of points");
        assertEquals(List.of(p5), result4, "Ray crosses sphere at one point");

        // TC15: Ray starts inside (1 point)
        List<Point> result5 = sphere.findIntersections(new Ray(new Point(1, 0, -0.5), new Vector(0, 0, 1)));
        assertEquals(1, result5.size(), "Wrong number of points");
        assertEquals(List.of(p5), result5, "Ray crosses sphere at one point");

        // TC16: Ray starts at the center (1 point)
        List<Point> result6 = sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 0, 1)));
        assertEquals(1, result6.size(), "Wrong number of points");
        assertEquals(List.of(p5), result6, "Ray crosses sphere at one point");

        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(p5, new Vector(0, 0, 1))),
                "Ray's line out of sphere");

        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1 ,0, 2), new Vector(0, 0, 1))),
                "Ray's line out of sphere");

        // **** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0,0, 1), new Vector(1, 0, 0))),
                "Ray's line out of sphere");

        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(p5, new Vector(0, 0, 1))),
                "Ray's line out of sphere");

        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(2 ,0, 1), new Vector(0, 0, 1))),
                "Ray's line out of sphere");

        // **** Group: Special cases
        // T22: Ray's line is outside, ray is orthogonal to ray start to sphere's center line (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(3 ,0, 0), new Vector(0, 0, 1))),
                "Ray's line out of sphere");
    }

}