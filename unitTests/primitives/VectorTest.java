package primitives;

import geometries.Polygon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**testing vector*/
class VectorTest {
    @Test
    /** Test method for {@link Vector#add(Vector)} (primitives.Vector)}. */

    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(new Vector(1, 1, 1), new Vector(2, 3, 4).add(new Vector(-1, -2, -3)), "Wrong vector add");

        // =============== Boundary Values Tests ==================
        // TC11: test adding v + (-v)
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(1, 2, 3).add(new Vector(-1, -2, -3)), "Add v plus -v must throw exception");
    }

    @Test
    /** Test method for {@link Vector#scale(double)} (primitives.Vector)}. */
    void testScale() {
                // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(new Vector(2, 4, 6), new Vector(1, 2, 3).scale(2),"Wrong vector scale");

        // =============== Boundary Values Tests ==================
        // TC11: test adding v + (-v)
        assertThrows(IllegalArgumentException.class,
                () -> new Vector(1, 2, 3).scale(0d),"Scale by 0 must throw exception");
    }

    @Test
    /** Test method for {@link Vector#dotProduct(Vector)} (primitives.Vector)}. */

    void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple dotProduct test
        Vector v2 = new Vector(-2, -4, -6);
        assertEquals(-28d, v1.dotProduct(v2), 0.00001, "dotProduct() wrong value");

        // =============== Boundary Values Tests ==================
        // TC11: dotProduct for orthogonal vectors
        Vector v3 = new Vector(0, 3, -2);
        assertEquals(0d, v1.dotProduct(v3), 0.00001, "dotProduct() for orthogonal vectors is not zero");
    }

    @Test
    /** Test method for {@link Vector#crossProduct(Vector)} (primitives.Vector)}. */

    void testCrossProduct() {
                Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        Vector v2 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v2);

        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals( v1.length() * v2.length(), vr.length(), 0.00001, "crossProduct() wrong result length");

        // TC02: Test cross-product result orthogonality to its operands
        assertTrue(Util.isZero(vr.dotProduct(v1)), "crossProduct() result is not orthogonal to 1st operand");
        assertTrue(Util.isZero(vr.dotProduct(v2)), "crossProduct() result is not orthogonal to 2nd operand");
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-productof co-lined vectors
        Vector v3 = new Vector(-2, -4, -6);
        assertThrows(IllegalArgumentException.class,
                () -> v1.crossProduct(v3), "crossProduct() for parallel vectors does not throw an exception");
    }

    @Test
    /** Test method for {@link Vector#lengthSquared()} (primitives.Vector)}. */

    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals(14d, new Vector(1, 2, 3).lengthSquared(), 0.00001, "lengthSquared() wrong value");
    }

    @Test
    /** Test method for {@link Vector#length()} (double)}. */

    void testLength() {
        // TC01: Simple test
        assertEquals(5d, new Vector(0, 3, 4).length(), 0.00001, "length() wrong value");
    }

}

