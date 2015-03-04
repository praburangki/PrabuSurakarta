/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch03.logic;

/**
 *
 * @author praburangki
 */
public class Piece {
    private int color;
    public static final int COLOR_WHITE = 0;
    public static final int COLOR_BLACK = 1;
    
    private int row;
    
    public static final int ROW_1 = 0;
    public static final int ROW_2 = 1;
    public static final int ROW_3 = 2;
    public static final int ROW_4 = 3;
    public static final int ROW_5 = 4;
    public static final int ROW_6 = 5;
    
    private int column;
    
    public static final int COLUMN_A = 0;
    public static final int COLUMN_B = 1;
    public static final int COLUMN_C = 2;
    public static final int COLUMN_D = 3;
    public static final int COLUMN_E = 4;
    public static final int COLUMN_F = 5;
    
    private boolean isCaptured = false;

    public Piece(int color, int row, int column) {
        this.color = color;
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        String strColor = this.color == COLOR_WHITE ? "white" : "black";
        
        String strRow = getRowString(this.row);
        String strColumn = getColumnString(this.column);
        
        return strColor + " " + strRow + "/" + strColumn;
    }
    
    public static String getRowString(int row) {
        String strRow = "unknown";
        switch(row) {
            case ROW_1 : strRow = "1"; break;
            case ROW_2 : strRow = "2"; break;
            case ROW_3 : strRow = "3"; break;
            case ROW_4 : strRow = "4"; break;
            case ROW_5 : strRow = "5"; break;
            case ROW_6 : strRow = "6"; break;
        }
        
        return strRow;
    }
    
    public static String getColumnString(int column) {
        String strColumn = "unknown";
        switch(column) {
            case COLUMN_A : strColumn = "A"; break;
            case COLUMN_B : strColumn = "B"; break;
            case COLUMN_C : strColumn = "C"; break;
            case COLUMN_D : strColumn = "D"; break;
            case COLUMN_E : strColumn = "E"; break;
            case COLUMN_F : strColumn = "F"; break;
        }
        
        return strColumn;
    }
    
    public void isCaptured(boolean isCaptured) {
        this.isCaptured = isCaptured;
    }
    
    public boolean isCaptured() {
        return this.isCaptured;
    }
}
