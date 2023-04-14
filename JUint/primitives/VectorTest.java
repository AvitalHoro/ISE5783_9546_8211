package unittests.primitives;

import static org.junit.Assert.*;
import org.junit.Test;

import static primitives.Util.*;
import primitives.*;

class VectorTest {

    public void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals("Wrong vector add", new Vector(1, 1, 1), new Vector(2, 3, 4).add(new Vector(-1, -2, -3)));

        // =============== Boundary Values Tests ==================
        // TC11: test adding v + (-v)
        assertThrows("Add v plus -v must throw exception", IllegalArgumentException.class,
                () -> new Vector(1, 2, 3).add(new Vector(-1, -2, -3)));
    }

    public void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals("Wrong vector subtract", new Vector(1, 1, 1), new Vector(2, 3, 4).subtract(new Vector(1, 2, 3)));

        // =============== Boundary Values Tests ==================
        // TC11: test subtracting same vector
        assertThrows("Subtract v from v must throw exception", IllegalArgumentException.class,
                () -> new Vector(1, 2, 3).subtract(new Vector(1, 2, 3)));
    }
    public void testScale() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals("Wrong vector scale", new Vector(2, 4, 6), new Vector(1, 2, 3).scale(2));

        // =============== Boundary Values Tests ==================
        // TC11: test adding v + (-v)
        assertThrows("Scale by 0 must throw exception", IllegalArgumentException.class,
                () -> new Vector(1, 2, 3).scale(0d));
    }
    public void testDotProduct() {
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple dotProduct test
        Vector v2 = new Vector(-2, -4, -6);
        assertEquals("dotProduct() wrong value", -28d, v1.dotProduct(v2), 0.00001);

        // =============== Boundary Values Tests ==================
        // TC11: dotProduct for orthogonal vectors
        Vector v3 = new Vector(0, 3, -2);
        assertEquals("dotProduct() for orthogonal vectors is not zero", 0d, v1.dotProduct(v3), 0.00001);
    }
    public void testCrossProduct() {
        Vector v1 = new Vector(1, 2, 3);

        // ============ Equivalence Partitions Tests ==============
        Vector v2 = new Vector(0, 3, -2);
        Vector vr = v1.crossProduct(v2);

        // TC01: Test that length of cross-product is proper (orthogonal vectors taken
        // for simplicity)
        assertEquals("crossProduct() wrong result length", v1.length() * v2.length(), vr.length(), 0.00001);

        // TC02: Test cross-product result orthogonality to its operands
        assertTrue("crossProduct() result is not orthogonal to 1st operand", isZero(vr.dotProduct(v1)));
        assertTrue("crossProduct() result is not orthogonal to 2nd operand", isZero(vr.dotProduct(v2)));
        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from cross-productof co-lined vectors
        Vector v3 = new Vector(-2, -4, -6);
        assertThrows("crossProduct() for parallel vectors does not throw an exception", IllegalArgumentException.class,
                () -> v1.crossProduct(v3));
    }


    public void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertEquals("lengthSquared() wrong value", 14d, new Vector(1, 2, 3).lengthSquared(), 0.00001);
    }
    public void testLength() {
        // TC01: Simple test
        assertEquals("length() wrong value", 5d, new Vector(0, 3, 4).length(), 0.00001);
    }


    @Test
    public void testNormalize() {
        Vector v = new Vector(0, 3, 4);
        Vector n = v.normalize();
        // ============ Equivalence Partitions Tests ==============
        // TC01: Simple test
        assertTrue("normalize() does not change the vector itself", v == n);
        assertEquals("wrong normalized vector length", 1d, v.lengthSquared(), 0.00001);
        assertEquals("wrong normalized vector", new Vector(0, 0.6, 0.8), v);
    }

}

