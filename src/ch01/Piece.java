/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch01;

import java.awt.Image;

/**
 *
 * @author praburangki
 */
public class Piece {
    private Image img;
    private int x, y;

    public Piece(Image img, int x, int y) {
        this.img = img;
        this.x = x;
        this.y = y;
    }

    public Image getImg() {
        return img;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return img.getWidth(null);
    }
    
    public int getHeight() {
        return img.getHeight(null);
    }
}
