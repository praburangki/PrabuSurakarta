/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch05.gui;

import ch05.logic.*;
import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author praburangki
 */
public class PieceDragAndDropListener implements MouseListener, MouseMotionListener {

    private List<GuiPiece> guiPieces;
    private Gui gui;

    private GuiPiece dragPiece;
    private int dragOffsetX, dragOffsetY;

    public PieceDragAndDropListener(List<GuiPiece> guiPieces, Gui gui) {
        this.guiPieces = guiPieces;
        this.gui = gui;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;

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

                    this.dragOffsetX = x - guiPiece.getX();
                    this.dragOffsetY = y - guiPiece.getY();
                    this.dragPiece = guiPiece;
                    break;
                }
            }
        }

        if (this.dragPiece != null) {
            this.guiPieces.remove(this.dragPiece);
            this.guiPieces.add(this.dragPiece);
        }
    }

    private boolean mouseOverPiece(GuiPiece guiPiece, int x, int y) {
        return guiPiece.getX() <= x
                && guiPiece.getX() + guiPiece.getWidth() >= x
                && guiPiece.getY() <= y
                && guiPiece.getY() + guiPiece.getHeight() >= y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (this.dragPiece != null) {
            int x = e.getPoint().x - this.dragOffsetX;
            int y = e.getPoint().y - this.dragOffsetY;

            gui.setNewPieceLocation(this.dragPiece, x, y);
            this.gui.repaint();
            this.dragPiece = null;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (this.dragPiece != null) {
            int x = e.getPoint().x - this.dragOffsetX;
            int y = e.getPoint().y - this.dragOffsetY;
            System.out.println("row : " + Gui.convertYToRow(y)
                    + " column : " + Gui.convertXToColumn(x));

            this.dragPiece.setX(x);
            this.dragPiece.setY(y);

            this.gui.repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

}
