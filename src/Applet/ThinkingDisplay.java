package Applet;

import java.awt.Color;
import java.awt.Graphics;

class ThinkingDisplay extends Thread {
    @Override
    public void run() {
        int i = 0;
        Color c = Color.magenta;
        Graphics g = Game.board.img.getGraphics();
        g.setColor(Color.cyan);
        g.fillRect(0, 0, 30, 30);
        g.setColor(c);
        for (;;) {
            i += 10;
            if (i > 360) {
                i = 0;

                if (c == Color.magenta) c = Color.yellow;
                else c = Color.magenta;
                
                g.setColor(c);
            }
            g.fillArc(0, 0, 30, 30, 0, i);
            Game.applet.repaint(0, 0, 30, 30);
            try {
                Thread.sleep(62L);
            } catch (InterruptedException e) {}
        }
    }
}
