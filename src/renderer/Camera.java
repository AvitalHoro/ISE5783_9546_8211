package renderer;

import primitives.*;

import java.util.MissingResourceException;

import static primitives.Util.*;

/**
 * a class that represents a camera.
 */
public class Camera {
    private Point p0;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;
    private double width;
    private double height;
    private double distance;
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int multiThreading = 1;


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
    //endregion

    //region setImageWriter
    public Camera setImageWriter(ImageWriter imageWriter) {
        this.imageWriter = imageWriter;
        return this;
    }
    //endregion

    //region setViewPlane
    public Camera setVPSize(double width, double height) {
        this.width = width;
        this.height = height;
        return this;
    }
    //endregion

    //region setVPDistance
    public Camera setVPDistance(double distance) {
        this.distance = distance;
        return this;
    }
    //endregion

    //region setRayTracer
    public Camera setRayTracer(RayTracerBase rayTracer) {
        this.rayTracer = rayTracer;
        return this;
    }
    //endregion

    //region constructor
    public Camera(Point p0, Vector vTo, Vector vUp) throws IllegalArgumentException {
        if (!isZero(vTo.dotProduct(vUp))) {
            throw new IllegalArgumentException("constructor threw - vUp and vTo are not orthogonal");
        }
        this.p0 = p0;
        this.vUp = vUp.normalize();
        this.vTo = vTo.normalize();
        this.vRight = vTo.crossProduct(vUp).normalize();
    }
    //endregion
    /**
     * Cast ray from camera in order to color a pixel
     *
     * @param nX   - resolution on X axis (number of pixels in row)
     * @param nY   - resolution on Y axis (number of pixels in column)
     * @param icol - pixel's column number (pixel index in row)
     * @param jrow - pixel's row number (pixel index in column)
     */
    private void castRay(int nX, int nY, int icol, int jrow) {
        Ray ray = constructRay(nX, nY, jrow, icol);
        Color pixelColor = rayTracer.traceRay(ray);
        imageWriter.writePixel(jrow, icol, pixelColor);
    }

    /**
     * if multithreading is needed then it is possible to change the amount of threads rendering the image
     * @param multiThreading the amount of threads
     * @return this - builder pattern
     */
    public Camera setMultiThreading(int multiThreading) {
        this.multiThreading = multiThreading;
        return this;
    }

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

    /**
     * render the image and fill the pixels with the desired color.
     * using the ray tracer to find the colors.
     * and the image writer to color the pixels.
     * @throws MissingResourceException if one of the following fields are uninitialized (unable to render the image):<ul>
     *     <li> imageWriter </li>
     *     <li> reyTracer </li>
     *     <li> width </li>
     *     <li> height </li>
     *     <li> distance </li>
     * </ul>
     * optional - use threading
     */
    public Camera renderImage() throws MissingResourceException{
        if (imageWriter == null || rayTracer == null || width == 0 || height == 0 || distance == 0) {
            throw new MissingResourceException("Camera is missing some fields", "Camera", "field");
        }

        int nY = imageWriter.getNy();
        int nX = imageWriter.getNx();

        int threadsCount = this.multiThreading;
        Pixel.initialize(nY, nX, 1);
        while (threadsCount-- > 0) {
            new Thread(() -> {          // start the treads
                for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                    imageWriter.writePixel(pixel.col, pixel.row,
                            rayTracer.traceRay(
                                    constructRay(nX, nY, pixel.col, pixel.row)));
            }).start();
        }
        Pixel.waitToFinish();
        return this;
    }

    /**
     * render the image and fill the pixels with the desired color.
     * using the ray tracer to find the colors.
     * and the image writer to color the pixels.
     * @throws MissingResourceException if one of the following fields are uninitialized (unable to render the image):<ul>
     *     <li> imageWriter </li>
     *     <li> reyTracer </li>
     *     <li> width </li>
     *     <li> height </li>
     *     <li> distance </li>
     * </ul>
     * @param rayNum the size of the ray beam
     * optional - use threading
     */
    public Camera renderImage(int rayNum) {
        if (imageWriter == null || rayTracer == null || width == 0 || height == 0 || distance == 0) {
            throw new MissingResourceException("Camera is missing some fields", "Camera", "field");
        }
        int nY = imageWriter.getNy();
        int nX = imageWriter.getNx();

        int threadsCount = this.multiThreading;
        Pixel.initialize(nY, nX, 1);
        while (threadsCount-- > 0) {
            new Thread(() -> {                  // start the threads
                for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone()) {
                    Color color = Color.BLACK;

                    RayBeam rayBeam = new RayBeam(constructRay(nX, nY, pixel.col, pixel.row),
                            this.vUp, this.vRight)
                            .setSize(this.width / nY, this.height / nX)
                            .setAmount(rayNum);

                    for (Ray r : rayBeam.constructRayBeam()) {
                        color = color.add(rayTracer.traceRay(r));
                    }
                    color = color.reduce(rayNum);   // average the color
                    imageWriter.writePixel(pixel.col, pixel.row, color);
                }
            }).start();
        }
        Pixel.waitToFinish();
        return this;
    }
//    /**
//     * render the image and fill the pixels with the desired colors
//     * using the ray tracer to find the colors
//     * and the image writer to color the pixels
//     *
//     * @throws MissingResourceException if one of the fields are uninitialized
//     */
//
//    public Camera renderImage() {
//        try {
//            if (imageWriter == null) {
//                throw new MissingResourceException("missing resource", ImageWriter.class.getName(), "");
//            }
//            if (rayTracer == null) {
//                throw new MissingResourceException("missing resource", RayTracerBase.class.getName(), "");
//            }
//            int nX = imageWriter.getNx();
//            int nY = imageWriter.getNy();
//            //rendering the image
//            for (int i = 0; i < nY; i++) {
//                for (int j = 0; j < nX; j++) {
//                    castRay(nX, nY, i, j);
//                }
//            }
////            }
//        } catch (MissingResourceException e) {
//            throw new UnsupportedOperationException("Not implemented yet" + e.getClassName());
//        }
//        return this;
//    }

    //endregion

    /**
     *  print a grid on the image without running over the original image
     * @param interval the size of the grid squares
     * @param color the color of the grid
     * @throws MissingResourceException if the imageWriter is uninitialized - unable to print a grid
     */
    public void printGrid(int interval, Color color) throws MissingResourceException{
        if (this.imageWriter == null)
            throw new MissingResourceException("Camera is missing some fields", "Camera", "imageWriter");

        // loop over j
        for (int i = 0;  i< imageWriter.getNy(); i++)
            for (int j = 0;j< imageWriter.getNx() ; j += interval)
                imageWriter.writePixel(j,i,color);  // color the grid

        // loop for j
        for (int i = 0;  i< imageWriter.getNy(); i+= interval)
            for (int j = 0;j< imageWriter.getNx() ; j++)
                imageWriter.writePixel(j,i,color);  // color the grid
    }
    //region printGrid
//
//    /**
//     * print a grid on the image without running over the original image
//     *
//     * @param interval the size of the grid squares
//     * @param color    the color of the grid
//     * @throws MissingResourceException
//     */
//    public void printGrid(int interval, Color color) throws MissingResourceException {
//        if (this.imageWriter == null) // the image writer is uninitialized
//            throw new MissingResourceException("Camera is missing some fields", "Camera", "imageWriter");
//        for (int i = 0; i < imageWriter.getNy(); i++)
//            for (int j = 0; j < imageWriter.getNx(); j++)
//                if (i % interval == 0 || j % interval == 0)  // color the grid
//                    imageWriter.writePixel(j, i, color);
//    }
    //endregion

    //region writeToImage

    /**
     * create the image file using the image writer
     */
    public void writeToImage() {
        if (this.imageWriter == null) // the image writer is uninitialized
            throw new MissingResourceException("Camera is missing some fields", "Camera", "imageWriter");
        imageWriter.writeToImage();
    }

    //region rotate around vTo
    /**
     * rotate the camera around the vTo vector -
     * <ul>
     *     <li>positive angle: spin the camera on the reverse of the clockwise direction = spin the scene to clockwise</li>
     *     <li>negative angle: spin the camera clockwise = spin the scene on the reverse of the clockwise direction</li>
     * </ul>
     * @param angle the angle of the rotation
     * @return the camera itself. builder pattern
     */
    public Camera rotateAroundVTo(double angle){
        vRight = vRight.moveClockwiseAround(vTo, angle);
        vUp = vRight.crossProduct(vTo).normalize();
        return this;
    }
    //endregion

    //region rotate around vUp
    /**
     * rotate the camera around the vUp vector -
     * <ul>
     *     <li>positive angle: rotate the camera to the left = move the scene to the right</li>
     *     <li>negative angle: rotate the camera to the right = move the scene to the left</li>
     * </ul>
     * @param angle the angle of the rotation
     * @return the camera itself. builder pattern
     */
    public Camera rotateAroundVUp(double angle){
        vTo = vTo.moveClockwiseAround(vUp, angle);
        vRight = vTo.crossProduct(vUp).normalize();
        return this;
    }
    //endregion

    /**
     * rotate the camera around the vRight vector -
     * <ul>
     *     <li>positive angle: rotate the camera upward = move the scene downward</li>
     *     <li>negative angle: rotate the camera downward = move the scene upward</li>
     * </ul>
     * @param angle the angle of the rotation
     * @return the camera itself. builder pattern
     */
    public Camera rotateAroundVRight(double angle){
        vUp = vUp.moveClockwiseAround(vRight, angle);
        vTo = vUp.crossProduct(vRight).normalize();
        return this;
    }

    /**
     * move the reference point of the camera using the amount of units to move in the direction of each reference vector
     * @param moveVUp on the direction of vUp
     * @param moveVTo on the direction of vTo
     * @param moveVRight on the direction of vRight
     * @return the camera itself. builder pattern
     */
    public Camera moveReferencePoint(double moveVUp, double moveVTo,double moveVRight){
        if(!isZero(moveVUp))     this.p0 = this.p0.add(vUp.scale(moveVUp));
        if(!isZero(moveVRight))  this.p0 = this.p0.add(vRight.scale(moveVRight));
        if(!isZero(moveVTo))     this.p0 = this.p0.add(vTo.scale(moveVTo));
        return this;
    }
    //endregion

}
