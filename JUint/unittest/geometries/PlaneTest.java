package unittest.geometries;

import geometries.Plane;
import geometries.Polygon;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class PlaneTest {

    @org.junit.jupiter.api.Test

    /** Test method for {@link geometries.Plane#Plane(Point p1, Point p2, Point p3)}. */

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
        void testgetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Point[] pts =
                { new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0), new Point(-1, 1, 1) };
        Plane pln = new Plane(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pln.getNormal(new Point(0, 0, 1)), "");
        // generate the test result
        Vector result = pln.getNormal(new Point(0, 0.5, 0.5));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Plane's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        assertTrue(isZero(result.dotProduct(pts[0].subtract(pts[1]))),
                    "Palne's normal is not orthogonal to the plane");
    }
}