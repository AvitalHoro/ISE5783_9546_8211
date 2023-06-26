package renderer;

import geometries.*;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

public class FinalImage {

    Material material = new Material().setKd(0.4).setKs(0.5).setShininess(50).setKt(0).setKr(0.5).setKs(0.5);
    Point A = new Point(0,0,0);
    Point B = new Point(0,0,4);
    Point C = new Point(0,4,0);
    Point D = new Point(0,0,2);
    Point E = new Point(0,0,1.3);
    Point F = new Point(0.13, -0.6, 2.34);
    Point G = new Point(-0.02, 0.63, 2.32);
    Point H = new Point(0, 0, 2.7);
    Point I = new Point(0, 0, 2.8);

    Point J = new Point(0.7,0.7,2.38);
    Point K = new Point(-0.7,-0.7,2.38);
    Point L = new Point(0.7,-0.7,2.38);
    Point M = new Point(-0.22, -0.65, 1.87);
    Point N = new Point(0,0,1);
    Point O = new Point(-0.67723,1.1,0.5);
    Point P = new Point(-0.68, 0.92, 0.54);
    Point Q = new Point(1.7,1.3,0.40443);

    Point R = new Point(0, 0.52, 3.97);
    Point S = new Point(0, 1.35, 3.77);
    Point T = new Point(-0.45, -0.21, 2.7);

    Point U = new Point(0.7,-0.9,1.9);
    Point V = new Point(-0.68862,-2.29595,0);
    Point W = new Point(-0.46, -1.2, 0.49);

    Point Z = new Point(-0.45, -1.37, 0.5);

    Point A1 = new Point(5.42198,2.5047,0);
    Point B1 = new Point(0, -2.15, 3.37);
    Point C1 = new Point(0, -1.22, 3.81);
    Point D1 = new Point(5.3, 2.78, 0);
    Point E1 = new Point(10.82895,5.82624,0);
    Point F1 = new Point(16,8,0);
    Point G1 = new Point(5.55, 2.23, 0.01);
    Point H1 = new Point(12.14124,4.81997,0);
    Point I1 = new Point(16.05414,6.82439,0);

    Circle c = new Circle(A,4, new Vector(1,0,0));
     Sphere a = new Sphere(0.7, D);
    Cylinder b = new Cylinder(0.5, new Ray(F,G.subtract(F)),F.distance(G));

    Cylinder d = new Cylinder(0.5, new Ray(H, I.subtract(H)),H.distance(I));

   Circle e = new Circle(new Point(0,0,2.8), 0.7, new Vector(0,0,1));

    Cylinder f = new Cylinder(0.05, new Ray(D, U.subtract(D)),U.distance(D));
    Cylinder g = new Cylinder(0.1, new Ray(E, N.subtract(E)),N.distance(E));
    Cylinder h = new Cylinder(0.1, new Ray(N, O.subtract(N)),O.distance(N));
    Cylinder i = new Cylinder(0.1, new Ray(O, Q.subtract(O)),O.distance(Q));

    Sphere j = new Sphere(O.distance(P),O);

    Cylinder k = new Cylinder(0.01, new Ray(R, Q.subtract(R)),R.distance(Q));
    Cylinder l = new Cylinder(0.01, new Ray(P, S.subtract(P)),P.distance(S));
    Cylinder m = new Cylinder(0.01, new Ray(T, B.subtract(T)),T.distance(B));
    Cylinder n = new Cylinder(0.01, new Ray(N, V.subtract(N)),N.distance(V));
    Sphere o = new Sphere(W.distance(Z),W);


    Cylinder p = new Cylinder(0.3, new Ray(N, A1.subtract(N)),N.distance(A1));
    Cylinder q = new Cylinder(0.01, new Ray(V, B1.subtract(V)),V.distance(B1));
    Cylinder r = new Cylinder(0.01, new Ray(Z, C1.subtract(Z)),Z.distance(C1));
    Cylinder s = new Cylinder(0.15, new Ray(E1, F1.subtract(E1)),E1.distance(F1));

    Cylinder t = new Cylinder(0.15, new Ray(G1, H1.subtract(G1)),G1.distance(H1));
    Cylinder u = new Cylinder(0.15, new Ray(H1, I1.subtract(H1)),H1.distance(I1));

    @Test
    public void finalImage() {
        Scene scene = new Scene.SceneBuilder("final picture").setBackground(new Color(0, 0, 0)).build();
        Camera camera = new Camera(new Point(50, -60, 6), new Vector(0, 1, 0), new Vector(0, 0, 1));
        camera.setVPSize(150, 150).setVPDistance(100);

        Material material = new Material().setKd(0.4).setKs(0.5).setShininess(50).setKt(0).setKr(0.5).setKs(0.5);
        SpotLight light = new SpotLight(new Color(255, 255, 255), new Point(0, -50, 25), new Vector(0, 2, -1));
        light.setKc(0).setKl(0.01).setKq(0.05);
        light.setNarrowBeam(5);

        DirectionalLight directionalLight1 = new DirectionalLight(new Color(100, 100, 100), new Vector(0, 0, -1));
        DirectionalLight directionalLight2 = new DirectionalLight(new Color(100, 100, 100), new Vector(1, 0, 0));
        DirectionalLight directionalLight3 = new DirectionalLight(new Color(100, 100, 100), new Vector(-1, 0, 0));
        PointLight pointLight = new PointLight(new Color(255, 255, 255), new Point(200, 50, -100));


        //scene.lights.add(light);
        scene.getLights().add(directionalLight1);
        scene.getLights().add(directionalLight2);
        scene.getLights().add(directionalLight3);
        scene.getLights().add(pointLight);

//        Sphere sphere = new Sphere(100, new Point(0, 0, 0));
//        //red sphere
//        Sphere sphere3 = new Sphere(100, new Point(200, -100, 50));
//        Sphere sphere4 = new Sphere(100, new Point(-200, -100, 50));
//        sphere.setMaterial(material);
//        //sphere.setEmission(new Color(255, 0, 0)).setMaterial(material1);
//        sphere3.setEmission(new Color(169, 50, 50));
//        sphere3.setMaterial(material);
//        //blue sphere
//        sphere4.setEmission(new Color(50, 50, 169));
//        sphere4.setMaterial(material);
//
//        Sphere sphere2 = new Sphere(50, new Point(0, 0, 0));
//        sphere2.setEmission(new Color(100, 69, 0)).setMaterial(material);
//
//        Polygon sqr1 = new Polygon(new Point(-100, 150, -100),
//                new Point(-100, 150, 100),
//                new Point(100, 150, 100),
//                new Point(100, 150, -100));
//        //.setEmission(new Color(255, 215, 0))
//        sqr1.setMaterial(material);
//
//        Polygon sqrt2 = new Polygon(new Point(-150, 100, -100),
//                new Point(-150, 100, 100),
//                new Point(-150, -100, 100),
//                new Point(-150, -100, -100));
//        //.setEmission(new Color(0, 0, 0))
//        sqrt2.setMaterial(material);
//
//        Polygon sqrt3 = new Polygon(new Point(150, -100, -100),
//                new Point(150, -100, 100),
//                new Point(150, 100, 100),
//                new Point(150, 100, -100));
//        //.setEmission(new Color(0, 0, 0))
//        sqrt3.setMaterial(material);
//
//        Polygon sqrt4 = new Polygon(new Point(150, -100, 150),
//                new Point(-100, -100, 150),
//                new Point(-100, 100, 150),
//                new Point(100, 100, 150));
//        //.setEmission(new Color(0, 0, 0))
//        sqrt4.setMaterial(material);
//
//        Polygon sqrt5 = new Polygon(new Point(100, -100, -150),
//                new Point(-100, -100, -150),
//                new Point(-100, 100, -150),
//                new Point(100, 100, -150));
//        //.setEmission(new Color(0, 0, 0));
//        sqrt5.setMaterial(material);
//
//        Plane pln = new Plane(new Point(100, -100, -150), new Vector(0, 0, 1));
//        //pln.setEmission(new Color(255,255,255));
//        //.setEmission(new Color(0, 0, 0));
//        pln.setMaterial(material);

        scene.getGeometries().add(a, b,c, d,e, h, g, f, i, j, k, l, m, n, o, p, q, r, s, t, u);

        camera.setImageWriter(new ImageWriter("image1", 500, 500))
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage();
        camera.writeToImage();

//        camera.setImageWriter(new ImageWriter("image2", 500, 500))
//                .setantiAliasing(81)
//                .setMultiThreading(3)
//                .setRayTracer(new RayTracerBasic(scene))
//                .renderImage();
//        camera.writeToImage();

        camera.setImageWriter(new ImageWriter("image3", 500, 500))
                .setantiAliasing(200)
                .setadaptive(true)
                .setMultiThreading(3)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage();
        camera.writeToImage();


    }
}
