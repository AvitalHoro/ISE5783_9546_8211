package renderer;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static org.junit.jupiter.api.Assertions.*;

class RayBeamTest {

    @Test
    void testConstructors(){
        Ray r = new Ray(new Point(0,0,0), new Vector(1,5, -20));
        //Vector vUp = new Vector()
        //Vector vRight = new Vector();
        double boardSize = 1;
        RayBeam rb = new RayBeam(r).setSize(boardSize, boardSize);

        // ============ Equivalence Partitions Tests ==============
        // TC01: the vectors are orthogonal
        assertEquals(0, rb.getvRight().dotProduct(r.getDir()), "TC01: vectors generated are not orthogonal");
        assertEquals(0, rb.getvUp().dotProduct(r.getDir()), "TC01: vectors generated are not orthogonal");

        // =============== Boundary Values Tests ==================
        // TC02: the vectors are parallel and one of the coordinates are 0
        r = new Ray(new Point(0,0,0), new Vector(0,-5,20));
        rb = new RayBeam(r).setSize(boardSize, boardSize);
        assertEquals(0, rb.getvRight().dotProduct(r.getDir()), "TC02: vectors generated are not orthogonal");
        assertEquals(0, rb.getvUp().dotProduct(r.getDir()), "TC02: vectors generated are not orthogonal");

        r = new Ray(new Point(0,0,0), new Vector(1,0,1));
        rb = new RayBeam(r).setSize(boardSize, boardSize);
        assertEquals(0, rb.getvRight().dotProduct(r.getDir()), "TC02: vectors generated are not orthogonal");
        assertEquals(0, rb.getvUp().dotProduct(r.getDir()), "TC02: vectors generated are not orthogonal");

        r = new Ray(new Point(0,0,0), new Vector(1,3,0));
        rb = new RayBeam(r).setSize(boardSize, boardSize);
        assertEquals(0, rb.getvRight().dotProduct(r.getDir()), "TC02: vectors generated are not orthogonal");
        assertEquals(0, rb.getvUp().dotProduct(r.getDir()), "TC02: vectors generated are not orthogonal");

        // TC02: the vectors are parallel and two of the coordinates are 0
        r = new Ray(new Point(0,0,0), new Vector(0,0,20));
        rb = new RayBeam(r).setSize(boardSize, boardSize);
        assertEquals(0, rb.getvRight().dotProduct(r.getDir()), "TC03: vectors generated are not orthogonal");
        assertEquals(0, rb.getvUp().dotProduct(r.getDir()), "TC03: vectors generated are not orthogonal");

        r = new Ray(new Point(0,0,0), new Vector(1,0,0));
        rb = new RayBeam(r).setSize(boardSize, boardSize);
        assertEquals(0, rb.getvRight().dotProduct(r.getDir()), "TC03: vectors generated are not orthogonal");
        assertEquals(0, rb.getvUp().dotProduct(r.getDir()), "TC03: vectors generated are not orthogonal");

        r = new Ray(new Point(0,0,0), new Vector(0,3,0));
        rb = new RayBeam(r).setSize(boardSize, boardSize);
        assertEquals(0, rb.getvRight().dotProduct(r.getDir()), "TC03: vectors generated are not orthogonal");
        assertEquals(0, rb.getvUp().dotProduct(r.getDir()), "TC03: vectors generated are not orthogonal");
    }

    @Test
    void testConstructRayBeam() {

        // ============ Equivalence Partitions Tests ==============
        // TC01: the rays are on the same side of the surface
        Ray r = new Ray(new Point(0,0,0), new Vector(1,5, -20));
        double boardSize = 1;
        RayBeam rb = new RayBeam(r).setSize(boardSize, boardSize);

        for (Ray ray:
                rb.constructRayBeam()) {
            assertTrue(ray.getDir().dotProduct(r.getDir()) > 0, "the ray is not on the same surface");
        }
    }
}
