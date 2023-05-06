package scene;

import geometries.Geometries;
import geometries.Intersectable;
import lighting.AmbientLight;
import primitives.Color;

public class Scene {
    String name;
    Color background;
    AmbientLight ambientLight;
    Geometries geometries;
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
}
