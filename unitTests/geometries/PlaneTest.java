package geometries;
import geometries.Plane;
import geometries.Polygon;
import org.junit.jupiter.api.Test;
import static primitives.Util.*;
import java.util.List;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class PlaneTest {


    /** Test method for {@link geometries.Plane#Plane(Point p1, Point p2, Point p3)}. */
    @Test
    public void testConstructor() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Correct concave quadrangular with vertices in correct order
        try {
            new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0));
        } catch (IllegalArgumentException e) {
            fail("Failed constructing a correct polygon");
        }

        // =============== Boundary Values Tests ==================

        // TC02: First and second points are connected
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 1), new Point(0, 1, 0)), //
                "Constructed a plane with first and second points are connected");

        // TC03: the same line
        assertThrows(IllegalArgumentException.class, //
                () -> new Plane(new Point(0, 0, 1), new Point(1, 0, 0), new Point(5, 0, 0)), //
                "Constructed a plane with points are on the same line");
    }


    /** Test method for {@link Plane#getNormal(Point)} (primitives.Point)}. */
    @Test
    void testgetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1)};
        Plane pln = new Plane(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pln.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pln.getNormal(new Point(0, 0.5, 0.5));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Plane's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        assertTrue(Util.isZero(result.dotProduct(pts[0].subtract(pts[1]))),
                "Palne's normal is not orthogonal to the plane");
    }

    /** Test method for {@link Plane#findIntersections(Ray)} (primitives.Ray)}. */
    @Test
    public void testFindIntersections() {

        // ============ Equivalence Partitions Tests ==============

        Plane plane = new Plane(new Point(1, 0, 0), new Vector(0, 1, 0));
        // TC01:  ray's head finds on the plane(0 points)
        assertNull(plane.findIntersections(new Ray(new Point(1, 1, 0), new Vector(1, 1, 1))), "ray's head is find on the plane");

        // TC02:  ray's head finds on the p0(0 points)
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 1, 1))), "ray's head is find on the p0");

        // TC03:  ray's line is contained in plane(0 points)
        assertNull(plane.findIntersections(new Ray(new Point(1, 1, 0), new Vector(2, 3, 0))), "ray's line is contained in plane");

        // TC04:  ray's line finds in the opposite direction from the plane(0 points)
        assertNull(plane.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 2, 3))), "ray's line is find in the opposite direction from the plane");

        // TC05:  ray's line parallels to the plane(0 points)
        assertNull(plane.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 0, 0))), "ray's line parallels to the plane");

        // TC06:  ray crosses plane(1 point)
        Point p = new Point(0.33333333333333337, 0.0, -0.33333333333333326);
        List<Point> result = plane.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-0.5, -0.75, -1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p), result, "ray crosses plane");

        // =============== Boundary Values Tests ==================
        // there are no boundary tests
    }
}