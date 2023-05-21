package scene;

import com.google.gson.Gson;
import geometries.Geometries;
import lighting.AmbientLight;
import lighting.LightSource;
import primitives.Color;

import java.util.LinkedList;
import java.util.List;

public class Scene {

    private final String name;
    private final Color background;
    private final Geometries geometries;
    private final List<LightSource> lights;
    private AmbientLight ambientLight;

    public String getName() {
        return name;
    }

    public Color getBackground() {
        return background;
    }

    public AmbientLight getAmbientLight() {
        return ambientLight;
    }

    public Geometries getGeometries() {
        return geometries;
    }

    public List<LightSource> getLights() {
        return lights;
    }

    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    private Scene(SceneBuilder builder) {
        name = builder.name;
        background = builder.background;
        ambientLight = builder.ambientLight;
        geometries = builder.geometries;
        lights = builder.lights;
    }


    public void writeGsonFile(String filename){
        Gson gson= new Gson();

    }

    public static class SceneBuilder {

        private final String name;
        private List<LightSource> lights = new LinkedList<>();
        private Color background = Color.BLACK;
        private AmbientLight ambientLight = AmbientLight.NONE;
        private Geometries geometries = new Geometries();

        public SceneBuilder(String name) {
            this.name = name;
        }

        public SceneBuilder setBackground(Color background) {
            this.background = background;
            return this;
        }

        public SceneBuilder setLights(List<LightSource> lights) {
            this.lights = lights;
            return this;
        }

        public SceneBuilder setAmbientLight(AmbientLight ambientLight) {
            this.ambientLight = ambientLight;
            return this;
        }

        public SceneBuilder setGeometries(Geometries geometries) {
            this.geometries = geometries;
            return this;
        }

        public Scene build() {
            // validateObject(scene);
            return new Scene(this);
        }

        private void validateObject(Scene scene) {
            //nothing to do
        }

        public SceneBuilder readXmlFile(String filename) {
            //todo
            return this;
        }
    }
}
