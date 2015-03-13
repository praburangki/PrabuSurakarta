/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial.gui;

import artificial.logic.Piece;
import java.awt.Image;

/**
 *
 * @author praburangki
 */
public class GuiPiece {

    private Image img;
    private int x;
    private int y;
    private Piece piece;

    public GuiPiece(Image img, Piece piece) {
        this.img = img;
        this.piece = piece;

        this.resetToUnderlyingPiecePosition();
    }

    public Image getImg() {
        return img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return img.getWidth(null);
    }

    public int getHeight() {
        return img.getHeight(null);
    }

    public int getColor() {
        return this.piece.getColor();
    }

    @Override
    public String toString() {
        return this.piece + " " + x + "/" + y;
    }

    /**
     * move the gui piece back to the coordinates that correspond with the
     * underlying piece's row and column
     */
    public void resetToUnderlyingPiecePosition() {
        this.x = Gui.convertColumnToX(piece.getColumn());
        this.y = Gui.convertRowToY(piece.getRow());
    }
    
    public boolean isCaptured() {
        return this.piece.isCaptured();
    }
}
