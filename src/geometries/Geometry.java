package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

public abstract class Geometry extends Intersectable{

   protected Color emission = Color.BLACK;
   private Material material =  new Material();

   /**
    * get of emission
    * @return emission color
    */
   public Color getEmission() {
      return emission;
   }

   /**
    * set of emission
    * @param emission
    * @return this
    */
   public Geometry setEmission(Color emission) {
      this.emission = emission;
      return this;
   }

   /**Geometry interface for some geometric shape
    * (to find the intersection points of the ray with the surface of the object)
    * @param point
    * @return vector
    */
   public abstract Vector getNormal(Point point);

   /**
    * get material
    * @return material
    */
   public Material getMaterial() {
      return material;
   }

   /**
    * set material
    * @param material
    * @return this Geometry
    */
   public Geometry setMaterial(Material material) {
      this.material = material;
      return this;
   }
}
