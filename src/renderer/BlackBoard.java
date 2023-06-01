package renderer;

import java.util.*;

/**
 * generate a random 2D points in a square around (0,0)
 */
public class BlackBoard {
    double xAxis;
    double yAxis;

    //region 2D points
    /**
     * inner class fpr representing 2D point
     */
    public static class Point2D {
        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        double x;
        double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    //endregion

    //region constructor
    public BlackBoard(double xAxis, double yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }
    //endregion

    //region generatePoints
    /**
     * generate random 2D points (x,y) in the black board surface
     * @param amount of points to generate
     * @return a list of the random pints generated with an offset,to normalize the points around the (0,0) point
     */
    public List<Point2D> generatePoints(int amount){
        List<Point2D> list = new LinkedList<>();

        for (int i = 0; i< amount; i++){
            list.add(new Point2D(xAxis * Math.random() - xAxis/2,
                    yAxis * Math.random() - yAxis/2));
        }
        return list;
    }
    //endregion
}

