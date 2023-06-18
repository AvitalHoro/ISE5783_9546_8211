package renderer;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;
import scene.Scene;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import static java.awt.Color.*;

/** Test rendering a basic image
 * @author Dan */
public class RenderTests {

    /** Produce a scene with basic 3D model and render it into a png image with a
     * grid */
    @Test
    public void basicRenderTwoColorTest() {
        Scene scene = new Scene.SceneBuilder("Test scene")//
                .setAmbientLight(new AmbientLight(new Color(255, 191, 191), //
                        new Double3(1, 1, 1))) //
                .setBackground(new Color(75, 127, 90)).build();

        scene.getGeometries().add(new Sphere(50d, new Point(0, 0, -100)),
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100)), // up
                // left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100),
                        new Point(-100, -100, -100)), // down
                // left
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))); // down
        // right
        Camera camera = new Camera(new Point(Double3.ZERO), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPDistance(100) //
                .setVPSize(500, 500) //
                .setImageWriter(new ImageWriter("base render test", 1000, 1000))
                .setRayTracer(new RayTracerBasic(scene));

        camera.renderImage();
        camera.printGrid(100, new Color(YELLOW));
        camera.writeToImage();
    }

    // For stage 6 - please disregard in stage 5
    /** Produce a scene with basic 3D model - including individual lights of the
     * bodies and render it into a png image with a grid */
    @Test
    public void basicRenderMultiColorTest() {
        Scene scene = new Scene.SceneBuilder("Test scene")//
                .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.2))).build(); //

        scene.getGeometries().add( // center
                new Sphere(50, new Point(0, 0, -100)),
                // up left
                new Triangle(new Point(-100, 0, -100), new Point(0, 100, -100), new Point(-100, 100, -100))
                        .setEmission(new Color(GREEN)),
                // down left
                new Triangle(new Point(-100, 0, -100), new Point(0, -100, -100), new Point(-100, -100, -100))
                        .setEmission(new Color(RED)),
                // down right
                new Triangle(new Point(100, 0, -100), new Point(0, -100, -100), new Point(100, -100, -100))
                        .setEmission(new Color(BLUE)));

        Camera camera = new Camera(new Point(Double3.ZERO), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
                .setVPDistance(100) //
                .setVPSize(500, 500) //
                .setImageWriter(new ImageWriter("color render test", 1000, 1000))
                .setRayTracer(new RayTracerBasic(scene));

        camera.renderImage();
        camera.printGrid(100, new Color(WHITE));
        camera.writeToImage();
    }

    /** Test for XML based scene - for bonus */
    @Test
    public void basicRenderXml() {
        try {
            String xmlFileName = "basicRenderTestTwoColors.xml";
            File inputFile = new File(xmlFileName);

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputFile);

            document.getDocumentElement().normalize();

            // Parse scene data
            Element sceneElement = (Element) document.getElementsByTagName("scene").item(0);
            String backgroundColor = sceneElement.getAttribute("background-color");

            // Parse ambient-light data
            Element ambientLightElement = (Element) document.getElementsByTagName("ambient-light").item(0);
            String ambientLightColor = ambientLightElement.getAttribute("color");

            String[] ambientLightColorCoord = ambientLightColor.split(" ");

            double rA = Double.parseDouble(ambientLightColorCoord[0]);
            double gA = Double.parseDouble(ambientLightColorCoord[1]);
            double bA = Double.parseDouble(ambientLightColorCoord[2]);

            String[] backgroundColorCoord = backgroundColor.split(" ");

            double rB = Double.parseDouble(backgroundColorCoord[0]);
            double gB = Double.parseDouble(backgroundColorCoord[1]);
            double bB = Double.parseDouble(backgroundColorCoord[2]);

            // Create scene object
            Scene scene = new Scene.SceneBuilder("XML Test scene")
                    .setBackground(new Color(rB,gB,bB))
                    .setAmbientLight(new AmbientLight(new Color(rA,gA,bA),new Double3(1, 1, 1)))
                    .build();

            // Parse geometries
            Element geometriesElement = (Element) document.getElementsByTagName("geometries").item(0);
            NodeList geometriesList = geometriesElement.getChildNodes();

            for (int i = 0; i < geometriesList.getLength(); i++) {
                Node geometryNode = geometriesList.item(i);

                if (geometryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element geometryElement = (Element) geometryNode;
                    String geometryType = geometryElement.getNodeName();

                    if (geometryType.equals("sphere")) {
                        // Parse sphere data
                        String sphereCenter = geometryElement.getAttribute("center");
                        double sphereRadius = Double.parseDouble(geometryElement.getAttribute("radius"));

                        String[] sphereCenterCoord = sphereCenter.split(" ");

                        double xS = Double.parseDouble(sphereCenterCoord[0]);
                        double yS = Double.parseDouble(sphereCenterCoord[1]);
                        double zS = Double.parseDouble(sphereCenterCoord[2]);

                        // Create sphere object and add it to the scene
                        scene.getGeometries().add(new Sphere(sphereRadius, new Point(xS,yS,zS)));
                    } else if (geometryType.equals("triangle")) {
                        // Parse triangle data
                        String p0 = geometryElement.getAttribute("p0");
                        String p1 = geometryElement.getAttribute("p1");
                        String p2 = geometryElement.getAttribute("p2");

                        String[] p0Coordinates = p0.split(" ");
                        String[] p1Coordinates = p1.split(" ");
                        String[] p2Coordinates = p2.split(" ");

// Extract the coordinates as doubles
                        double p0X = Double.parseDouble(p0Coordinates[0]);
                        double p0Y = Double.parseDouble(p0Coordinates[1]);
                        double p0Z = Double.parseDouble(p0Coordinates[2]);

                        double p1X = Double.parseDouble(p1Coordinates[0]);
                        double p1Y = Double.parseDouble(p1Coordinates[1]);
                        double p1Z = Double.parseDouble(p1Coordinates[2]);

                        double p2X = Double.parseDouble(p2Coordinates[0]);
                        double p2Y = Double.parseDouble(p2Coordinates[1]);
                        double p2Z = Double.parseDouble(p2Coordinates[2]);

                        // Create Point objects using the extracted coordinates
                        // Create triangle object and add it to the scene
                        scene.getGeometries().add(new Triangle(new Point(p0X, p0Y, p0Z), new Point(p1X, p1Y, p1Z), new Point(p2X, p2Y, p2Z)));
                    }
                }
            }

            // Continue with the rest of the method using the parsed scene object

            Camera camera = new Camera(new Point(0, 0, 0), new Vector(0, 0, -1), new Vector(0, 1, 0))
                    .setVPDistance(100)
                    .setVPSize(500, 500)
                    .setImageWriter(new ImageWriter("xml render test", 1000, 1000))
                    .setRayTracer(new RayTracerBasic(scene));

            camera.renderImage();
            camera.printGrid(100, new Color(YELLOW));
            camera.writeToImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
