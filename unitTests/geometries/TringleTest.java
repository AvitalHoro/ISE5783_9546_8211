package geometries;
import java.util.List;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class TringleTest {


    /** Test method for {@link geometries.Triangle#getNormal(Point)} (primitives.Point)}. */
    @Test
    public void testgetNormal()
    {
        unittest.geometries.PolygonTests p = new unittest.geometries.PolygonTests();
        p.testGetNormal();
    }

    /** Test method for {@link Triangle#findIntersections(Ray)} (primitives.Ray)}. */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(1,1,0), new Point(0,1,0), new Point(1, 0, 0));

        // ============ Equivalence Partitions Tests ==============

        // TC01: Ray starts outside the triangle and crosses it (1 point)
        Point p = new Point(0.5,0.6,0);
        List<Point> result = triangle.findIntersections(new Ray(new Point(1, 1, 1), new Vector(-0.5, -0.4, -1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p), result, "Ray crosses triangle");

        // TC02: Ray starts outside the triangle and does not cross it (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(-0.4, -0.4, 0), new Vector(-0.6, -0.6, -1))),
                "Ray does not cross triangle");

        // TC03: Ray starts outside the triangle and does not cross the plane of triangle (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, 1))),
                "Ray does not cross the plane of triangle");

        //TC04: Ray starts outside the triangle and goes opposite vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(1, 1, 1), new Vector(1, 1, -1))),
                "Ray starts outside and goes outside");


        // =============== Boundary Values Tests ==================
        //TC05: Ray starts outside the triangle and goes to the side (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(1,1,1), new Vector(0, -0.5, -1))),
                "Ray starts opposite the vertex");
        //TC06: Ray starts outside the triangle and goes to the vertex (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(1,1,1), new Vector(0, -1, -1))),
                "Ray starts opposite the vertex");
        //TC07: Ray starts outside the triangle and goes to the side's continuation (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(1,1,1), new Vector(0, 1, -1))),
                "Ray starts opposite the vertex");
        //TC08: Ray starts at triangle and goes inside (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(0.5,0.6,0), new Vector(0, 1, 0))),
                "Ray starts opposite the vertex");
        //TC09: Ray starts at triangle and goes outside (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(0.5,0.6,0), new Vector(1, 1, 1))),
                "Ray starts opposite the vertex");










    }
}
