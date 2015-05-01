package Surakarta.logic;

import java.util.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 *
 * @author praburangki
 */
public class PieceDragAndDropListener implements MouseListener, MouseMotionListener {
    
    private List<Piece> pieces;
    private Gui gui;
    private Piece dragPiece;
    private int dragOffsetX, dragOffsetY;

    public PieceDragAndDropListener(List<Piece> pieces, Gui gui) {
        this.pieces = pieces;
        this.gui = gui;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getPoint().x;
        int y = e.getPoint().y;
        
        for ( int i = this.pieces.size() - 1; i >= 0; i-- ) {
            Piece piece = this.pieces.get(i);
            
            if ( mouseOverPiece( piece, x, y ) ) {
                this.dragOffsetX = x - piece.getX();
                this.dragOffsetY = y - piece.getY();
                this.dragPiece = piece;
                break;
            }
        }
        
        if (this.dragPiece != null) {
            this.pieces.remove( this.dragPiece );
            this.pieces.add( this.dragPiece );
        }
    }
    
    private boolean mouseOverPiece(Piece piece, int x, int y) {
        return piece.getX() <= x &&
                piece.getX() + piece.getWidth() >= x &&
                piece.getY() <= y &&
                piece.getY() + piece.getHeight() >= y;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.dragPiece = null;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if(this.dragPiece != null) {
            this.dragPiece.setX(e.getPoint().x - this.dragOffsetX);
            this.dragPiece.setY(e.getPoint().y - this.dragOffsetY);
            this.gui.repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
    
    @Override
    public void mouseClicked(MouseEvent e) {}
    
}
