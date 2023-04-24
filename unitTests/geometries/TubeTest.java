package unittest.geometries;

import geometries.Polygon;
import geometries.Sphere;
import geometries.Tube;
import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

class TubeTest {


    /** Test method for {@link Tube#getNormal(Point)} (primitives.Point)}. */
    @Test
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

    @Test
    /** Test method for {@link Tube#findIntersections(Ray)}  (primitives.Ray)}. */
     void testFindIntersections() {
         Tube tube = new Tube(1d, new Ray(new Point(1, 0, 0), new Vector(0, 0, 1)));

         // ============ Equivalence Partitions Tests ==============

         //TC01: Ray starts outside and crosses the Tube(2 points)
         Point p1 = new Point(2, 0, 1);
         Point p2 = new Point(0, 0, 3);
         List<Point> result = tube.findIntersections(new Ray(new Point(3, 0, 0),
                 new Vector(-1, 0, 1)));
         assertEquals(2, result.size(), "Wrong number of points");
         if (result.get(0).getX() > result.get(1).getX())
             result = List.of(result.get(1), result.get(0));
         assertEquals(List.of(p1, p2), result, "Ray crosses tube at two points");

         //TC02: Ray starts inside and crosses(1 point)
         List<Point> result1 = tube.findIntersections(new Ray(new Point(0.5, 0.5, 0.5),
                 new Vector(1.5, -0.5, 0.5)));
         assertEquals(1, result1.size(), "Wrong number of points");
         assertEquals(List.of(p1), result1, "Ray crosses tube at one points");

         //TC03: Ray starts outside and goes to opposite direction(0 points)
         assertNull(tube.findIntersections(new Ray(new Point(3,0,0), new Vector(1, 1, 1))),
                 "Ray starts outside and does not cross");

         // =============== Boundary Values Tests ==================

         //TC04: Ray starts outside and parallels to height(0 points)
         assertNull(tube.findIntersections(new Ray(new Point(3, 0, 0), new Vector(0, 0, 1))),
                 "Ray parallels and does not crosses");

         //TC05: Ray goes at the height(0 points)
         assertNull(tube.findIntersections(new Ray(new Point(2, 0, 0), new Vector(0, 0, 1))),
                 "Ray goes at the height and does not cross");

         //TC06: Ray starts at tube and crosses(1 point)
         List<Point> result2 = tube.findIntersections(new Ray(p2, new Vector(1, 0, -1)));
         assertEquals(1, result2.size(), "Wrong number of points");
         assertEquals(List.of(p1), result2, "Ray starts at tube and crosses at one point");

         //TC07: Ray starts at tube and parallels(1 point)
         List<Point> result3 = tube.findIntersections(new Ray(p2, new Vector(0, 0, 1)));
         assertEquals(1, result3.size(), "Wrong number of points");
         assertEquals(List.of(p1), result3, "Ray starts at tube and crosses at one points");

         //TC08: Ray starts at tube and goes to opposite direction(0 points)
         assertNull(tube.findIntersections(new Ray(p2, new Vector(0, 0, -1))),
                 "Ray starts at tube and does not cross");

         //TC09: Ray starts inside and parallels to the height(0 points)
         assertNull(tube.findIntersections(new Ray(new Point(0.5, 0.5, 1), new Vector(0, 0, 1))),
                 "Ray parallels to height and does not cross");

         //TC10: Ray starts outside and parallels to the height(2 points)
         Point p3 = new Point(0, 0, 1);
         List<Point> result4 = tube.findIntersections(new Ray(new Point(-1, 0, 1), new Vector(1, 0, 0)));
         assertEquals(20, result4.size(), "Wrong number of points");
         if (result4.get(0).getX() > result4.get(1).getX())
             result = List.of(result4.get(1), result4.get(0));
         assertEquals(List.of(p3, p1), result, "Ray crosses tube at two points");

         //TC11: Ray starts at P0 point and parallels to the height(0 points)
         assertNull(tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 0, 1))),
                 "Ray starts at P0 and does not cross");

         //TC12: Ray starts before P0 and goes at height(0 points)
         assertNull(tube.findIntersections(new Ray(new Point(1, 0, -1), new Vector(0, 0, 1))),
                 "Ray starts before P0 and does not cross");

         //TC13: Ray starts at P0 point and crosses(1 point)
         List<Point> result5 = tube.findIntersections(new Ray(new Point(1, 0, 0), new Vector(1, 0, 1)));
         assertEquals(1, result5.size(), "Wrong number of points");
         assertEquals(List.of(p1), result5, "Ray starts at P0 and crosses tune at one points");
     }
}