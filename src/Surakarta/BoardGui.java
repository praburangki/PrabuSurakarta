package Surakarta;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

/**
 * @author praburangki
 */
public class BoardGui {
    public Image image;
    int boardDimension;

    public BoardGui(int boardDimension, Image image) {
        this.boardDimension = boardDimension;
        this.image = image;
        redraw();
    }
    
    void redraw() {
        Graphics g = image.getGraphics();
        
        g.setColor(Color.blue);
        g.fillRect(0, 0, this.boardDimension, this.boardDimension);
    }
}
