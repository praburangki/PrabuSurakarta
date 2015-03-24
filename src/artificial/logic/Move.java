/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial.logic;

/**
 *
 * @author praburangki
 */
public class Move {

    public int sourceRow;
    public int sourceColumn;
    public int targetRow;
    public int targetColumn;

    public int score;
    public Piece capturedPiece;

    public Move(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        this.sourceRow = sourceRow;
        this.sourceColumn = sourceColumn;
        this.targetRow = targetRow;
        this.targetColumn = targetColumn;
    }

    public String toString() {
        return Piece.getColumnString(sourceColumn) + "/" + Piece.getRowString(sourceRow)
                + " -> " + Piece.getColumnString(targetColumn) + "/" + Piece.getRowString(targetRow);
    }
    
    public Move copy() {
        return new Move(sourceRow, sourceColumn, targetRow, targetColumn);
    }
}
