package Surakarta.gui;

import Surakarta.logic.Piece;
import java.awt.Image;

/**
 *
 * @author praburangki
 */
public class GuiPiece {

    private Image img;
    private int x, y;
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
