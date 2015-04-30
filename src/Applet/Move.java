package Applet;

import java.awt.Point;

public final class Move {

    public int startCol;
    public int startRow;
    public int endCol;
    public int endRow;
    public boolean isWhite;
    public boolean isBlack;
    public boolean isCapture;

    public Move(Point p1, Point p2, boolean isWhite, boolean isCapture) {
        this.startCol = p1.x;
        this.startRow = p1.y;
        this.endCol = p2.x;
        this.endRow = p2.y;
        this.isWhite = isWhite;
        this.isBlack = (isWhite ^ true);
        this.isCapture = isCapture;
    }

    public Move(int startRow, int startCol, int endRow, int endCol, boolean isWhite, boolean isCapture) {
        this.startCol = startCol;
        this.startRow = startRow;
        this.endCol = endCol;
        this.endRow = endRow;
        this.isWhite = isWhite;
        this.isBlack = (isWhite ^ true);
        this.isCapture = isCapture;
    }

    public static boolean equals(Move move1, Move move2) {
        return (move1.endCol == move2.endCol)
                && (move1.endRow == move2.endRow)
                && (move1.startRow == move2.startRow)
                && (move1.startCol == move2.startCol);
    }
}
