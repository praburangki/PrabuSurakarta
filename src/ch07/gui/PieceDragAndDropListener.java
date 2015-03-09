/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch07.gui;

import ch07.logic.Game;
import ch07.logic.Piece;
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
        if (!this.gui.isDraggingGamePiecesEnabled()) {
            return;
        }

        int x = e.getPoint().x;
        int y = e.getPoint().y;

        // find out which piece to move.
        // we check the list from top to buttom
        // (therefore we itereate in reverse order)
        //
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
                    // calculate offset, because we do not want the drag piece
                    // to jump with it's upper left corner to the current mouse
                    // position
                    //
                    this.dragOffsetX = x - guiPiece.getX();
                    this.dragOffsetY = y - guiPiece.getY();
                    this.gui.setDragPiece(guiPiece);
                    this.gui.repaint();
                    break;
                }
            }
        }

        // move drag piece to the top of the list
        if (this.gui.getDragPiece() != null) {
            this.guiPieces.remove(this.gui.getDragPiece());
            this.guiPieces.add(this.gui.getDragPiece());
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

            // set game piece to the new location if possible
            //
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
