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
         Tube tube = new Tube( 1d, new Ray(new Point(1 , 0, 0), new Vector(0,0,1)));

         // ============ Equivalence Partitions Tests ==============
         //מתחיל בחוץ וחותך בשתי נקודות
         // מתחיל בחוץ ולא חותך
         assertNull(tube.findIntersections(new Ray(new Point(2,0,3), new Vector(1, 1, 1))),
                 "ray starts outside and does not cross");

         // מתחיל החוץ וחותך בקודקוד
         Point p = new Point(1,0,1);
         List<Point> result = tube.findIntersections(new Ray(new Point(2, 0, 3), new Vector(-1, 0, -2)));
         assertEquals(1, result.size(), "Wrong number of points");
         assertEquals(List.of(p), result, "Ray crosses tube at the vertex");

         //מתחיל בחוץ להולך לכיוון השני
         //מתחיל בפנים
         Point p = new Point(1,0,1);
         List<Point> result1 = tube.findIntersections(new Ray(new Point(1, 0, 0.5), new Vector(-1, 0, -2)));
         assertEquals(1, result.size(), "Wrong number of points");
         assertEquals(List.of(p), result, "Ray crosses tube at the vertex");


         // =============== Boundary Values Tests ==================
         //משיק להולך פנימה(נקודה 1)
         //משיק והולך החוצה(0)
         //מתחיל בקודקוד והולך פנימה
         //מתחיל בקודקוד והולך החוצה
         //הקרן על הגובה
         //מתחיל בבסיס והולך פנימה(1)
         //מתחיל בבסיס והולך החוצה
         //מתחיל בבסיס ומוכל בו
         //מתחיל לפני הבסיס ומוכל בו
         //מתחיל אחרי הבסיס ואם זה היה בכיוון השני הוא היה מוכל בו
         //אורתוגונלי לבסיס
     }
}