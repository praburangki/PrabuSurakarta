/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch06.gui;

import ch06.logic.Game;
import ch06.logic.Piece;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

/**
 *
 * @author Prabu Rangki
 */
public class PieceDragAndDropListener implements MouseListener, MouseMotionListener {

    private List<GuiPiece> guiPieces;
    private Gui gui;

    private int dragOffsetX, dragOffsetY;

    public PieceDragAndDropListener(List<GuiPiece> guiPieces, Gui gui) {
        this.guiPieces = guiPieces;
        this.gui = gui;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;

        for (int i = guiPieces.size() - 1; i >= 0; i--) {
            GuiPiece guiPiece = guiPieces.get(i);
            if (guiPiece.isCaptured()) {
                continue;
            }

            if (mouseOverPiece(guiPiece, x, y)) {
                if ((gui.getGameState() == Game.GAME_STATE_WHITE
                        && guiPiece.getColor() == Piece.COLOR_WHITE)
                        || (gui.getGameState() == Game.GAME_STATE_BLACK
                        && guiPiece.getColor() == Piece.COLOR_BLACK)) {

                    dragOffsetX = x - guiPiece.getX();
                    dragOffsetY = y - guiPiece.getY();
                    gui.setDragPiece(guiPiece);
                    gui.repaint();
                    break;
                }
            }
        }

        if (gui.getDragPiece() != null) {
            guiPieces.remove(gui.getDragPiece());
            guiPieces.add(gui.getDragPiece());
        }
    }

    /**
     * check whether the mouse is currently over this piece
     *
     * @param piece the playing piece
     * @param x x coordinate of mouse
     * @param y y coordinate of mouse
     * @return true if mouse is over the piece
     */
    private boolean mouseOverPiece(GuiPiece guiPiece, int x, int y) {
        return guiPiece.getX() <= x
                && guiPiece.getX() + guiPiece.getWidth() >= x
                && guiPiece.getY() <= y
                && guiPiece.getY() + guiPiece.getHeight() >= y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gui.getDragPiece() != null) {
            int x = e.getPoint().x - dragOffsetX;
            int y = e.getPoint().y - dragOffsetY;

            gui.setNewPieceLocation(gui.getDragPiece(), x, y);
            gui.repaint();
            gui.setDragPiece(null);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (gui.getDragPiece() != null) {
            int x = e.getPoint().x - dragOffsetX;
            int y = e.getPoint().y - dragOffsetY;

            System.out.println("row : " + Gui.convertYToRow(y) + " column : " + Gui.convertXToColumn(x));
            GuiPiece dragPiece = gui.getDragPiece();
            dragPiece.setX(x);
            dragPiece.setY(y);
            
            gui.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
