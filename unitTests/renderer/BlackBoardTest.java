package renderer;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.alignZero;
import static primitives.Util.isZero;

class BlackBoardTest {

    @Test
    void testGeneratePoints() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: there is one simple test here
        // all the points are in the right distance from the (0,0) point
        BlackBoard b = new BlackBoard(1,1);
        for (BlackBoard.Point2D p:
                b.generatePoints(81)) {
            assertTrue(alignZero( 2 - p.x * p.x + p.y * p.y ) > 0, "rand point is not in range");
        }

    }
}