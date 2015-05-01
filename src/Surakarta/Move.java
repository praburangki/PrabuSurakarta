package Surakarta;

import java.awt.Point;

/**
 * @author praburangki
 */
public class Move {
    public int sourceRow, sourceColumn, targetRow, targetColumn;
    public boolean isWhite, isBlack, isCapture;
    
    public Move(Point sourcePoint, Point targetPoint, boolean isWhite, boolean isCapture) {
        sourceRow = sourcePoint.y;
        sourceColumn = sourcePoint.x;
        targetRow = targetPoint.y;
        targetColumn = targetPoint.x;
        this.isWhite = isWhite;
        isBlack = isWhite ^ true;
        this.isCapture = isCapture;
    }

    public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn, boolean isWhite, boolean isCapture) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
        this.isWhite = isWhite;
        isBlack = isWhite ^ true;
        this.isCapture = isCapture;
    }
    
    public static boolean equals(Move m1, Move m2) {
        return m1.targetColumn == m2.targetColumn && m1.targetRow == m1.targetRow &&
                m1.sourceRow == m2.sourceRow && m2.sourceColumn == m1.sourceColumn;
    }
}
