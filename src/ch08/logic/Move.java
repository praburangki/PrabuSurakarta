/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.logic;

/**
 *
 * @author Prabu Rangki
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

    @Override
    public boolean equals(Object obj) {
        return sourceRow == ((Move) obj).sourceRow
                && sourceColumn == ((Move) obj).sourceColumn
                && targetRow == ((Move) obj).targetRow
                && targetColumn == ((Move) obj).targetColumn;
    }

    @Override
    public String toString() {
//        String s = "(" + sourceRow + "," + sourceColumn + ")->(" + targetRow + "," + targetColumn + ")";
        String s = sourceRow + "" + sourceColumn + "" + targetRow + "" + targetColumn 
                + (capturedPiece != null ? "p" : " ");
        
        return s;
    }
    
    @Override
    public Move clone() {
        return new Move(sourceRow, sourceColumn, targetRow, targetColumn);
    }
}
