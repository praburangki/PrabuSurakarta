package Applet;

import java.awt.Color;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Vector;

public class PlayerAi extends Thread {
    @Override
    public void run() {
        ThinkingDisplay td = null;
        try {
            td = new ThinkingDisplay();
            td.start();
            Move move;
            if (Game.moveNumber > 8) move = Game.pieces.bestBlackMove();
            else move = Game.pieces.randomBlackMove();
            
            td.stop();

            if (move.isCapture) {
                Vector v = Game.pieces.pathOfCapture(move);
                Enumeration e = v.elements();
                if (e.hasMoreElements()) {
                    do {
                        Point p = (Point) e.nextElement();
                        Game.board.drawSinglePiece(p, Color.cyan);
                        Game.applet.repaint();
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException ex) {}
                    } while (e.hasMoreElements());
                }
            } else {
                int i = 0;
                do {
                    Game.board.redraw(Game.pieces);
                    Game.board.drawSinglePiece(new Point(move.startCol, move.startRow), Color.red);
                    Game.applet.repaint();
                    try {
                        Thread.sleep(300L);
                    } catch (InterruptedException ex) {}
                    Game.board.redraw(Game.pieces);
                    Game.board.drawSinglePiece(new Point(move.endCol, move.endRow), Color.red);
                    Game.applet.repaint();
                    try {
                        Thread.sleep(300L);
                    } catch (InterruptedException ex) {}
                    i++;
                } while (i < 4);
            }
            
            Game.pieces.doMove(move);
        } catch (NoMoreMovesException ex) {
            Game.gameOver();
        } finally {
            if (td != null) td.stop();
        }
        Game.setPlayer(false);
    }
}
