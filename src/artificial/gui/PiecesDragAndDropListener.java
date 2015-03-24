/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial.gui;

import artificial.logic.Game;
import artificial.logic.Piece;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

/**
 *
 * @author praburangki
 */
public class PiecesDragAndDropListener implements MouseListener, MouseMotionListener {

    private List<GuiPiece> guiPieces;
    private Gui gui;

    private int dragOffsetX;
    private int dragOffsetY;

    public PiecesDragAndDropListener(List<GuiPiece> guiPieces, Gui gui) {
        this.guiPieces = guiPieces;
        this.gui = gui;
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        if (!this.gui.isDraggingGamePiecesEnabled()) {
            return;
        }

        int x = evt.getPoint().x;
        int y = evt.getPoint().y;

		// find out which piece to move.
        // we check the list from top to buttom
        // (therefore we itereate in reverse order)
        //
        for (int i = this.guiPieces.size() - 1; i >= 0; i--) {
            GuiPiece guiPiece = this.guiPieces.get(i);
            if (guiPiece.isCaptured()) {
                continue;
            }

            if (mouseOverPiece(guiPiece, x, y)) {

                if ((this.gui.getGameState() == Game.GAME_STATE_WHITE
                        && guiPiece.getColor() == Piece.COLOR_WHITE)
                        || (this.gui.getGameState() == Game.GAME_STATE_BLACK
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
    public void mouseReleased(MouseEvent evt) {
        if (this.gui.getDragPiece() != null) {
            int x = evt.getPoint().x - this.dragOffsetX;
            int y = evt.getPoint().y - this.dragOffsetY;

			// set game piece to the new location if possible
            //
            gui.setNewPieceLocation(this.gui.getDragPiece(), x, y);
            this.gui.repaint();
            this.gui.setDragPiece(null);
        }
    }

    @Override
    public void mouseDragged(MouseEvent evt) {
        if (this.gui.getDragPiece() != null) {

            int x = evt.getPoint().x - this.dragOffsetX;
            int y = evt.getPoint().y - this.dragOffsetY;

            GuiPiece dragPiece = this.gui.getDragPiece();
            dragPiece.setX(x);
            dragPiece.setY(y);

            this.gui.repaint();
        }

    }

    @Override
    public void mouseMoved(MouseEvent arg0) {
    }

}
