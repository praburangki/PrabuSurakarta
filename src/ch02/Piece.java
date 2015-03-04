/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch02;

import ch01.*;
import java.awt.Image;

/**
 *
 * @author praburangki
 */
public class Piece {
    public static final int COLOR_WHITE = 0, COLOR_BLACK = 1;
    
    private Image img;
    private int x, y, color;

    public Piece(Image img, int x, int y, int color) {
        this.img = img;
        this.x = x;
        this.y = y;
        this.color = color;
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

    public int getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        String strColor = this.color == COLOR_WHITE ? "white" : "black";
        
        return strColor + " " + x + "/" + y;
    }
}
