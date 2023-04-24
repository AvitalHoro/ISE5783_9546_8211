package geometries;

import geometries.Cylinder;
import geometries.Plane;
import geometries.Tube;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class CylinderTest {


    /** Test method for {@link Cylinder#getNormal(Point)} (primitives.Point)}. bonus */
    @Test
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

    /** Test method for {@link Cylinder#findIntsersections(Ray)} (primitives.Ray)}. */
    @Test
    void testFindIntersections() {

        Cylinder cylinder = new Cylinder(1d, new Ray(new Point(1,0,0), new Vector(0,0,1)), 3);
        // ============ Equivalence Partitions Tests ==============

        // TC01: ray starts outside and crosses at two points(2 points)
        Point p1 = new Point(2, 0, 1);
        Point p2 = new Point(0, 0, 2);
        List<Point> result = cylinder.findIntersections(new Ray(new Point(6, 0, -1),
                new Vector(-2, 0, 1)));
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses cylinder at two points");

        // TC02: ray starts outside and goes to opposite direction(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(6, 0, -1), new Vector(2, 0, -1))),
                "ray starts outside and does not cross");

        // TC03: ray starts inside and crosses at one point(1 point)
        List<Point> result1 = cylinder.findIntersections(new Ray(new Point(0.5, 0.5, 1),
                new Vector(-0.5, -0.5, 1)));
        assertEquals(1, result1.size(), "Wrong number of points");
        assertEquals(List.of(p2), result1, "Ray crosses cylinder at one point");

        // TC04: ray starts outside and does not cross(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(3, 3, 2), new Vector(1, 1, 1))),
                "ray starts outside and does not cross");

        // TC05: ray crosses at cylinder ant at base(2 points)
        Point p3 = new Point(0.5, 0.5, 0);
        List<Point> result2 = cylinder.findIntersections(new Ray(new Point(1.5, 1.5, -4),
                new Vector(-0.5, -0.5, 2)));
        assertEquals(2, result2.size(), "Wrong number of points");
        if (result2.get(0).getX() > result2.get(1).getX())
            result2 = List.of(result2.get(1), result2.get(0));
        assertEquals(List.of(p3, p2), result2, "Ray crosses cylinder at two points");

        // TC06: ray starts inside and crosses at base(1 point)
        List<Point> result10 = cylinder.findIntersections(new Ray(new Point(0.75,0.75, 1), new Vector(-0.25, -0.25, -1)));
        assertEquals(1, result10.size(), "Wrong number of points");
        assertEquals(List.of(p3), result10, "Ray crosses cylinder at one points");

        // =============== Boundary Values Tests ==================

        // TC07: ray starts outside and crosses at cylinder and at base(2 points)
        Point p4 = new Point(1, 0, 0);
        List<Point> result3 = cylinder.findIntersections(new Ray(new Point(-1, 0, -2),
                new Vector(1, 0, 1)));
        assertEquals(2, result3.size(), "Wrong number of points");
        if (result3.get(0).getX() > result3.get(1).getX())
            result3 = List.of(result3.get(1), result3.get(0));
        assertEquals(List.of(p4, p2), result3, "Ray crosses cylinder at two points");

        // TC08: ray starts at center point and crosses at one point(1 point)
        List<Point> result4 = cylinder.findIntersections(new Ray(p4, new Vector(-1, 0, 2)));
        assertEquals(1, result4.size(), "Wrong number of points");
        assertEquals(List.of(p2), result4, "Ray crosses cylinder at one point");

        // TC09: ray starts at center point and goes outside(0 points)
        assertNull(cylinder.findIntersections(new Ray(p4, new Vector(1, 0, -2))),
                "ray does not cross");

        // TC10: ray starts at center point and contained at base(0 points)
        assertNull(cylinder.findIntersections(new Ray(p4, new Vector(1, 1, 0))),
                "ray starts at center point and contained at base");

        // TC11: ray starts at center point and goes to meeting point(1 point)
        Point p5 = new Point(0,0,3);
        List<Point> result5 = cylinder.findIntersections(new Ray(p4, new Vector(-1, 0, 3)));
        assertEquals(1, result5.size(), "Wrong number of points");
        assertEquals(List.of(p5), result5, "Ray crosses sphere at two points");

        // TC12: ray starts outside and crosses at center point and at meeting point(2 points)
        List<Point> result6 = cylinder.findIntersections(new Ray(new Point(2,0,-3), new Vector(-1, 0, 3)));
        assertEquals(2, result6.size(), "Wrong number of points");
        if (result6.get(0).getX() > result6.get(1).getX())
            result6 = List.of(result6.get(1), result6.get(0));
        assertEquals(List.of(p4, p5), result6, "Ray crosses cylinder at two points");

        // TC13: ray starts outside and crosses at base and at meeting point(2 points)
        List<Point> result7 = cylinder.findIntersections(new Ray(new Point(1, 1, -3),
                new Vector(-0.5, -0.5, 3)));
        assertEquals(2, result7.size(), "Wrong number of points");
        if (result7.get(0).getX() > result7.get(1).getX())
            result7 = List.of(result7.get(1), result7.get(0));
        assertEquals(List.of(p3, p5), result7, "Ray crosses cylinder at two points");

        // TC14: ray starts at continue the height of the cylinder and crosses at base and at cylinder(2 points)
        Point p9 = new Point(1.666666667, 0, 0);
        List<Point> result17 = cylinder.findIntersections(new Ray(new Point(1, 0, -2),
                new Vector(1, 0, 1)));
        assertEquals(2, result17.size(), "Wrong number of points");
        if (result17.get(0).getX() > result17.get(1).getX())
            result17 = List.of(result17.get(1), result17.get(0));
        assertEquals(List.of(p9, p1), result17, "Ray crosses cylinder at two points");

        // TC15: ray starts outside and parallels to height(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(3,3,1), new Vector(0, 0, 1))),
                "ray does not cross the cylinder");

        // TC16: ray starts outside and parallels to width(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(3,3,4), new Vector(-1, 0, 0))),
                "ray does not cross the cylinder");

        // TC17: ray parallels to width and crosses at 2 points(2 points)
        Point p6 = new Point(0, 0, 1);
        List<Point> result8 = cylinder.findIntersections(new Ray(new Point(3, 0, 1),
                new Vector(-1, 0, 0)));
        assertEquals(2, result8.size(), "Wrong number of points");
        if (result8.get(0).getX() > result8.get(1).getX())
            result8 = List.of(result8.get(1), result8.get(0));
        assertEquals(List.of(p1, p6), result8, "Ray crosses cylinder at two points");

        // TC18: ray starts at cylinder and goes to the apposite directions(0 points)
        assertNull(cylinder.findIntersections(new Ray(p1, new Vector(1, 0, 0))),
                " ray starts at cylinder and goes to the apposite directions");

        // TC19: ray starts at cylinder and parallels to width(1 point)
        List<Point> result9 = cylinder.findIntersections(new Ray(p6, new Vector(1, 0, 0)));
        assertEquals(1, result9.size(), "Wrong number of points");
        assertEquals(List.of(p1), result9, "Ray crosses cylinder at one point");

        // TC20: ray starts inside and parallels to width(1 point)
        List<Point> result11 = cylinder.findIntersections(new Ray(new Point(0.5,0, 1), new Vector(1, 0, 0)));
        assertEquals(1, result11.size(), "Wrong number of points");
        assertEquals(List.of(p1), result11, "Ray crosses cylinder at one point");

        // TC20: ray starts at cylinder and goes on the height(0 points)
        assertNull(cylinder.findIntersections(new Ray(p1, new Vector(0, 0, 1))),
                "ray goes on the height and does not cross the cylinder");

        // TC21: ray starts after the cylinder and goes on the height(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(2,0,4), new Vector(0, 0, 1))),
                "ray goes on the height and does not cross the cylinder");

        // TC22: ray starts at meeting point and goes on the height outside(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(2,0,3), new Vector(0, 0, 1))),
                "ray goes on the height and does not cross the cylinder");

        // TC23: ray starts at meeting point and goes on the height inside(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(2,0,3), new Vector(0, 0, -1))),
                "ray goes on the height and does not cross the cylinder");

        // TC24: ray starts before the cylinder and goes on the height(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(2,0,-1), new Vector(0, 0, 1))),
                "ray goes on the height and does not cross the cylinder");

        // TC25: ray starts before and contained at base(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-0.5,0.5,0), new Vector(1, 0, 0))),
                "ray contained at base and does not cross");

        // TC26: ray starts at base and contained at base(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0.5,0.5,0), new Vector(1, 0, 0))),
                "ray contained at base and does not cross");

        // TC27: ray starts after and contained at base(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(2.5,0.5,0), new Vector(1, 0, 0))),
                "ray contained at base and does not cross");

        // TC28: ray contained at the plane of the base(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(3,3,0), new Vector(1, 0, 0))),
                "ray contained at the plane and does not cross");

        // TC29: ray starts at center point and goes on the height(1 point)
        Point p7 = new Point(1,0,3);
        List<Point> result12 = cylinder.findIntersections(new Ray(p4, new Vector(0, 0, 1)));
        assertEquals(1, result12.size(), "Wrong number of points");
        assertEquals(List.of(p7), result12, "Ray goes on the height and crosses cylinder at one point");

        // TC30: ray starts before the center and goes on the height(2 points)
        List<Point> result13 = cylinder.findIntersections(new Ray(new Point(-1,0,0), new Vector(0, 0, 1)));
        assertEquals(2, result13.size(), "Wrong number of points");
        if (result13.get(0).getX() > result13.get(1).getX())
            result13 = List.of(result13.get(1), result13.get(0));
        assertEquals(List.of(p4, p7), result13, "Ray goes on the height and crosses cylinder at two points");

        // TC31: ray starts after the center and goes on the height(1 point)
        List<Point> result14 = cylinder.findIntersections(new Ray(new Point(1,0,1), new Vector(0, 0, 1)));
        assertEquals(1, result14.size(), "Wrong number of points");
        assertEquals(List.of(p7), result14, "Ray goes on the height and crosses cylinder at one point");

        // TC32: ray starts after the cylinder and goes on the height(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(1,0,4), new Vector(0, 0, -1))),
                "Ray goes on the height and does not cross");

        // TC33: ray starts at center and goes on the height to outside(0 points)
        assertNull(cylinder.findIntersections(new Ray(p4, new Vector(0, 0, -1))),
                "Ray goes on the height and does not cross");

        // TC34: ray starts at base and goes on the height(1 point)
        Point p8 = new Point(0.5,0.5,3);
        List<Point> result15 = cylinder.findIntersections(new Ray(p3, new Vector(0, 0, 1)));
        assertEquals(1, result15.size(), "Wrong number of points");
        assertEquals(List.of(p8), result15, "Ray goes on the height and crosses at one point");

        // TC35: ray starts before the base and goes on the height(2 points)
        List<Point> result16 = cylinder.findIntersections(new Ray(new Point(0.5,0.5,-1), new Vector(0, 0, 1)));
        assertEquals(2, result16.size(), "Wrong number of points");
        if (result16.get(0).getX() > result16.get(1).getX())
            result16 = List.of(result16.get(1), result16.get(0));
        assertEquals(List.of(p3, p8), result16, "Ray goes on the height and crosses at two points");

        // TC36: ray starts after the base and goes on the height(1 point)
        List<Point> result18 = cylinder.findIntersections(new Ray(new Point(0.5,0.5,1), new Vector(0, 0, 1)));
        assertEquals(1, result18.size(), "Wrong number of points");
        assertEquals(List.of(p8), result18, "Ray goes on the height and crosses at one point");

        // TC37: ray starts after the cylinder and goes on the height(0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(0.5,0.5,4), new Vector(0, 0, 1))),
                "Ray goes on the height and does not cross");

        // TC38: ray starts after the base and goes on the height outside(0 points)
        assertNull(cylinder.findIntersections(new Ray(p3, new Vector(0, 0, -1))),
                "Ray goes on the height and does not cross");

        // TC38: ray starts before the meeting point (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(-2,0,1), new Vector(1, 0, 1))),
                "Ray goes on the meeting point and does not cross");

        // TC39: ray starts at the meeting point (0 points)
        assertNull(cylinder.findIntersections(new Ray(p5, new Vector(1, 0, 1))),
                "Ray goes on the meeting point and does not cross");

        // TC40: ray starts after the meeting point (0 points)
        assertNull(cylinder.findIntersections(new Ray(new Point(2,0,5), new Vector(1, 0, 1))),
                "Ray goes on the meeting point and does not cross");

        // TC41: ray starts at the meeting point and goes to the opposite directions (0 points)
        assertNull(cylinder.findIntersections(new Ray(p5, new Vector(-1, 0, 1))),
                "Ray starts at the meeting point and does not cross");

        // TC42: ray starts at the meeting point and crosses at the second meeting points (1 point)
        Point p10 = new Point(2,0,0);
        List<Point> result19 = cylinder.findIntersections(new Ray(p5, new Vector(2, 0, -3)));
        assertEquals(1, result19.size(), "Wrong number of points");
        assertEquals(List.of(p10), result19, "Ray starts at the meeting point and crosses at one point");

        // TC43: ray starts at the meeting point and crosses at the cylinder(1 point)
        List<Point> result20 = cylinder.findIntersections(new Ray(p5, new Vector(1, 0, -1)));
        assertEquals(1, result20.size(), "Wrong number of points");
        assertEquals(List.of(p1), result20, "Ray starts at the meeting point and crosses at one point");

        // TC44: ray starts at the meeting point and crosses at the base(1 point)
        List<Point> result21 = cylinder.findIntersections(new Ray(p5, new Vector(0.5, 0.5, -3)));
        assertEquals(1, result21.size(), "Wrong number of points");
        assertEquals(List.of(p3), result21, "Ray starts at the meeting point and crosses at one point");
    }
}