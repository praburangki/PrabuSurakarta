/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch06.gui;

import ch06.logic.Piece;
import java.awt.Image;

/**
 *
 * @author Prabu Rangki
 */
public class GuiPiece {

    private Image img;
    private int x, y;
    private Piece piece;

    public GuiPiece(Image img, Piece piece) {
        this.img = img;
        this.piece = piece;

        resetToUnderlyingPiecePosition();
    }

    public Image getImg() {
        return img;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return img.getHeight(null);
    }

    public int getHeight() {
        return img.getHeight(null);
    }

    public int getColor() {
        return piece.getColor();
    }

    @Override
    public String toString() {
        return this.piece + " " + x + "/" + y;
    }
    /**
	 * move the gui piece back to the coordinates that
	 * correspond with the underlying piece's row and column
	 */
    
    public void resetToUnderlyingPiecePosition() {
        x = Gui.convertColumnToX(piece.getColumn());
        y = Gui.convertRowToY(piece.getRow());
    }
    
    public Piece getPiece() {
        return piece;
    }
    
    public boolean isCaptured() {
        return piece.isCaptured();
    }
}
