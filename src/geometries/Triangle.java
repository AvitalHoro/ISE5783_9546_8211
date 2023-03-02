package geometries;

import primitives.Point;
import primitives.Vector;

import java.util.List;

public class Triangle extends Polygon{

    public Triangle(Point... vertices) {
        super(vertices);
    }
    @Override
    public Vector getNormal() {
        return null;
    }

}
