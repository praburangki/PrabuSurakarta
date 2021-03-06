package Surakarta.logic;

import java.awt.Point;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author praburangki
 */
public class Evaluate {

    Game game;
    int blackNum, whiteNum, blackMove, whiteMove, blackPos, whitePos;
    Point[] blackPoint, whitePoint;
    Vector<Point> blackVector, whiteVector;
    int value;
    int depth;
    Vector bestMove;

    public Evaluate(Game game) {
        this.game = game;
    }
    
    public Evaluate(Game game, int depth, Vector bestMove, int color) {
        this.value = evaluate(game, color);
        this.depth = depth;
        this.bestMove = bestMove;
    }
    

    private void pieceNum() {
        blackNum = 0;
        whiteNum = 0;
        blackVector = new Vector<>(12);
        whiteVector = new Vector<>(12);
        int i = 0;
        int j;
        do {
            j = 0;
            do {
                if (game.isBlack(i, j)) {
                    blackNum++;
                    blackVector.add(new Point(i, j));
                } else if (game.isWhite(i, j)) {
                    whiteNum++;
                    whiteVector.add(new Point(i, j));
                }
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);
        blackVector.trimToSize();
        whiteVector.trimToSize();
    }

    private final int[][] direction = {
        {0, 1}, {1, 0}, {0, -1}, {-1, 0},
        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    private void moveRange() {
        Point p1;
        Point p2 = new Point();
        Point targetPoint = new Point();
        blackMove = 0;
        whiteMove = 0;
        Enumeration e = blackVector.elements();
        if (e.hasMoreElements()) {
            do {
                p1 = (Point) e.nextElement();
                int i = 0;
                do {
                    p2.x = p1.x + direction[i][0];
                    p2.y = p1.y + direction[i][1];
                    if (isInMap(p2.x, p2.y) && game.isEmpty(p2.x, p2.y)) {
                        int j = 0;
                        int enemy = 0;
                        int safe = 0;
                        do {                            
                            targetPoint.x = p2.x + direction[j][0];
                            targetPoint.y = p2.y + direction[j][1];
                            if(isInMap(targetPoint.x, targetPoint.y)) {
                                if (game.isWhite(targetPoint.x, targetPoint.y)) enemy++;
                                else safe++;
                            }
                            j++;
                        } while (j < 8);
                        if(safe >= enemy) blackMove++;
                    }
                    i++;
                } while (i < 8);
            } while (e.hasMoreElements());
        }
//        
//        System.out.println("Black board:");
//        for (int i = 0; i < 6; i++) {
//            System.out.println(Arrays.toString(game.blacks[i]));
//        }
//        System.out.println();
//        System.out.println("blackMove : " + blackMove);

        e = whiteVector.elements();
        if (e.hasMoreElements()) {
            do {
                p1 = (Point) e.nextElement();
                int i = 0;
                do {
                    p2.x = p1.x + direction[i][0];
                    p2.y = p1.y + direction[i][1];
                    if (isInMap(p2.x, p2.y) && game.isEmpty(p2)) {
                        int j = 0;
                        int enemy = 0;
                        int safe = 0;
                        do {                            
                            targetPoint.x = p2.x + direction[j][0];
                            targetPoint.y = p2.y + direction[j][1];
                            if(isInMap(targetPoint.x, targetPoint.y)) {
                                if (game.isBlack(targetPoint.x, targetPoint.y)) enemy++;
                                else safe++;
                            }
                            j++;
                        } while (j < 8);
                        if(safe >= enemy) whiteMove++;
                    }
                    i++;
                } while (i < 8);
            } while (e.hasMoreElements());
        }
        
//        System.out.println("White board:");
//        for (int i = 0; i < 6; i++) {
//            System.out.println(Arrays.toString(game.whites[i]));
//        }
//        System.out.println();
//        System.out.println("whiteMove : " + whiteMove);
    }
    
    //reverse tengah dan pinggir and uji
    private final int[][] positionValue = {
        {5, 20, 20, 20, 20, 5},
        {20, 30, 50, 50, 30, 20},
        {20, 50, 40, 40, 50, 20},
        {20, 50, 40, 40, 50, 20},
        {20, 30, 50, 50, 30, 20},
        {5, 20, 20, 20, 20, 5}
    };
    
    private final int[][] positionValueReverse = {
        {5, 20, 20, 20, 20, 5},
        {20, 40, 30, 30, 40, 20},
        {20, 30, 50, 50, 30, 20},
        {20, 30, 50, 50, 30, 20},
        {20, 40, 30, 30, 40, 20},
        {5, 20, 20, 20, 20, 5}
    };
    
    private void posValue() {
        blackPos = 0;
        whitePos = 0;
        Point p1 = new Point();
        Enumeration e = blackVector.elements();
        if (e.hasMoreElements()) {
            do {
                p1 = (Point) e.nextElement();
                if(game.moveCount % 3 == 0) blackPos += positionValue[p1.x][p1.y];
                else blackPos += positionValueReverse[p1.x][p1.y];
            } while (e.hasMoreElements());
        }

        e = whiteVector.elements();
        if (e.hasMoreElements()) {
            do {
                p1 = (Point) e.nextElement();
                if(game.moveCount % 3 == 0) whitePos += positionValue[p1.x][p1.y];
                else whitePos += positionValueReverse[p1.x][p1.y];
            } while (e.hasMoreElements());
        }
    }

    private boolean isInMap(int x, int y) {
        return x >= 0 && x < 6 && y >= 0 && y < 6;
    }

    public int evaluate(Game game, int color) {
        int blackValue = 0;
        int whiteValue = 0;
//        for (int i = 0; i < 6; i++) {
//            for (int j = 0; j < 6; j++) {
//                if(game.blacks[i][j])
//                    System.out.print("1 ");
//                else
//                    System.out.print("0 ");
//            }
//            System.out.println();
//        }
        pieceNum();
        moveRange();
        posValue();
        
        blackValue += (blackPos) + (blackMove * 100 / 8) + (blackNum * 100);
        whiteValue += (whitePos) + (whiteMove * 100 / 8) + (whiteNum * 100);
//        System.out.println("blackPos: " + blackPos + " blackMove: " + (blackMove * 100 / 8) + 
//                " blackNum: " + (blackNum * 100) + " = " + blackValue);
//        blackValue += (blackMove * 100 / 8);
//        whiteValue += (whiteMove * 100 / 8);
        if (color == Piece.COLOR_BLACK) {
            return whiteValue - blackValue;
        } else {
            return blackValue - whiteValue;
        }
    }
    
    
    
}
