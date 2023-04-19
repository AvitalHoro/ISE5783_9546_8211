package unittest.geometries;

import geometries.Polygon;
import geometries.Sphere;
import geometries.Tube;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class TubeTest {

    @Test

    /** Test method for {@link Tube#getNormal(Point)} (primitives.Point)}. */

    void testgetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: There is a simple single test here - using a quad
        Tube tb = new Tube(1, new Ray(new Point(0,0,0), new Vector(0,0,1)));
        // ensure there are no exceptions
        assertDoesNotThrow(() -> tb.getNormal(new Point(1, 0, 3)), "");
        // generate the test result
        Vector result = tb.getNormal(new Point(0, 1, 3));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Tube's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        assertTrue(Util.isZero(result.dotProduct(new Vector(0,0,1))),
                    "Tube's normal is not orthogonal to axis ray");

        // =============== Boundary Values Tests ==================
        // extreme case when 𝑷 − 𝑷𝟎 vector is orthogonal to point start axis ray
        assertDoesNotThrow(() -> tb.getNormal(new Point(1, 0, 0)), "");
        assertTrue(Util.isZero(result.dotProduct(new Vector(0,0,1))),
                "Tube's normal is not orthogonal to axis ray");
    }

    /** Test method for {@link Tube#findIntsersections(Ray)} (primitives.Ray)}. */
    void testFindIntersections() {

    }
}