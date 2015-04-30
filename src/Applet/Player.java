package Applet;

import java.awt.Color;
import java.awt.Point;

public class Player {
    Point pStart = null;
    Point pEnd = null;
    boolean isActive = false;

    public void activate(boolean act) {
        this.isActive = act;
        this.pStart = null;
    }

    public void mouseDown(Point p) {
        if (this.isActive) {
            this.pStart = Game.board.getSquare(p);
            if (this.pStart != null) {
                Piece pieces = (Piece) Game.pieces.clone();
                Game.board.redraw(pieces.remove(this.pStart));
                if (Game.pieces.isWhite(this.pStart)) {
                    Game.board.drawLegalMoves(Game.pieces.getLegalMoves(this.pStart));
                    Game.board.drawSinglePiece(this.pStart, Color.gray);
                    Game.applet.repaint();
                } else this.pStart = null;
            }
        }
    }

    public void mouseUp(Point p) {
        if ((this.isActive) && (this.pStart != null)) {
            this.pEnd = Game.board.getSquare(p);
            if ((this.pEnd != null) && (this.pStart != null)) {
                if (Game.pieces.attemptMove(this.pStart, this.pEnd, !Game.whitePlayer)) {
                    activate(false);
                    Game.setPlayer(true);
                    return;
                }
            }
            Game.setPlayer(false);
        }
    }
}
