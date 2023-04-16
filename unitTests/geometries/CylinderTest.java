package geometries;

import geometries.Cylinder;
import geometries.Plane;
import geometries.Tube;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class CylinderTest {

    @Test

    /** Test method for {@link Cylinder#getNormal(Point)} (primitives.Point)}. */
    //bonus
    void testgetNormal() {
        //The points to be checked
        Point[] points ={new Point(0,0,0), new Point(0,0,2), new Point(0.2, 0.2, 2), new Point(-0.2, 0.1, 0), new Point(1,0,1)};
        //The cylinder to be checked
        Cylinder cy = new Cylinder(1, new Ray(new Point(0,0,0), new Vector(0,0,1)), 2);
        Ray tmpRay = cy.getAxisRay();

        // ============ Equivalence Partitions Tests ==============
        // TC01: on the round surface
        // ensure there are no exceptions
        assertDoesNotThrow(() -> cy.getNormal(points[4]), "");
            // generate the test result
        Vector result = cy.getNormal(points[4]);
            // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Cylinder's normal is not a unit vector");
            // ensure the result is orthogonal to all the edges
         assertTrue(Util.isZero(result.dotProduct(new Vector(0,0,1))),
                    "Cylinder's normal is not orthogonal to axis ray");
         assertDoesNotThrow(() -> cy.getNormal(new Point(1, 2, 1)), "");
        // generate the test result
        result = cy.getNormal(new Point(1, 2, 3));
        // ensure |result| = 1
         assertEquals(1, result.length(), 0.00000001, "Cylinder's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
         assertTrue(Util.isZero(result.dotProduct(tmpRay.getDir())),
                "Cylinder's normal is not orthogonal to axis ray");

        //on the bases (1 TC for each base)
         assertDoesNotThrow(() -> cy.getNormal(points[2]), "");
         assertDoesNotThrow(() -> cy.getNormal(points[3]), "");
        for (int i =2; i<4 ; i++) {
            result = cy.getNormal(points[i]);
             assertEquals(1, result.length(), 0.00000001, "Cylinder's normal is not a unit vector");
            Vector finalResult = result;
             assertThrows(IllegalArgumentException.class, //
                    () -> finalResult.crossProduct((tmpRay.getDir())),
                    "Cylinder's normal isn't parallel to axis ray");
        }
        // =============== Boundary Values Tests ==================
        // in the center of each base
         //assertDoesNotThrow(() -> cy.getNormal(points[0]), "");
         assertDoesNotThrow(() -> cy.getNormal(points[1]), "");
        for (int i = 0; i<2 ; i++)
        {
            result = cy.getNormal(points[1]);
             assertEquals(1, result.length(), 0.00000001, "Cylinder's normal is not a unit vector");
            Vector finalResult = result;
             assertThrows(IllegalArgumentException.class, //
                    () -> finalResult.crossProduct((tmpRay.getDir())),
                    "Cylinder's normal isn't parallel to axis ray");
        }
    }
}