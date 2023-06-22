//package renderer;
//
//import geometries.Sphere;
//import geometries.Triangle;
//import lighting.AmbientLight;
//import lighting.DirectionalLight;
//import lighting.PointLight;
//import lighting.SpotLight;
//import org.junit.jupiter.api.Test;
//import primitives.*;
//import geometries.*;
//import scene.Scene;
//
//import static java.awt.Color.*;
//
//public class PictureTest {
//    Point A=new Point(-51.82, -19.69, -550);
//    Point B=new Point(-48.42, 14.15, -550);
//    Point C=new Point(-51.81, -0.78, -530.3);
//    Point D=new Point(-61.73, 21.56, -672.88);
//    Point E=new Point(-65.3, 34.47, -550);
//    Point F=new Point(-79, 34.07, -529.92);
//    Point G=new Point(-97.33, 38.05, -550);
//    Point H=new Point(-117.87, 22.8, -550);
//    Point I=new Point(-104.97, 27.82, -570.67);
//    Point J=new Point(-117.98, 7.5, -530.28);
//    Point K=new Point(-124.19, -8.01, -550);
//    Point L=new Point(-106.35, -33.82, -550);
//    Point M=new Point(-110.51, -22.87, -570.65);
//    Point N=new Point(-91.91, -33.61, -529.44);
//    Point O=new Point(-76.83, -38.77, -550);
//    Point P=new Point(-64.72, -24.47, -573.02);
//    Point Q=new Point(-104.02, -12.86, -517.24);
//    Point R=new Point(-93.94, 12.57, -513.09);
//    Point S=new Point(-75.55, -13.08, -486.7);
//    Point T=new Point(-70.82, 10.11, -514.68);
//    Point U=new Point(-70.96, -1.38, -586.65);
//    Point V=new Point(-85, -20.62, -584.27);
//    Point W=new Point(-85, 19.69, -584.82);
//    Point Z=new Point(-99.93, 0.49, -587.1);
//
//    //private Scene scene = new Scene("Test scene11");
//    @Test
//    public void specialPicture() {
//        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
//                .setVPSize(200, 200).setVPDistance(1000);
//        Scene scene = new Scene.SceneBuilder("Test scene11")
//                .setBackground(new Color(124,242,248))
//                .setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.1)))
//                .build();
//
//        scene.getGeometries().add( //
//                new Elepsoaide(new Point(50,40,0),10,17,50).setEmission(new Color(PINK))
//                        .setMaterial(new Material().setKr(1d)),
//                new Elepsoaide(new Point(30,40,0),10,17,50).setEmission(new Color(RED))
//                        .setMaterial(new Material().setKr(1d)),
//                new Elepsoaide(new Point(50,55,20),10,17,50).setEmission(new Color(BLACK))
//                        .setMaterial(new Material().setKr(1d)),
//                new Elepsoaide(new Point(40,35,10),10,17,50).setEmission(new Color(BLUE))
//                        .setMaterial(new Material().setKr(1d)),
//                new Elepsoaide(new Point(60,30,0),10,17,50).setEmission(new Color(blue))
//                        .setMaterial(new Material().setKr(1d)));
//        new Triangle(new Point(1500, -1500, 5000), new Point(-1500, 1500, 5000),
//                new Point(-1500, -1500, 5000))
//                .setEmission(new Color(black)) //
//                .setMaterial(new Material().setKr(0.5));
//        new Triangle(new Point(-10, -60, 302), new Point(0, 0, 302), new Point(10, -60, 302))
//                .setEmission(new Color(black))//
//                .setMaterial(new Material().setKd(0.5).setKs(0.5).setShininess(60).setKt(0.6));
//
////                new Triangle(new Point(-50, -50, 0), new Point(-50, 0, 0), new Point(-100, -100, 50))
////                        .setEmission(new Color(20,20,20))//
////                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(60).setkT(0.6)),//
////                new Sphere(new Point(0, -20, 100), 40d).setEmission(new Color(BLUE)) //
////                        .setMaterial(new Material().setkD(0.4).setkS(0.3).setnShininess(100).setkT(0.3)),
////                new Sphere(new Point(0, -20, 100), 15d).setEmission(new Color(RED)) //
////                        .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)));
//        scene.getLights().add(new SpotLight(new Color(1020, 400, 400), new Point(50, 40, 0), new Vector(-1, -1, -4)) //
//                .setKl(0.00001).setKq(0.000005));
//        ImageWriter imageWriter = new ImageWriter("MiniProjectTest", 500, 500);
//        camera.setImageWriter(imageWriter) //
//                .setRayTracer(new RayTracerBasic(scene)) //
//                .renderImage() //
//                .writeToImage();
//
//    }
//    @Test
//    public void specialPicture1() {
//        Camera camera = new Camera(new Point(0, 0, 1000), new Vector(0, 0, -1), new Vector(0, 1, 0)) //
//                .setVPSize(500, 500).setVPDistance(1000);
//        //scene.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));
//        Scene scene = new Scene.SceneBuilder("MiniProjectTest1").setBackground(new Color(black)).build();
//        scene.getGeometries().add(
////                new Sphere(new Point(0, 0, 0), 40).setEmission(new Color(128,128,128))
////                       .setMaterial(new Material().setkD(0.5).setkS(0.5).setnShininess(100)),
//                new Triangle(A, B, C)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(B, E, D)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(E, F, G)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(G, H, I)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(H, J, K)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(K, L, M)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(L, N, O)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(O, A, P)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(J, Q, R)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(N, Q, S)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(C, S, T)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(F, T, R)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(P, U, V)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(D, U, W)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(M, V, Z)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.5)),
//                new Triangle(I, Z, W)
//                        .setEmission(new Color(red))//
//                        .setMaterial(new Material().setKd(0.4).setKs(0.3).setShininess(100).setKt(0.5)),
//                new Plane(new Point(0, -39, 0), new Point(1, -39, 0), new Point(0, -46, 100))
//                        .setEmission(new Color(black))
//                        .setMaterial(new Material().setKd(0.5)),
//                new Polygon(new Point(-25, 60, 330),new Point(25, 60, 330),new Point(50, 0, 330),
//                        new Point(25, -60, 330),new Point(-25, -60, 330),new Point(-50, 0, 330))
//                        .setEmission(new Color(BLUE))
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(1.0)),
//                new Sphere(15, new Point(0, -45, 300))
//                        .setEmission(new Color(190,45,200))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.1)),
//                new Sphere(22, new Point(-52, -25, 120))
//                        .setEmission(new Color(117,250,141))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.1)),
//                new Sphere(25, new Point(55, -24.5, 150))
//                        .setEmission(new Color(255,255,145))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(1.0)),
//                new Sphere(50, new Point(0, 0, 0))
//                        .setEmission(new Color(BLUE))//
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(0.6)));
//        scene.getLights().add(new DirectionalLight(new Color(50, 1500, 400), new Vector(-1, 1, 0)));
//        scene.getLights().add(new SpotLight(new Color(700, 400, 400), new Point(0, 80, 30), new Vector(0, -1, -10)) //
//                .setKl(4E-5).setKq(2E-7));
//
//        ImageWriter imageWriter = new ImageWriter("MiniProjectTest1", 1000, 1000);
//        camera.setImageWriter(imageWriter) //
//                .setRayTracer(new RayTracerBasic(scene)) //
//                .renderImage()
//                .writeToImage();
//    }
//    @Test
//    public void OurFinalImage() {
//        Scene scene = new Scene.SceneBuilder("Test scene").setBackground(Color.BLACK).build();
//        scene.setAmbientLight(new AmbientLight(new Color(java.awt.Color.WHITE), new Double3(0.15)));
//        Camera camera = new Camera(new Point(0, 0, -1000), new Vector(0, 0, 1), new Vector(0, -1, 0)) //
//                .setVPSize(1000, 1000).setVPDistance(1000);
//        scene.getGeometries().add(
//
//                new Sphere(190, new Point(100, 0, -200))
//                        .setEmission(new Color(BLACK))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//
//                new Sphere(145, new Point(100, 0, -200))
//                        .setEmission(new Color(BLACK))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//
//                new Sphere(95, new Point(-205, 95, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                //region Chain
//                new Sphere(10, new Point(-340, 115, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-339, 95, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-335, 75, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-330, 55, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-320, 37, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-310, 19, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-295, 5, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-280, -10, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-265, -20, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-245, -26, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-225, -31, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-205, -33, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-185, -30, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-165, -25, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-148, -17, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-132, -8, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-118, 5, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-108, 23, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-100, 40, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-95, 60, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-89, 79, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-79, 97, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-69, 115, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-57, 132, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-43, 149, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-27, 162, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(-12, 172, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(5, 182, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(22, 190, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(20, new Point(40, 196, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(60, 202, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(80, 204, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(100, 205, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(120, 205, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(140, 204, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(160, 200, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(180, 195, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(195, 185, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(212, 175, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(226, 166, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//                new Sphere(10, new Point(241, 154, -200))
//                        .setEmission(new Color(GREEN))//
//                        .setMaterial(new Material().setKd(0.8).setKs(0.8).setShininess(30).setKt(0.0).setKr(0.0)),
//
//
////endregion
//                new Polygon(new Point(-250, -210, -400), new Point(-150, -210, -400), new Point(-150, 200, -400),
//                        new Point(-250, 200, -400))
//                        .setEmission(new Color(BLACK))
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(1.0).setGlossiness(2)),
//                new Polygon(new Point(-110, -210, -400), new Point(-10, -210, -400), new Point(-10, 200, -400),
//                        new Point(-110, 200, -400))
//                        .setEmission(new Color(BLACK))
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(1.0).setGlossiness(1)),
//                new Polygon(new Point(30, -210, -400), new Point(130, -210, -400), new Point(130, 200, -400),
//                        new Point(30, 200, -400))
//                        .setEmission(new Color(BLACK))
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(1.0).setGlossiness(0.5)),
//                new Polygon(new Point(170, -210, -400), new Point(270, -210, -400), new Point(270, 200, -400),
//                        new Point(170, 200, -400))
//                        .setEmission(new Color(BLACK))
//                        .setMaterial(new Material().setKd(0.2).setKs(0.2).setShininess(30).setKt(1.0).setGlossiness(0.25)),
//
//
//                new Triangle(new Point(500, 200, -100), new Point(-500, 200, -100), new Point(1800, 200, -700))
//                        .setEmission(new Color(BLACK))//
//                        .setMaterial(new Material().setKd(0.8).setKs(1.0).setShininess(10000).setKr(1d).setGlossiness(0.5)),
//
//                new Triangle(new Point(-500, 200, -100), new Point(1800, 200, -700), new Point(-1800, 200, -700))
//                        .setEmission(new Color(BLACK))//
//                        .setMaterial(new Material().setKd(0.8).setKs(1d).setShininess(10000).setKr(1d).setGlossiness(0.5)));
//
//
//        scene.getLights().add(new DirectionalLight(new Color(10, 10, 10), new Vector(1, -1, 0)));
//        scene.getLights().add(new SpotLight(new Color(400, 400, 1020), new Point(-300, -300, -100), new Vector(2, 2, -3))
//                .setKl(0.00001).setKq(0.000005).setKc(1));
//        scene.getLights().add(new SpotLight(new Color(400, 400, 1020), new Point(0, 0, -1), new Vector(0, 0, -300))
//                .setKl(0.0001).setKq(0.00005).setKc(0.7));
//        scene.getLights().add(new PointLight(new Color(400, 400, 1020), new Point(-60, -210, -400))
//                .setKl(0.1).setKq(0.5).setKc(0.7));
//        scene.getLights().add(new PointLight(new Color(400, 400, 1020), new Point(220, -210, -400))
//                .setKl(0.1).setKq(0.5).setKc(0.7));
//
//        ImageWriter imageWriter = new ImageWriter("my picture ", 1000, 1000);
//
//        int frames = 10;
//        double angle = 360d / frames;
//        double angleRadians = 2 * Math.PI / frames;
//        double radius = camera.getP0().subtract(new Point(0,0,0)).length();
//
//        for (int i = 0; i < frames; i++) {
//            camera.rotate(0, angle, 0);
//            camera.setP0(
//                    Math.sin(angleRadians * (i + 1)) * radius,
//                    0,
//                    Math.cos(angleRadians * (i + 1)) * radius
//            );
//
//            camera.setImageWriter(new ImageWriter("moveTest" + (i + 1), 500, 500))
//                    .setantiAliasing(1)
//                    .setadaptive(true)
//                    .setthreadsCount(3)
//                    .setRayTracer(new RayTracerBasic(scene))
//                    .renderImage();
//            camera.writeToImage();
//        }
//    }
//}

package renderer;


import geometries.*;
import lighting.*;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.*;

public class PictureTest {
    @Test
    public void PictureTest() {
        Scene scene = new Scene.SceneBuilder("final picture").setBackground(new Color(255, 255, 255)).build();
        Camera camera = new Camera(new Point(0, -600, 10), new Vector(0, 1, 0), new Vector(0, 0, 1));
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

        Sphere sphere = new Sphere(100, new Point(0, 0, 0));
        //red sphere
        Sphere sphere3 = new Sphere(100, new Point(200, -100, 50));
        Sphere sphere4 = new Sphere(100, new Point(-200, -100, 50));
        sphere.setMaterial(material);
        //sphere.setEmission(new Color(255, 0, 0)).setMaterial(material1);
        sphere3.setEmission(new Color(169, 50, 50));
        sphere3.setMaterial(material);
        //blue sphere
        sphere4.setEmission(new Color(50, 50, 169));
        sphere4.setMaterial(material);

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

        Plane pln = new Plane(new Point(100, -100, -150), new Vector(0, 0, 1));
        //pln.setEmission(new Color(255,255,255));
        //.setEmission(new Color(0, 0, 0));
        pln.setMaterial(material);

        scene.getGeometries().add(sqr1, sqrt2, sqrt3, sqrt4, sqrt5, pln, sphere, sphere2, sphere3, sphere4);

        camera.setImageWriter(new ImageWriter("not final picture", 500, 500))
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage();
        camera.writeToImage();

        camera.setImageWriter(new ImageWriter("not adaptive final picture", 500, 500))
                .setantiAliasing(81)
                .setMultiThreading(3)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage();
        camera.writeToImage();

        camera.setImageWriter(new ImageWriter("adaptive final picture", 500, 500))
                .setantiAliasing(200)
                .setadaptive(true)
                .setMultiThreading(3)
                .setRayTracer(new RayTracerBasic(scene))
                .renderImage();
        camera.writeToImage();

//        int frames = 10;
//        double angle = 360d / frames;
//        double angleRadians = 2 * Math.PI / frames;
//        double radius = camera.getP0().subtract(new Point(0, 0, 0)).length();
//
//        for (int i = 0; i < frames; i++) {
//            camera.rotate(0, angle, 0);
//            camera.setP0(
//                    Math.sin(angleRadians * (i + 1)) * radius,
//                    0,
//                    Math.cos(angleRadians * (i + 1)) * radius
//            );
//
//            camera.setImageWriter(new ImageWriter("adaptive final picture" + (i + 1), 500, 500))
//                    .setantiAliasing(200)
//                    .setadaptive(true)
//                    .setMultiThreading(3)
//                    .setRayTracer(new RayTracerBasic(scene))
//                    .renderImage();
//            camera.writeToImage();
//        }
      }
}

//    @Test
//    public void PictureTest2() {
//        Scene scene = new Scene.SceneBuilder("not final picture").setBackground(new Color(255, 255, 255)).build();
//        Camera camera = new Camera(new Point(0, -600, 10), new Vector(0, 1, 0), new Vector(0, 0, 1));
//        camera.setVPSize(150, 150).setVPDistance(100);
//
//        Material material = new Material().setKd(0.4).setKs(0.5).setShininess(50).setKt(0).setKr(0.5).setKs(0.5);
//        SpotLight light = new SpotLight(new Color(255, 255, 255), new Point(0, -50, 25), new Vector(0, 2, -1));
//        light.setKc(0).setKl(0.01).setKq(0.05);
//        light.setNarrowBeam(5);
//
//        DirectionalLight directionalLight1 = new DirectionalLight(new Color(100, 100, 100), new Vector(0, 0, -1));
//        DirectionalLight directionalLight2 = new DirectionalLight(new Color(100, 100, 100), new Vector(1, 0, 0));
//        DirectionalLight directionalLight3 = new DirectionalLight(new Color(100, 100, 100), new Vector(-1, 0, 0));
//        PointLight pointLight = new PointLight(new Color(255, 255, 255), new Point(200, 50, -100));
//
//
//        //scene.lights.add(light);
//        scene.getLights().add(directionalLight1);
//        scene.getLights().add(directionalLight2);
//        scene.getLights().add(directionalLight3);
//        scene.getLights().add(pointLight);
//
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
//
//        scene.getGeometries().add(sqr1, sqrt2, sqrt3, sqrt4, sqrt5, pln, sphere, sphere2, sphere3, sphere4);
//
//        camera.setImageWriter(new ImageWriter("not final picture", 500, 500))
//                .setRayTracer(new RayTracerBasic(scene))
//                .renderImage();
//        camera.writeToImage();
//
//        camera.setImageWriter(new ImageWriter("not adaptive final picture", 500, 500))
//                .setantiAliasing(81)
//                .setMultiThreading(3)
//                .setRayTracer(new RayTracerBasic(scene))
//                .renderImage();
//        camera.writeToImage();
//
//
//        camera.setImageWriter(new ImageWriter("adaptive final picture", 500, 500))
//                .setantiAliasing(200)
//                .setadaptive(true)
//                .setMultiThreading(3)
//                .setRayTracer(new RayTracerBasic(scene))
//                .renderImage();
//        camera.writeToImage();
//
////        int frames = 10;
////        double angle = 360d / frames;
////        double angleRadians = 2 * Math.PI / frames;
////        double radius = camera.getP0().subtract(new Point(0,0,0).length();
////
////        for (int i = 0; i < frames; i++) {
////            camera.rotate(0, angle, 0);
////            camera.setP0(
////                    Math.sin(angleRadians * (i + 1)) * radius,
////                    0,
////                    Math.cos(angleRadians * (i + 1)) * radius
////            );
////
////            camera.setImageWriter(new ImageWriter("moveTest" + (i + 1), 500, 500))
////                    .setantiAliasing(1)
////                    .setadaptive(true)
////                    .setthreadsCount(3)
////                    .setRayTracer(new RayTracerBasic(scene))
////                    .renderImage();
////            camera.writeToImage();
//    }
//