package scene;

import geometries.Geometries;
import geometries.Intersectable;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

public class Scene {
    public String name;
    public Color background;
    public AmbientLight ambientLight;

    public Geometries geometries;

    public List<LightSource> lights =  new LinkedList<>();
    /**
     * construct a scene. giving default values to all the fields
     */
    public Scene(String name) {
        this.geometries = new Geometries();
        this.name = name;
        this.background = Color.BLACK;
        this.ambientLight = AmbientLight.NONE;
    }
    //endregion

    //region setBackground
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }
    //endregion

    //region setGeometries
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }
    //endregion

    //region addGeometries
    public Scene addGeometry(Intersectable geometry){
        geometries.add(geometry);
        return this;
    }
    //endregion

    //region setAmbientLight
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     *
     * @param lights
     * @return this scene
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }

    /**
     * Getter for the geometries structures in the scene.
     * @return The geometries object.
     */
    public Geometries getGeometries() {
        return this.geometries;
    }
}
