package unittest.geometries;

import geometries.Cylinder;
import geometries.Plane;
import geometries.Tube;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class CylinderTest {

    @org.junit.jupiter.api.Test

    /** Test method for {@link Cylinder#getNormal(Point)} (primitives.Point)}. */

    void testgetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: on the round surface
        Cylinder cy = new Cylinder(1, new Ray(new Point(0,0,0), new Vector(0,0,1)), 2);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> cy.getNormal(new Point(1, 2, 1)), "");
        // generate the test result
        Vector result = cy.getNormal(new Point(1, 2, 3));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Tube's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        assertTrue(isZero(result.dotProduct(new Vector(0,0,1))),
                "Tube's normal is not orthogonal to axis ray");

        //on the bases (1 TC for each base)

        // =============== Boundary Values Tests ==================
        // in the center of each base
        assertDoesNotThrow(() -> cy.getNormal(new Point(1, 0, 0)), "");
        assertTrue(isZero(result.dotProduct(new Vector(0,0,1))),
                "Tube's normal is not orthogonal to axis ray");
    }
}