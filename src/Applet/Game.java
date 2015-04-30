package Applet;

import java.awt.Image;

public class Game {
    public static Board board = null;
    public static Piece pieces = null;
    public static final boolean black = true, white = false;
    public static boolean whitePlayer = false, gameOver = false;
    static Surakarta applet = null;
    static Thread thread = null;
    public static int moveNumber = 0, difficulty = 1;
    
    public static void init(Surakarta surakarta) {
        applet = surakarta;
        int i = Math.min(applet.size().width, applet.size().height);
        Image img = applet.createImage(i, i);
        pieces = new Piece();
        board = new Board(i, img);
        whitePlayer = false;
        if(thread != null) thread.stop();
        thread = null;
        moveNumber = 0;
    }
    
    public static void setPlayer(boolean turn) {
        whitePlayer = turn;
        board.redraw(pieces);
        applet.repaint();
        moveNumber++;
        if(turn) {
            applet.player.activate(false);
            thread = new PlayerAi();
            thread.start();
        } else applet.player.activate(true);
    }
    
    public static void gameOver() {
        gameOver = true;
    }
}
