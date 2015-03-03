/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch01;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author praburangki
 */
public class Gui extends JPanel {
    private static final int COLOR_WHITE = 0;
    private static final int COLOR_BLACK = 1;
    
    private static final int BOARD_START_X = 377;
    private static final int BOARD_START_Y = 51;
    
    private static final int TILE_OFFSET_X = 45;
    private static final int TILE_OFFSET_Y = 75;
    
    private Image imgBackground;
    
    private List<Piece> pieces = new ArrayList<>();

    public Gui() {
        URL urlBackgroundImg = getClass().getResource("/img/surakarta.png");
        this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();
        
        for (int i = 0; i < 6; i++) {
            createAndAddPiece(COLOR_WHITE, BOARD_START_X + TILE_OFFSET_X * i,
                    BOARD_START_Y + TILE_OFFSET_Y);
        }

        for (int i = 0; i < 6; i++) {
            createAndAddPiece(COLOR_WHITE, BOARD_START_X + TILE_OFFSET_X * i,
                    BOARD_START_Y + TILE_OFFSET_Y + 45);
        }

        for (int i = 0; i < 6; i++) {
            createAndAddPiece(COLOR_BLACK, BOARD_START_X + TILE_OFFSET_X * i,
                    BOARD_START_Y + TILE_OFFSET_Y + 225);
        }

        for (int i = 0; i < 6; i++) {
            createAndAddPiece(COLOR_BLACK, BOARD_START_X + TILE_OFFSET_X * i,
                    BOARD_START_Y + TILE_OFFSET_Y + 180);
        }
        
        PieceDragAndDropListener listener = new PieceDragAndDropListener(this.pieces, this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        
        JFrame f = new JFrame();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setResizable(false);
        f.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null));
    }
    
    private void createAndAddPiece(int color, int x, int y) {
        Image img = this.getImageForPiece(color);
        Piece piece = new Piece(img, x, y);
        this.pieces.add(piece);
    }
    
    private Image getImageForPiece(int color) {
        String fileName = "";
        fileName += (color == COLOR_WHITE ? "w" : "b");
        fileName += ".png";
        
        URL urlPieceImg = getClass().getResource("/img/" + fileName);
        
        return new ImageIcon(urlPieceImg).getImage();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(this.imgBackground, 0, 0, null);
        for(Piece piece : this.pieces) {
            g.drawImage(piece.getImg(), piece.getX(), piece.getY(), null);
        }
    }
    
    public static void main(String[] args) {
        new Gui();
    }
}
