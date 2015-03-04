/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch02;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author praburangki
 */
public class Gui extends JPanel {
    
    private static final int BOARD_START_X = 377;
    private static final int BOARD_START_Y = 51;
    
    private static final int TILE_OFFSET_X = 45;
    private static final int TILE_OFFSET_Y = 75;
    
    private Image imgBackground;
    
    private int gameState = GAME_STATE_WHITE;
    static final int GAME_STATE_WHITE = 0, GAME_STATE_BLACK = 1;
    
    private List<Piece> pieces = new ArrayList<Piece>();
    private JLabel labelGameState;

    public Gui() {
        setLayout(null);
        
        URL urlBackgroundImg = getClass().getResource("/img/surakarta.png");
        this.imgBackground = new ImageIcon(urlBackgroundImg).getImage();
        
        for (int i = 0; i < 6; i++) {
            createAndAddPiece(Piece.COLOR_WHITE, BOARD_START_X + TILE_OFFSET_X * i,
                    BOARD_START_Y + TILE_OFFSET_Y);
        }

        for (int i = 0; i < 6; i++) {
            createAndAddPiece(Piece.COLOR_WHITE, BOARD_START_X + TILE_OFFSET_X * i,
                    BOARD_START_Y + TILE_OFFSET_Y + 45);
        }

        for (int i = 0; i < 6; i++) {
            createAndAddPiece(Piece.COLOR_BLACK, BOARD_START_X + TILE_OFFSET_X * i,
                    BOARD_START_Y + TILE_OFFSET_Y + 225);
        }

        for (int i = 0; i < 6; i++) {
            createAndAddPiece(Piece.COLOR_BLACK, BOARD_START_X + TILE_OFFSET_X * i,
                    BOARD_START_Y + TILE_OFFSET_Y + 180);
        }
        
        PieceDragAndDropListener listener = new PieceDragAndDropListener(this.pieces, this);
        this.addMouseListener(listener);
        this.addMouseMotionListener(listener);
        
        JButton buttonChangeGameState = new JButton("Change");
        buttonChangeGameState.addActionListener(new ChangeGameStateButtonActionListener(this));
        buttonChangeGameState.setBounds(0, 0, 80, 30);
        this.add(buttonChangeGameState);
        
        String labelText = this.getGameStateAsText();
        this.labelGameState = new JLabel(labelText);
        labelGameState.setBounds(0, 30, 80, 30);
        labelGameState.setForeground(Color.WHITE);
        this.add(labelGameState);
        
        JFrame f = new JFrame();
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.setResizable(false);
        f.setSize(this.imgBackground.getWidth(null), this.imgBackground.getHeight(null));
    }
    
    private String getGameStateAsText() {
        return this.gameState == GAME_STATE_BLACK ? "black" : "white";
    }
    
    private void createAndAddPiece(int color, int x, int y) {
        Image img = this.getImageForPiece(color);
        Piece piece = new Piece(img, x, y, color);
        this.pieces.add(piece);
    }
    
    private Image getImageForPiece(int color) {
        String fileName = "";
        fileName += (color == Piece.COLOR_WHITE ? "w" : "b");
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
    
    public void changeGameState() {
        switch(this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            default:
                throw new IllegalStateException("unknown game state : " + this.gameState);
        }
        this.labelGameState.setText(this.getGameStateAsText());
    }
    
    public int getGameState() {
        return this.gameState;
    }
}
