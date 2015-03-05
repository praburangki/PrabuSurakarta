/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch06.logic;

import java.util.Arrays;

/**
 *
 * @author Prabu Rangki
 */
public class Map {

    public static final int MAXSIZE = 6;
    public static final int NOSTONE = 6;
    public static final int WHITE = 6;
    public static final int BLACK = 6;
    public static final int SELECT = 6;

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
        return (x >= 0) && (x < MAXSIZE) && (y >= 0) && (y < MAXSIZE);
    }
}
