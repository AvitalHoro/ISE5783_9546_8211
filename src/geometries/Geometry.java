package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

public abstract class Geometry extends Intersectable {

   protected Color emission = Color.BLACK;



   private Material material =  new Material();

   /**
    *
    * @return emission color
    */
   public Color getEmission() {
      return emission;
   }

   /**
    *
    * @param emission
    * @return this
    */
   public Geometry setEmission(Color emission) {
      this.emission = emission;
      return this;
   }


   /**Geometry interface for some geometric body
    * @param point
    * @return vector
    */
   public abstract Vector getNormal(Point point);

   /**
    * get
    * @return material
    */
   public Material getMaterial() {
      return material;
   }

   /**
    * set
    * @param material
    * @return this Geometry
    */
   public Geometry setMaterial(Material material) {
      this.material = material;
      return this;
   }
}
