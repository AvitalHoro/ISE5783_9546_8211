package geometries;

import primitives.Point;
import primitives.Vector;

public interface Geometry {//An interface for some geometric body
   public Vector getNormal(Point point);       /**return normal of point*/

}
