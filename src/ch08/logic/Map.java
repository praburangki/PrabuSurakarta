/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.logic;

import java.util.Arrays;

/**
 *
 * @author Prabu Rangki
 */
public class Map {

    public static final int MAXSIZE = 6;
    public static final int NOSTONE = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;
    public static final int SELECT = 3;

    private int whiteNum;
    private int blackNum;

    public int[][] map = new int[MAXSIZE][MAXSIZE];

    public void clear() {
        for (int i = 0; i < MAXSIZE; i++) {
            Arrays.fill(map[i], 0);
        }
    }

    public void init() {
        clear();

        whiteNum = blackNum = 12;
    }

    public boolean isInMap(int x, int y) {
        return ((x >= 0) && (x < MAXSIZE)) && ((y >= 0) && (y < MAXSIZE));
    }
    
    public String toString() {
        String s = "";
        for (int i = 0; i < map.length; i++) {
            s += Arrays.toString(map[i]) + "\n";
        }
        
        return s;
    }
}
