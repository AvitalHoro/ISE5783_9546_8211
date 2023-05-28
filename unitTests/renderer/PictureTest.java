package renderer;


import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.*;

public class PictureTest {
    @Test
    public void PictureTest() {
        Scene scene = new Scene.SceneBuilder("Targil 7.2").setBackground(new Color(255,255,255)).build();
        Camera camera = new Camera(new Point(0, -600, 10), new Vector(0, 1, 0), new Vector(0, 0, 1));
        camera.setVPSize(150, 150).setVPDistance(100);

        Material material = new Material().setKd(0.4).setKs(1).setShininess(50).setKt(0).setKr(0.5).setKs(0.5);
        Material material1 = new Material().setKd(0.4).setKs(0.3).setShininess(50).setKt(0.7).setKr(0);
        SpotLight light = new SpotLight(new Color(255, 255, 255), new Point(0, -50, 25), new Vector(0, 2, -1));
        light.setKc(0).setKl(0.01).setKq(0.05);
        light.setNarrowBeam(5);

        DirectionalLight directionalLight1 = new DirectionalLight(new Color(100, 100, 100), new Vector(0, 0, -1));
        DirectionalLight directionalLight2 = new DirectionalLight(new Color(100, 100, 100), new Vector(1, 0, 0));
        DirectionalLight directionalLight3 = new DirectionalLight(new Color(100, 100, 100), new Vector(-1, 0, 0));
        PointLight pointLight = new PointLight(new Color(255,255,255),new Point(200,50,-100));


        //scene.lights.add(light);
        scene.getLights().add(directionalLight1);
        scene.getLights().add(directionalLight2);
        scene.getLights().add(directionalLight3);
        scene.getLights().add(pointLight);

        Sphere sphere = new Sphere(100, new Point(0, 0, 0));
        Sphere sphere3 = new Sphere(100, new Point( 200, -100, 50));
        Sphere sphere4 = new Sphere(100, new Point(-200, -100, 50));
        sphere.setMaterial(material);
        //sphere.setEmission(new Color(255, 0, 0)).setMaterial(material1);
        sphere3.setEmission(new Color(169,50,50));
        sphere4.setEmission(new Color(50,50,169));

        Sphere sphere2 = new Sphere(50, new Point(0, 0, 0));
        sphere2.setEmission(new Color(100, 69, 0)).setMaterial(material);

        Polygon sqr1 = new Polygon(new Point(-100, 150, -100),
                new Point(-100, 150, 100),
                new Point(100, 150, 100),
                new Point(100, 150, -100));
        //.setEmission(new Color(255, 215, 0))
        sqr1.setMaterial(material);

        Polygon sqrt2 = new Polygon(new Point(-150, 100, -100),
                new Point(-150, 100, 100),
                new Point(-150, -100, 100),
                new Point(-150, -100, -100));
        //.setEmission(new Color(0, 0, 0))
        sqrt2.setMaterial(material);

        Polygon sqrt3 = new Polygon(new Point(150, -100, -100),
                new Point(150, -100, 100),
                new Point(150, 100, 100),
                new Point(150, 100, -100));
        //.setEmission(new Color(0, 0, 0))
        sqrt3.setMaterial(material);

        Polygon sqrt4 = new Polygon(new Point(150, -100, 150),
                new Point(-100, -100, 150),
                new Point(-100, 100, 150),
                new Point(100, 100, 150));
        //.setEmission(new Color(0, 0, 0))
        sqrt4.setMaterial(material);

        Polygon sqrt5 = new Polygon(new Point(100, -100, -150),
                new Point(-100, -100, -150),
                new Point(-100, 100, -150),
                new Point(100, 100, -150));
        //.setEmission(new Color(0, 0, 0));
        sqrt5.setMaterial(material);

        Plane pln = new Plane(new Point(100,-100,-150),new Vector(0,0,1));
        //pln.setEmission(new Color(255,255,255));
        //.setEmission(new Color(0, 0, 0));
        pln.setMaterial(material);

        scene.getGeometries().add(sqr1, sqrt2, sqrt3, sqrt4, sqrt5,pln, sphere ,sphere2, sphere3, sphere4);

        camera.setImageWriter(new ImageWriter("Targil 7.2", 1000, 1000))
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage()
                .writeToImage();
    }
}
