package Surakarta.logic;

import java.awt.Point;
import java.util.Vector;

/**
 *
 * @author PrabuRangki
 */
public class Move {
    public int sourceRow;
    public int sourceColumn;
    public int targetRow;
    public int targetColumn;
    public boolean isWhite;
    public boolean isBlack;
    public boolean isCapture;
    public int score;
    public Piece capturedPiece;
    public Vector bestMove;

    public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
        bestMove = new Vector();
    }
    
    public Move(Point p1, Point p2, boolean isWhite, boolean isCapture) {
        this.sourceColumn = p1.x;
        this.sourceRow = p1.y;
        this.targetColumn = p2.x;
        this.targetRow = p2.y;
        this.isWhite = isWhite;
        this.isBlack = (isWhite ^ true);
        this.isCapture = isCapture;
        bestMove = new Vector();
    }

    public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn, boolean isWhite, boolean isCapture) {
        this.sourceColumn = sourceColumn;
        this.sourceRow = sourceRow;
        this.targetColumn = targetColumn;
        this.targetRow = targetRow;
        this.isWhite = isWhite;
        this.isBlack = (isWhite ^ true);
        this.isCapture = isCapture;
        bestMove = new Vector();
    }

    @Override
    public boolean equals(Object obj) {
        return sourceRow == ((Move) obj).sourceRow
                && sourceColumn == ((Move) obj).sourceColumn
                && targetRow == ((Move) obj).targetRow
                && targetColumn == ((Move) obj).targetColumn;
    }

    @Override
    public String toString() {
        return "(" + sourceRow + "," + sourceColumn + ")->(" + targetRow + "," + targetColumn + ")";
    }
    
    public Move copy() {
        return new Move(sourceRow, sourceColumn, targetRow, targetColumn);
    }
}
