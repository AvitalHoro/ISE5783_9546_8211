package renderer;

import primitives.*;

import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import static primitives.Util.*;

/**
 * this class represent camera by location <br/>
 * and directions toward, right and up to the scene that lives in a virtual view plane. <br/>
 * The view plane is represent by height and wight
 * @author ahuva and avital
 */
public class Camera {
    private Point p0;
    private Vector vRight;
    private Vector vUp;
    private Vector vTo;
    private double distance;
    private double width;
    private double height;
    private Point centerPoint;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int antiAliasing= 1;
    private boolean adaptive = false;
    private int threadsCount = 1;


    /**
     * constructor for camera
     *
     * @param p0  the location of the camera
     * @param vTo the direction to the view plane
     * @param vUp the direction up
     * @throws IllegalArgumentException if vTo and vUp is not orthogonal
     */
    public Camera(Point p0, Vector vTo, Vector vUp) {
        if (!isZero(vUp.dotProduct(vTo))) {
            throw new IllegalArgumentException("The vectors 'up' and and 'to' is not orthogonal");
        }

        this.p0 = p0;
        this.vUp = vUp.normalize();
        this.vTo = vTo.normalize();
        vRight = this.vTo.crossProduct(this.vUp).normalize();
    }

    //region Getters/Setters
    /**
     * get of p0
     * @return point
     */
    public Point getP0() {
        return p0;
    }

    /**
     * get of vRight
     * @return vector
     */
    public Vector getvRight() {
        return vRight;
    }

    /**
     * get of vUp
     * @return vector
     */
    public Vector getvUp() {
        return vUp;
    }

    /**
     * get of vTo
     * @return vector
     */
    public Vector getvTo() {
        return vTo;
    }

    /**
     * get of distance
     * @return double
     */
    public double getDistance() {
        return distance;
    }

    /**
     * get of width
     * @return double
     */
    public double getWidth() {
        return width;
    }

    /**
     * get of height
     * @return double
     */
    public double getHeight() {
        return height;
    }

    /**
     * get of centerPoint
     * @return point
     */
    public Point get_centerVPPoint() {
        return centerPoint;
    }

    /**
     * set the view plane size
     * @param width  the width of the view plane
     * @param height the height of the view plane
     * @return this camera (like builder pattern)
     * @throws IllegalArgumentException if width or height equal to 0
     */
    public Camera setVPSize(double width, double height) {
        if (isZero(width) || isZero(height)) {
            throw new IllegalArgumentException("width or height cannot be zero");
        }

        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * set the distance from the camera to the view plane
     * @param distance the distance
     * @return this camera (like builder pattern)
     * @throws IllegalArgumentException if distance = 0
     */
    public Camera setVPDistance(double distance) {
        if (isZero(distance)) {
            throw new IllegalArgumentException("distance cannot be zero");
        }

        this.distance = distance;
        // every time that we chang the distance from the view plane
        // we will calculate the center point of the view plane aging
        centerPoint = p0.add(vTo.scale(this.distance));
        return this;
    }
    /**
     * set the rayTracer ray from the camera to the view plane
     * @param rayTracer the rayTracer
     * @return this camera (like builder pattern)
     */
    public Camera setRayTracer(RayTracerBase rayTracer) {
        this.rayTracer = rayTracer;
        return  this;
    }

    /**
     * set the imageWriter for the Camera
     * @param imageWriter
     * @return the Camera object
     */
    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }
    /**
     * set the anti Aliasing
     * @return the Camera object
     */
    public Camera setantiAliasing(int antiAliasing) {
        this.antiAliasing = antiAliasing;
        return this;
    }
    /**
     * set the adaptive
     * @param  adaptive
     * @return the Camera object
     */
    public Camera setadaptive(boolean adaptive) {
        this.adaptive = adaptive;
        return this;
    }
    /**
     * set the threadsCount
     * @param threadsCount
     * @return the Camera object
     */
    public Camera setMultiThreading(int threadsCount) {
        this.threadsCount = threadsCount;
        return this;

    }
    /**
     * set senter the camera
     * @param x
     * @param y
     * @param z
     */
    public void setP0(double x,double y, double z) {
        this.p0=new Point(x,y,z);
    }
//endregion

    /**
     * Checks that all fields are full and creates an image
     * @return the Camera object
     */
    public Camera renderImage() {
        //If one of the fields of the camera is wrong
        if (p0 == null || vRight == null
                || vUp == null || vTo == null || distance == 0
                || width == 0 || height == 0 || centerPoint == null
                || imageWriter == null || rayTracer == null) {
            throw new MissingResourceException("Missing camera data", Camera.class.getName(), null);
        }
        //initialize of the pixel
        Pixel.initialize(imageWriter.getNy(), imageWriter.getNx(), 1);

        //if there is no antialiasing
        if (antiAliasing == 1) {
            //over all the threads
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    //over all the pixels ant print its
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                        imageWriter.writePixel(pixel.col, pixel.row, rayTracer.TraceRays(constructRays(imageWriter.getNx(),
                                imageWriter.getNy(), pixel.col, pixel.row)));
                }).start();
            }
            //wait to all the pixel finish
            Pixel.waitToFinish();
        }
        //if there is antialiasing
        else {
            //over all the threads
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    //over all the pixels ant print its
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                        imageWriter.writePixel(pixel.col, pixel.row, SuperSampling(imageWriter.getNx(),
                                imageWriter.getNy(), pixel.col, pixel.row, antiAliasing, adaptive));
                }).start();
            }
            Pixel.waitToFinish();
        }
        return this;
    }

    /**
     Calculates the color of the pixel using a beam of rays in an adaptive or regular method
     * @param nX amount of pixels by length
     * @param nY amount of pixels by width
     * @param j The position of the pixel relative to the y-axis
     * @param i The position of the pixel relative to the x-axis
     * @param numOfRays The amount of rays sent
     * @return Pixel color
     */
    private Color SuperSampling(int nX, int nY, int j, int i, int numOfRays, boolean adaptiveAlising)  {
        Vector Vright = vRight;
        Vector Vup = vUp;
        Point cameraLoc = this.getP0();
        int numOfRaysInRowCol = (int)Math.floor(Math.sqrt(numOfRays));
        if(numOfRaysInRowCol == 1)  return rayTracer.traceRay(constructRayThroughPixel(nX, nY, j, i));

        Point pIJ = getCenterOfPixel(nX, nY, j, i);

        //the size of sub pixel
        double rY = alignZero(height / nY);
        double rX = alignZero(width / nX);

        double PRy = rY/numOfRaysInRowCol;
        double PRx = rX/numOfRaysInRowCol;
        //if antialiasing is adaptive
        if (adaptiveAlising)
            return rayTracer.AdaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy,cameraLoc,Vright, Vup,null);
        //if antialiasing is not adaptive
        else
            return rayTracer.RegularSuperSampling(pIJ, rX, rY, PRx, PRy,cameraLoc,Vright, Vup,null);
    }

    /**
     *Grid printing of the view plane
     * @param interval The space between pixels
     * @param color color of grid
     */
    public void printGrid(int interval, Color color) {
        if (this.imageWriter == null)
            throw new MissingResourceException("imageWriter is null", ImageWriter.class.getName(), null);
        imageWriter.printGrid(interval,color);
    }

    /**
     * construct ray through the center of pixel in the view plane
     * nX and nY create the resolution
     * @param nX number of pixels in the width of the view plane
     * @param nY number of pixels in the height of the view plane
     * @param j  index row in the view plane
     * @param i  index column in the view plane
     * @return ray that goes through the pixel (j, i)  Ray(p0, Vi,j)
     */
    public Ray constructRayThroughPixel(int nX, int nY, int j, int i) {
        // center point of the pixel
        Point pIJ = getCenterOfPixel(nX, nY, j, i);

        //Vi,j = Pi,j - P0, the direction of the ray to the pixel(j, i)
        Vector vIJ = pIJ.subtract(p0);
        return new Ray(p0, vIJ);
    }

    /**
     * get the center point of the pixel in the view plane
     * @param nX number of pixels in the width of the view plane
     * @param nY number of pixels in the height of the view plane
     * @param j  index row in the view plane
     * @param i  index column in the view plane
     * @return the center point of the pixel
     */
    private Point getCenterOfPixel(int nX, int nY, int j, int i) {
        // calculate the ratio of the pixel by the height and by the width of the view plane
        // the ratio Ry = h/Ny, the height of the pixel
        double rY = alignZero(height / nY);
        // the ratio Rx = w/Nx, the width of the pixel
        double rX = alignZero(width / nX);

        // Xj = (j - (Nx -1)/2) * Rx
        double xJ = alignZero((j - ((nX - 1d) / 2d)) * rX);
        // Yi = -(i - (Ny - 1)/2) * Ry
        double yI = alignZero(-(i - ((nY - 1d) / 2d)) * rY);

        Point pIJ = centerPoint;

        if (!isZero(xJ)) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(vUp.scale(yI));
        }
        return pIJ;
    }

    /**
     * function that returns the list of rays from the camera to the point
     *
     * @param nX the x resolution
     * @param nY the y resolution
     * @param i  the x coordinate
     * @param j  the y coordinate
     * @return the ray
     */

    public List<Ray> constructRays(int nX, int nY, int j, int i) {
        //empty list to save the rays
        List<Ray> rays = new LinkedList<>();
        //calculate the center of pixel
        Point centralPixel = getCenterOfPixel(nX, nY, j, i);
        //calculate the size of sub-pixel
        double rY = height / nY / antiAliasing;
        double rX = width / nX / antiAliasing;
        double x, y;

        //Create a ray for each subpixel
        for (int rowNumber = 0; rowNumber < antiAliasing; rowNumber++) {
            for (int colNumber = 0; colNumber < antiAliasing; colNumber++) {
                y = -(rowNumber - (antiAliasing - 1d) / 2) * rY;
                x = (colNumber - (antiAliasing - 1d) / 2) * rX;
                Point pIJ = centralPixel;
                if (y != 0) pIJ = pIJ.add(vUp.scale(y));
                if (x != 0) pIJ = pIJ.add(vRight.scale(x));
                rays.add(new Ray(p0, pIJ.subtract(p0)));
            }
        }
        //return the list of rays
        return rays;
    }

    /**
     * Invites the coloring function
     */
    public void writeToImage() {
        imageWriter.writeToImage();
    }

    /**
     * Rotates the camera around the axes with the given angles
     *
     * @param x angles to rotate around the x axis
     * @param y angles to rotate around the y axis
     * @param z angles to rotate around the z axis
     * @return the current camera
     */
    public Camera rotate(double x, double y, double z) {
        //vector vTo after the rotation
        vTo = vTo.rotateX(x).rotateY(y).rotateZ(z);
        //vector vUp after the rotation
        vUp = vUp.rotateX(x).rotateY(y).rotateZ(z);
        //vector vRight after the rotation
        vRight = vTo.crossProduct(vUp);

        //return the camera after the rotation
        return this;
    }
}



