package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import static primitives.Util.*;

public class Camera {
    /**
     * a class that represents a camera.
     */
    private Point p0;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;
    private double width;
    private double height;
    private double distance;

    //region getters
    public Point getP0() {
        return p0;
    }

    public Vector getvUp() {
        return vUp;
    }

    public Vector getvTo() {
        return vTo;
    }

    public Vector getvRight() {
        return vRight;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    /**
     * @param p0
     * @param vTo
     * @param vUp
     * @throws IllegalArgumentException
     */
    public Camera(Point p0, Vector vTo, Vector vUp) throws IllegalArgumentException {
        if (!isZero(vTo.dotProduct(vUp))) {
            throw new IllegalArgumentException("constructor threw - vUp and vTo are not orthogonal");
        }
        this.p0 = p0;
        this.vUp = vUp.normalize();
        this.vTo = vTo.normalize();
        this.vRight = vTo.crossProduct(vUp).normalize();
    }

    /**
     * @param width
     * @param height
     * @return
     */
    public Camera setVPSize(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }
    //endregion

    /**
     * @param distance
     * @return
     */
    public Camera setVPDistance(double distance) {
        this.distance = distance;
        return this;
    }
    //endregion


    //region constructRay

    /**
     * constructs a ray from the camera through pixel i,j.
     *
     * @param nX number of pixels on the width of the view plane.
     * @param nY number of pixels on the height of the view plane.
     * @param j  location of the pixel in the X direction.
     * @param i  location of the pixel in the Y direction.
     * @return the constructed ray - from p0 through the wanted pixel.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pc = p0.add(vTo.scale(distance));     // center of the view plane
        double Ry = height / nY;                      // Ratio - pixel height
        double Rx = width / nX;                       // Ratio - pixel width

        double yJ = alignZero(-(i - (nY - 1) / 2d) * Ry);       // move pc Yi pixels
        double xJ = alignZero((j - (nX - 1) / 2d) * Rx);        // move pc Xj pixels

        Point PIJ = pc;
        if (!isZero(xJ)) PIJ = PIJ.add(vRight.scale(xJ));
        if (!isZero(yJ)) PIJ = PIJ.add(vUp.scale(yJ));

        return new Ray(p0, PIJ.subtract(p0));
    }
    //endregion
}

