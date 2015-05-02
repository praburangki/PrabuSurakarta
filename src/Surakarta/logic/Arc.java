package Surakarta.logic;

import java.awt.Point;

public final class Arc {

    static int[][] innerIdx = {
        {-1, 23, -1, -1, 6, -1},
        {0, 1, 2, 3, 4, 5},
        {-1, 21, -1, -1, 8, -1},
        {-1, 20, -1, -1, 9, -1},
        {17, 16, 15, 14, 10, 12},
        {-1, 18, -1, -1, 11, -1}
    };

    static int[][] outerIdx = {
        {-1, -1, 23, 6, -1, -1},
        {-1, -1, 22, 7, -1, -1},
        {0, 1, 2, 3, 4, 5},
        {17, 16, 15, 9, 13, 12},
        {-1, -1, 19, 10, -1, -1},
        {-1, -1, 18, 11, -1, -1}
    };

    static int[][] innerCoord = {
        {1, 0}, {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5},
        {0, 4}, {1, 4}, {2, 4}, {3, 4}, {4, 4}, {5, 4},
        {4, 5}, {4, 4}, {4, 3}, {4, 2}, {4, 1}, {4, 0},
        {5, 1}, {4, 1}, {3, 1}, {2, 1}, {1, 1}, {0, 1}
    };

    static int[][] outerCoord = {
        {2, 0}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5},
        {0, 3}, {1, 3}, {2, 3}, {3, 3}, {4, 3}, {5, 3},
        {3, 5}, {3, 4}, {3, 3}, {3, 2}, {3, 1}, {3, 0},
        {5, 2}, {4, 2}, {3, 2}, {2, 2}, {1, 2}, {0, 2}
    };
    
    public static int getInnerIdx(int x, int y) {
        return innerIdx[x][y];
    }
    
    public static int getOuterIdx(int x, int y) {
        return outerIdx[x][y];
    }
    
    public static Point getInnerCoord(int x) {
        x = (x + innerCoord.length) % innerCoord.length;
        return new Point(innerCoord[x][1], innerCoord[x][0]);
    }
    
    public static Point getOuterCoord(int x) {
        x = (x + outerCoord.length) % outerCoord.length;
        return new Point(outerCoord[x][1], outerCoord[x][0]);
    }
    
    public static boolean travelsCorner(int x, int y) {
        return (x + 24) / 6 != (y + 24) / 6;
    }
}
