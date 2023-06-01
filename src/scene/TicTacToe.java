package scene;

import geometries.*;
import primitives.*;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * a class that generates the instruments and the full game of tic-tac-toe.
 * relative to the (0,0,0) position in space
 */
public class TicTacToe {
    private final int size;
    private final Vector vUp = new Vector(0,0,1);
    private final Vector vRight = new Vector(1,0,0);
    private final Vector vTo = new Vector(0,1,0);
    private Color boardColor;
    private Material boardMat;
    private Color xColor;
    private Material xMat;
    private Color oColor;
    private Material oMat;

    double squareSize;
    double radius ;
    double shapesHeight;
    double boardHeight;
    double linesHeight;

    //region setters
    public TicTacToe setX(Color xColor, Material xMat){
        this.xColor = xColor;
        this.xMat = xMat;
        return this;
    }

    public TicTacToe setO(Color oColor, Material oMat){
        this.oColor = oColor;
        this.oMat = oMat;
        return this;
    }

    public TicTacToe setBoard(Color boardColor, Material boardMat){
        this.boardColor = boardColor;
        this.boardMat = boardMat;
        return this;
    }

    public TicTacToe setBoardParameters(){
        squareSize = this.size / 3d;
        radius = squareSize * 0.35;
        shapesHeight = radius * 0.9;
        boardHeight = this.size * 0.05;
        linesHeight = this.size / 50d;
        return this;
    }
    //endregion

    //region ctor
    public TicTacToe(int boardSize) {
        this.size = boardSize;
        setBoardParameters();
    }
    //endregion

    //region generateLine
    /**
     * generate a triangular line on the board
     * @param vTo direction of the line
     * @param vRight direction orthogonal to the line
     * @param p start point of the line (left side of the line)
     * @return geometry that represents the line
     */
    public Geometries generateLine(Vector vTo, Vector vRight, Point p){
        p = p.add(vUp.scale(linesHeight));

        // points on the perimeter of the triangular lines
        Point A = p.add(vRight.scale(linesHeight / 2)
                .add(vUp.scale(-linesHeight)));
        Point B = A.add(vTo.scale(size));

        Point secondHead = p.add(vTo.scale(size));

        Point C = B.add(vRight.scale(-linesHeight));
        Point D = A.add(vRight.scale(-linesHeight));

        return new Geometries( new Polygon(p,A,B,secondHead)    // first side
                .setEmission(boardColor)
                .setMaterial(boardMat),
                new Polygon(p,secondHead,C,D)     // second side
                        .setEmission(boardColor)
                        .setMaterial(boardMat),
                new Triangle(A,D,p)               // base 1
                        .setEmission(boardColor)
                        .setMaterial(boardMat),
                new Triangle(B,C,secondHead)      // base 2
                        .setEmission(boardColor)
                        .setMaterial(boardMat));

    }
    //endregion

    //region generateLines
    /**
     * generate 4 grid lines on the tic-tac-toe board relative to the board size
     */
    public Geometries generateLines(){
        double s1 = -size / 6d;
        double s2 = -size / 2d;

        Geometries lines = generateLine(vTo, vRight, new Point(s1, s2, boardHeight));
        lines.add(generateLine(vTo, vRight, new Point(-s1, s2, boardHeight)));
        lines.add(generateLine(vRight, vTo.scale(-1), new Point(s2, -s1, boardHeight)));
        lines.add(generateLine(vRight, vTo.scale(-1), new Point(s2, s1, boardHeight)));
        return lines;
    }
    //endregion

    //region board
    /**
     * create the board shape, the base and the lines separating the board to squares.
     * giving the board the grid shape of a tic-tac-toe game board.
     *
     * all the calculations are made with the knowledge that
     * the middle of the board is in (0,0,0) .
     */
    public Geometries generateBoard(){
        double coordinate = size / 2d;

        //region 4 points for bottom base
        Point A = new Point (-coordinate, coordinate,0);
        Point B = new Point (coordinate, coordinate,0);
        Point C = new Point (coordinate, -coordinate,0);
        Point D = new Point (-coordinate, -coordinate,0);
        //endregion

        //region 4 points for top base
        Point E = A.add(vUp.scale(boardHeight));
        Point F = B.add(vUp.scale(boardHeight));
        Point G = C.add(vUp.scale(boardHeight));
        Point H = D.add(vUp.scale(boardHeight));
        //endregion

        // two bases
        Geometry topBase = new Polygon(A,B,C,D).setEmission(boardColor).setMaterial(boardMat);
        Geometry bottomBase = new Polygon(E,F,G,H).setEmission(boardColor).setMaterial(boardMat);

        //4 sides
        Geometry side1 = generateOrthogonalRectangle(E,F, -boardHeight).setEmission(boardColor).setMaterial(boardMat);
        Geometry side2 = generateOrthogonalRectangle(F,G, -boardHeight).setEmission(boardColor).setMaterial(boardMat);
        Geometry side3 = generateOrthogonalRectangle(G,H, -boardHeight).setEmission(boardColor).setMaterial(boardMat);
        Geometry side4 = generateOrthogonalRectangle(H,E, -boardHeight).setEmission(boardColor).setMaterial(boardMat);

        Geometries lines = generateLines();
        lines.add(topBase, bottomBase, side1, side2, side3, side4);
        return lines;
    }
    //endregion

    //region X
    /**
     * crate a 3D shape of an X in a size relative to the size of the board
     * @param A the middle of the bottom base of the X
     */
    public Geometries generateX(Point A){
        return generateX(A, radius * 2, shapesHeight);
    }

    /**
     * create a 3D shape of an X with a given height and length of one '/' of the X
     * @param A the middle of the X - on the bottom base of the shape
     * @return the 3D representation of an X
     */
    public Geometries generateX(Point A, double totalLength, double height){
        Vector vTo = this.vTo.moveClockwiseAround(this.vUp, Math.random() * 25 + 45);
        Vector vRight = vTo.crossProduct(vUp).normalize();

        double width = totalLength * 0.25;
        double length = totalLength * 0.75 * 0.5;

        // move the middle point to be on one edge of the X
        A = A.add(vTo.scale(-width / 2d))
                .add(vRight.scale(-totalLength / 2d));

        //region set points
        // long rectangle base
        Point B = A.add(vTo.scale(width));
        Point C = B.add(vRight.scale(totalLength));
        Point D = A.add(vRight.scale(totalLength));
        // short rectangle base
        Point E = B.add(vRight.scale(width + length));
        Point F = A.add(vRight.scale(width + length));
        Point G = A.add(vRight.scale(length));
        Point H = B.add(vRight.scale(length));

        Point I = H.add(vTo.scale(length));
        Point J = E.add(vTo.scale(length));
        Point K = F.add(vTo.scale(-length));
        Point L = G.add(vTo.scale(-length));
        //endregion

        //region x bottom base
        Geometry base1 = new Polygon(A,B,C,D)
                .setMaterial(xMat).setEmission(xColor.reduce(2));
        Geometry base2 = new Polygon(E,H,I,J)
                .setMaterial(xMat).setEmission(xColor.reduce(2));
        Geometry base3 = new Polygon(F,G,L,K)
                .setMaterial(xMat).setEmission(xColor.reduce(2));
        Geometries XShape = new Geometries(base1, base2, base3); // bottom base
        //endregion

        //region x upper base
        // move up in vUp's direction
        base1 = new Polygon(A.add(this.vUp.scale(height)),B.add(this.vUp.scale(height)),
                C.add(this.vUp.scale(height)),D.add(this.vUp.scale(height)))
                .setMaterial(xMat).setEmission(xColor.reduce(2));
        base2 = new Polygon(E.add(this.vUp.scale(height)),H.add(this.vUp.scale(height)),
                I.add(this.vUp.scale(height)),J.add(this.vUp.scale(height)))
                .setMaterial(xMat).setEmission(xColor.reduce(2));
        base3 = new Polygon(F.add(this.vUp.scale(height)),G.add(this.vUp.scale(height)),
                L.add(this.vUp.scale(height)),K.add(this.vUp.scale(height)))
                .setMaterial(xMat).setEmission(xColor.reduce(2));

        XShape.add(base1, base2, base3); // upper base
        //endregion

        //region x sides
        // side 1
        XShape.add(generateOrthogonalRectangle(A,B,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 2
        XShape.add(generateOrthogonalRectangle(B,H,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 3
        XShape.add(generateOrthogonalRectangle(H,I,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 4
        XShape.add(generateOrthogonalRectangle(I,J,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 5
        XShape.add(generateOrthogonalRectangle(J,E,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 6
        XShape.add(generateOrthogonalRectangle(E,C,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 7
        XShape.add(generateOrthogonalRectangle(C,D,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 8
        XShape.add(generateOrthogonalRectangle(D,F,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 9
        XShape.add(generateOrthogonalRectangle(F,K,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 10
        XShape.add(generateOrthogonalRectangle(K,L,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 11
        XShape.add(generateOrthogonalRectangle(L,G,height)
                .setMaterial(xMat).setEmission(xColor));
        // side 12
        XShape.add(generateOrthogonalRectangle(G,A,height)
                .setMaterial(xMat).setEmission(xColor));
        //endregion

        return XShape;
    }

    /**
     * for 2 given points, generate a standing rectangle in a specified height.
     * 'standing' meaning in the direction of vUp
     * @param p1 point on the base of the X
     * @param p2 point on the base of the X
     * @param height of the X
     * @return the side of X between p1 and p2
     */
    private Polygon generateOrthogonalRectangle(Point p1, Point p2, double height){
        return new Polygon(p1, p1.add(vUp.scale(height)),
                p2.add(vUp.scale(height)),p2);
    }
    //endregion

    //region O

    /**
     * create a 3D shape of an O in the size relative to the size of the board
     * @param center of the O pond
     */
    public Geometries generateO(Point center) {
        return generateO(center, radius, shapesHeight);
    }

    /**
     * create a 3D shape of an O with a given height and radius of the O
     * @param center of the O pond
     */
    public Geometries generateO(Point center, double radius, double height) {
        return new Geometries(new Cylinder(radius, new Ray(center, vUp), height)
                .setMaterial(oMat)
                .setEmission(oColor),
                new Circle(center, radius, vUp)
                        .setEmission(oColor.reduce(2))
                        .setMaterial(oMat));
    }
    //endregion

    //region tic-tac-toe
    public Geometries generateTicTacToe(){
        Geometries TTT = new Geometries(generateBoard());

        // 3 o
        Point centerSquare = middleOfSquare(2,2); //
        TTT.add(generateO(centerSquare, radius, shapesHeight));
        centerSquare = middleOfSquare(2,0); //
        TTT.add(generateO(centerSquare, radius, shapesHeight));
        centerSquare = middleOfSquare(1,2); //
        TTT.add(generateO(centerSquare, radius, shapesHeight));


        // 3 x
        centerSquare = middleOfSquare(1, 1); //
        TTT.add(generateX(centerSquare));
        centerSquare = middleOfSquare(1, 0); //
        TTT.add(generateX(centerSquare));
        centerSquare = middleOfSquare(0, 2); //
        TTT.add(generateX(centerSquare));

        return TTT; //
    }
    //endregion

    //region same method from the camera
    /**
     * find the middle point of a square on the game board.
     * @return the center of (i,j) square of the grid on the board
     */
    private Point middleOfSquare(int i, int j) {
        // calculating the middle of the "pixel"
        double yI = alignZero(-(i - (2) / 2d) * squareSize);       // move pc Yi pixels
        double xJ = alignZero((j - (2) / 2d) * squareSize);        // move pc Xj pixels

        Point PIJ =  new Point(0,0,0);
        if(!isZero(xJ))  PIJ = PIJ.add(vRight.scale(xJ));       // move the point in vRight direction
        if(!isZero(yI))  PIJ = PIJ.add(vTo.scale(yI));          // move the point in vUp direction
        return PIJ.add(vUp.scale(boardHeight));
    }
    //endregion

}
