package geometries;

import primitives.Point;
import primitives.Vector;

public interface Geometry extends Intersectable {
   /**Geometry interface for some geometric body
    * @param point
    * @return vector
    */

   public Vector getNormal(Point point);

}
