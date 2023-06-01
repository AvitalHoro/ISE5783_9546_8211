package renderer;

import primitives.*;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.isZero;

/**
 * create a beam of points in a relative radius or perimeter from a Ray in space
 */
public class RayBeam {
    private final Ray centerRay;
    private final Vector vUp;
    private final Vector vRight;
    private double height = 1;
    private double width = 1;
    private final double DISTANCE = 50;
    private int amount = 81;

    //region setters
    /**
     * set the size of the blackboard in a radius from the center ray of the beam according to an angle
     * @param a of the beam's span
     * @return this - builder pattern
     */
    RayBeam setSize(double a){
        double radius = DISTANCE * Math.tan(Math.toRadians(a));
        this.height = this.width = radius * 2 ;
        return this;
    }

    /**
     * set the size of the blackboard according to a height and width parameters
     * @return this - builder pattern
     */
    RayBeam setSize(double height, double width){
        this.height = height;
        this.width = width ;
        return this;
    }

    /**
     * optional to change the amount of rays in the ray beam
     * @return this - builder pattern
     */
    public RayBeam setAmount(int amount) {
        this.amount = amount;
        return this;
    }
    //endregion

    //region getters
    public Vector getvUp() {
        return vUp;
    }

    public Vector getvRight() {
        return vRight;
    }
    //endregion

    //region constructors
    public RayBeam(Ray centerRay, Vector vUp, Vector vRight) {
        if(!isZero(vUp.dotProduct(vRight)))
            throw new IllegalArgumentException("vectors are not orthogonal");
        this.vUp = vUp.normalize();
        this.vRight = vRight.normalize();
        this.centerRay = centerRay;
    }

    public RayBeam(Ray centerRay) {
        this.centerRay = centerRay;

        // generate orthogonal vectors
        Vector dir = centerRay.getDir();
        this.vUp = dir.getOrthogonal();

        this.vUp.normalize();
        this.vRight = this.vUp.crossProduct(dir).normalize();
    }
    //endregion

    //region constructRayBeam
    /**
     * generate a list with all the ray going through a given perimeter relative to the center ray
     * of the ray beam
     */
    List<Ray> constructRayBeam(){
        Point center = centerRay.getPoint(DISTANCE);
        List<Ray> list = new LinkedList<>();
        list.add(centerRay);

        BlackBoard blackBoard = new BlackBoard(this.width, this.height);

        for (BlackBoard.Point2D point: blackBoard.generatePoints(amount - 1)) {
            Point endPoint = center;
            // move the point in vRight direction
            if(!isZero(point.getX()))  endPoint = endPoint.add(vRight.scale(point.getX()));
            // move the point in vUp direction
            if(!isZero(point.getY()))  endPoint = endPoint.add(vUp.scale(point.getY()));

            list.add(new Ray(centerRay.getP0(), endPoint.subtract(centerRay.getP0()).normalize()));
        }
        return list;
    }
    //endregion
}