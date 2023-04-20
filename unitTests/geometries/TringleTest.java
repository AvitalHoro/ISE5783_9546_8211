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
        // **** Group: Ray's line starts outside
        // TC01: Ray goes inside (1 point)
        Point p = new Point(0.5,0.5,0);
        List<Point> result = triangle.findIntersections(new Ray(new Point(1, 2, -1), new Vector(-0.5, -1.5, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p), result, "Ray starts outside and goes inside");

        // TC02: Ray goes outside(0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(1, 2, -1), new Vector(0.5, 1.5, -1))),
                "Ray starts outside and goes outside");

        // TC03: Ray is parallel to the triangle (0 points)
        assertNull(triangle.findIntersections(new Ray(new Point(1, 2, -1), new Vector(1, 0, 0))),
                " Ray is parallel to the triangle");

        // **** Group: Ray's line starts at triangle
        // TC04: Ray goes outside(0 points)
        // TC05: Ray is contained at triangle (0 points)

        // **** Group: Ray's line starts opposite the vertex
        // TC05: Ray goes inside(0 points)
        // TC07: Ray goes outside(0 points)

        // =============== Boundary Values Tests ==================
        // **** Group: Ray's line starts at the continuation of side
        // TC08: Ray goes inside(2 points)
        // TC09: Ray goes outside(0 points)
        // TC010: Ray goes to the side(0 points)

        // **** Group: Ray's line starts at a vertex
        // TC11: Ray goes inside(1 points)
        // TC12: Ray goes outside(0 points)

        // **** Group: Ray's line starts at a side
        // TC013: Ray goes inside(1 points)
        // TC14: Ray goes to the vertex(0 points)
        // TC15: Ray goes outside(0 points)









    }
}
