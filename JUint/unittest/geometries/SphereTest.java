package unittest.geometries;

import geometries.Polygon;
import geometries.Sphere;
import primitives.Point;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class SphereTest {

    @org.junit.jupiter.api.Test
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
}