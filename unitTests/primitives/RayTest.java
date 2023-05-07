package primitives;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    void testFindClosestPoint() {
        Ray ray = new Ray(new Point(1,2,3), new Vector(2,5,1));
        Point a = new Point(3,7,4);
        Point b = new Point(5,12,5);
        Point c = new Point(15,37,10);


        // =============== Boundary Values Tests ==================

        // TC01 list of points is empty
        assertNull(ray.findClosestPoint(new LinkedList<>()), "list of points is empty- didn't return null");

        // TC02 the closest point is the first point in the list
        List<Point> points = new LinkedList<>();
        points.add(a);
        points.add(b);
        points.add(c);
        assertEquals(a, ray.findClosestPoint(points),
                "the closest point is the first point in the list- didn't return the right point");

        // TC03 the closest point is at the end of the list
        points = new LinkedList<>();
        points.add(b);
        points.add(c);
        points.add(a);
        assertEquals(a, ray.findClosestPoint(points),
                "the closest point is at the end of the list- didn't return the right point");

        // ============ Equivalence Partitions Tests ==============

        // TC04 the closest point is in the middle of the list
        points = new LinkedList<>();
        points.add(b);
        points.add(a);
        points.add(c);
        assertEquals(a, ray.findClosestPoint(points),
                "the closest point is at the end of the list- didn't return the right point");
    }
}