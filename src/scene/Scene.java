package scene;

import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;

/**
 * Scene class represents Scene in its name, AmbientLight,
 * Color of background and geometry objects in the scene
 * This class is responsible for creating and describing the scene
 * @author Michal Superfine & Evgi
 */
public class Scene {
    public final String name;
    public Color background;
    public AmbientLight ambientLight;

    public Geometries geometries;

    public Scene(String name) {
        this.name = name;
        this.background = Color.BLACK;
        this.ambientLight = AmbientLight.NONE;
        this.geometries = new Geometries();
    }

    public Scene setBackground(Color background){
        this.background = background;
        return this;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;    }

    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;    }

}