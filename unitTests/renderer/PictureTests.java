package renderer;

import geometries.*;
import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.RayTracerBasic;
import scene.Scene;

import static java.awt.Color.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Testing Mini-project functionalities
 */


public class PictureTests {
    private Scene scene = new Scene.SceneBuilder("Test scene").build();
    private Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
        .setVPSize(150, 150).setVPDistance(1000);


    @Test
    public void pictureOFsomeShapes() {

        scene.getGeometries().add( //
                new Cylinder(30d, new Ray(new Point(0, 0, -50), new Vector(0, 0, 1)), 5d)
                        .setEmission(new Color(PINK))
                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.3)),
                new Sphere(10d, new Point(40, 40, 3))
                        .setEmission(new Color(BLUE)) //
                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)),
                new Triangle(new Point(100, 35, 3), new Point(87, 40, 10), new Point(7, 38, 10))
                        .setEmission(new Color(RED))
                        .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(100)));

//        scene.getLights().add //
//                (new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2)) //
//                        .setKl(0.0004).setKq(0.0000006));
        scene.getLights().add( //
                new SpotLight(new Color(400, 240, 0), new Point(-100, -100, 200), new Vector(1, 1, -3)) //
                        .setKl(1E-5).setKq(1.5E-7));

        scene.getLights().add
                (new DirectionalLight(new Color(400, 400, 400), new Vector(1, 0, 0)));

        scene.setAmbientLight(new AmbientLight(new Color(WHITE),new Double3(0.15)));

        camera.setImageWriter(new ImageWriter("pictureOFsomeShapes", 500, 500)) //
                .setRayTracer(new RayTracerBasic(scene)) //
                .renderImage() //
                .writeToImage();
    }
}

