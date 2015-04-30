package Applet;

import java.awt.Point;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

public class Piece {
    private Random random;
    private boolean[][] blacks;
    private boolean[][] whites;
    private int maxDepth = Game.difficulty;

    public Piece() {
        this.random = new Random();

        this.blacks = new boolean[6][6];
        this.whites = new boolean[6][6];
        int i = 0;
        do {
            int j = 0;
            do {
                this.blacks[i][j] = true;
                this.whites[(i + 4)][j] = true;
                j++;
            } while (j < 6);
            i++;
        } while (i < 2);
    }

    public Piece(boolean[][] black, boolean[][] white) {
        this.random = new Random();

        this.blacks = new boolean[6][6];
        this.whites = new boolean[6][6];
        int i = 0;
        do {
            int j = 0;
            do {
                this.blacks[i][j] = black[i][j];
                this.whites[i][j] = white[i][j];
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);
    }

    public synchronized Object clone() {
        return new Piece(this.blacks, this.whites);
    }

    public boolean attemptMove(Point p1, Point p2, boolean player) {
        boolean bool;

        if (!player) bool = isBlack(p2);
        else bool = isWhite(p2);

        Move move = new Move(p1, p2, player, bool);
        if (isLegal(move)) {
            doMove(new Move(p1, p2, player, bool));
            return true;
        }
        return false;
    }

    public boolean isEmpty(int x, int y) {
        boolean e = this.blacks[x][y];
        if (!e) {
            e = this.whites[x][y];
        }
        return e ^ true;
    }

    public boolean isEmpty(Point p) {
        boolean black = this.blacks[p.y][p.x];
        boolean white = this.whites[p.y][p.x];
        boolean temp = black;
        if (!temp) {
            temp = white;
        }
        return temp ^ true;
    }

    public boolean isBlack(int x, int y) {
        return this.blacks[x][y];
    }

    public boolean isBlack(Point p) {
        return this.blacks[p.y][p.x];
    }

    public boolean isWhite(int x, int y) {
        return this.whites[x][y];
    }

    public boolean isWhite(Point p) {
        return this.whites[p.y][p.x];
    }

    public Piece remove(Point p) {
        this.whites[p.y][p.x] = false;
        this.blacks[p.y][p.x] = false;
        return this;
    }

    public void getCaptures(int x, int y, Vector v) {
        boolean white = isWhite(x, y);

        this.whites[x][y] = false;
        this.blacks[x][y] = false;

        int idx = Arc.getOuterIdx(x, y);
        int i;
        Point sourcePoint;
        Point targetPoint;
        if (idx >= 0) {
            do {
                i = idx;
                do {
                    i++;
                } while ((isEmpty(Arc.getOuterCoord(i))) && (i < idx + 24));
                
                sourcePoint = Arc.getOuterCoord(i);
                if (((white) && (isBlack(sourcePoint.y, sourcePoint.x))) || ((!white) && (isWhite(sourcePoint.y, sourcePoint.x)))) {
                    if (Arc.travelsCorner(idx, i)) {
                        targetPoint = Arc.getOuterCoord(idx);
                        v.addElement(new Move(targetPoint, sourcePoint, white, true));
                    }
                }

                i = idx;
                do {
                    i--;
                } while ((isEmpty(Arc.getOuterCoord(i))) && (i > idx - 24));

                sourcePoint = Arc.getOuterCoord(i);
                if (((white) && (isBlack(sourcePoint.y, sourcePoint.x))) || ((!white) && (isWhite(sourcePoint.y, sourcePoint.x)))) {
                    if (Arc.travelsCorner(idx, i)) {
                        targetPoint = Arc.getOuterCoord(idx);
                        v.addElement(new Move(targetPoint, sourcePoint, white, true));
                    }
                }

                switch (idx) {
                    case 2:
                        idx = 21;
                        break;
                    case 3:
                        idx = 8;
                        break;
                    case 9:
                        idx = 14;
                        break;
                    case 15:
                        idx = 20;
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    default:
                        idx = -1;
                }
            } while (idx >= 0);
        }

        idx = Arc.getInnerIdx(x, y);

        if (idx >= 0) {
            do {
                i = idx;
                do {
                    i++;
                } while ((isEmpty(Arc.getInnerCoord(i))) && (i < idx + 24));

                sourcePoint = Arc.getInnerCoord(i);
                if (((white) && (isBlack(sourcePoint.y, sourcePoint.x))) || ((!white) && (isWhite(sourcePoint.y, sourcePoint.x)))) {
                    if (Arc.travelsCorner(idx, i)) {
                        targetPoint = Arc.getInnerCoord(idx);
                        v.addElement(new Move(targetPoint, sourcePoint, white, true));
                    }
                }

                i = idx;
                do {
                    i--;
                } while ((isEmpty(Arc.getInnerCoord(i))) && (i > idx - 24));

                sourcePoint = Arc.getInnerCoord(i);
                if (((white) && (isBlack(sourcePoint.y, sourcePoint.x))) || ((!white) && (isWhite(sourcePoint.y, sourcePoint.x)))) {
                    if (Arc.travelsCorner(idx, i)) {
                        targetPoint = Arc.getInnerCoord(idx);
                        v.addElement(new Move(targetPoint, sourcePoint, white, true));
                    }
                }

                switch (idx) {
                    case 1:
                        idx = 22;
                        break;
                    case 4:
                        idx = 7;
                        break;
                    case 10:
                        idx = 13;
                        break;
                    case 16:
                        idx = 19;
                        break;
                    case 2:
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    default:
                        idx = -1;
                }
            } while (idx >= 0);
        }

        if (white) {
            this.whites[x][y] = true;
        } else {
            this.blacks[x][y] = true;
        }
    }

    public Vector pathOfCapture(Move move) {
        int row = move.startRow;
        int col = move.startCol;
        Vector v = new Vector(12, 11);
        boolean white = isWhite(row, col);

        this.whites[row][col] = false;
        this.blacks[row][col] = false;

        int idx = Arc.getOuterIdx(row, col);
        int i;
        Point p;
        if (idx >= 0) {
            do {
                i = idx;
                v.addElement(Arc.getOuterCoord(i));
                do {
                    i++;
                    v.addElement(Arc.getOuterCoord(i));
                } while ((isEmpty(Arc.getOuterCoord(i))) && (i < idx + 24));

                p = Arc.getOuterCoord(i);
                if ((p.x == move.endCol) && (p.y == move.endRow) && (Arc.travelsCorner(idx, i))) {
                    if (white) {
                        this.whites[row][col] = true;
                    } else {
                        this.blacks[row][col] = true;
                    }
                    return v;
                }

                v = new Vector(12, 11);

                i = idx;
                v.addElement(Arc.getOuterCoord(i));
                do {
                    i--;
                    v.addElement(Arc.getOuterCoord(i));
                } while ((isEmpty(Arc.getOuterCoord(i))) && i > (idx - 24));

                p = Arc.getOuterCoord(i);
                if ((p.x == move.endCol) && (p.y == move.endRow) && (Arc.travelsCorner(idx, i))) {

                    if (white) {
                        this.whites[row][col] = true;
                    } else {
                        this.blacks[row][col] = true;
                    }

                    return v;
                }

                v = new Vector(12, 11);

                switch (idx) {
                    case 2:
                        idx = 21;
                        break;
                    case 3:
                        idx = 8;
                        break;
                    case 9:
                        idx = 14;
                        break;
                    case 15:
                        idx = 20;
                        break;
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    default:
                        idx = -1;
                }
            } while (idx >= 0);
        }

        idx = Arc.getInnerIdx(row, col);

        if (idx >= 0) {
            do {
                i = idx;
                v.addElement(Arc.getInnerCoord(i));
                do {
                    i++;
                    v.addElement(Arc.getInnerCoord(i));
                } while ((isEmpty(Arc.getInnerCoord(i))) && (i < idx + 24));

                p = Arc.getInnerCoord(i);
                if ((p.x == move.endCol) && (p.y == move.endRow) && (Arc.travelsCorner(idx, i))) {
                    if (white) {
                        this.whites[row][col] = true;
                    } else {
                        this.blacks[row][col] = true;
                    }

                    return v;
                }

                v = new Vector(12, 11);

                i = idx;

                v.addElement(Arc.getInnerCoord(i));
                do {
                    i--;
                    v.addElement(Arc.getInnerCoord(i));
                } while (isEmpty(Arc.getInnerCoord(i)) && i > idx - 24);

                p = Arc.getInnerCoord(i);
                if ((p.x == move.endCol) && (p.y == move.endRow) && (Arc.travelsCorner(idx, i))) {
                    if (white) {
                        this.whites[row][col] = true;
                    } else {
                        this.blacks[row][col] = true;
                    }
                    return v;
                }

                v = new Vector(12, 11);

                switch (idx) {
                    case 1:
                        idx = 22;
                        break;
                    case 4:
                        idx = 7;
                        break;
                    case 10:
                        idx = 13;
                        break;
                    case 16:
                        idx = 19;
                        break;
                    case 2:
                    case 3:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    default:
                        idx = -1;
                }
            } while (idx >= 0);
        }
        if (white) {
            this.whites[row][col] = true;
        } else {
            this.blacks[row][col] = true;
        }

        return new Vector();
    }

    public void getSlides(int x, int y, Vector v) {
        Vector dir = new Vector(9);
        boolean white = isWhite(x, y);

        int i = x - 1;
        if (i < x + 2) {
            do {
                int j = y - 1;
                if (j < y + 2) {
                    do {
                        dir.addElement(new Move(x, y, i, j, white, false));
                        j++;
                    } while (j < y + 2);
                }
                i++;
            } while (i < x + 2);
        }

        Vector dir2 = (Vector) dir.clone();
        Enumeration e = dir2.elements();

        if (e.hasMoreElements()) {
            do {
                Move move = (Move) e.nextElement();
                if ((move.endCol < 0) || (move.endCol > 5) || (move.endRow < 0) || (move.endRow > 5) || (!isEmpty(move.endRow, move.endCol)))
                    dir.removeElement(move);
            } while (e.hasMoreElements());
        }

        dir2 = (Vector) dir.clone();
        e = dir2.elements();

        if (e.hasMoreElements()) {
            do {
                v.addElement(e.nextElement());
            } while (e.hasMoreElements());
        }
    }

    public void doMove(Move move) {
        if (move.isWhite) {
            this.whites[move.endRow][move.endCol] = true;
            this.blacks[move.endRow][move.endCol] = false;
        } else {
            this.blacks[move.endRow][move.endCol] = true;
            this.whites[move.endRow][move.endCol] = false;
        }
        this.whites[move.startRow][move.startCol] = false;
        this.blacks[move.startRow][move.startCol] = false;
    }

    private void undoMove(Move move) {
        this.whites[move.startRow][move.startCol] = this.whites[move.endRow][move.endCol];
        this.blacks[move.startRow][move.startCol] = this.blacks[move.endRow][move.endCol];
        if (move.isCapture) {
            if (move.isWhite) {
                this.blacks[move.endRow][move.endCol] = true;
                this.whites[move.endRow][move.endCol] = false;
            } else {
                this.blacks[move.endRow][move.endCol] = false;
                this.whites[move.endRow][move.endCol] = true;
            }
        } else {
            this.whites[move.endRow][move.endCol] = false;
            this.blacks[move.endRow][move.endCol] = false;
        }
    }

    public boolean isLegal(Move move) {
        Vector v = new Vector(12);

        getSlides(move.startRow, move.startCol, v);
        getCaptures(move.startRow, move.startCol, v);
        v.trimToSize();

        Enumeration e = v.elements();
        if (e.hasMoreElements()) {
            do {
                if (Move.equals(move, (Move) e.nextElement())) {
                    return true;
                }
            } while (e.hasMoreElements());
        }

        return false;
    }

    public Vector getLegalMoves(Point p) {
        Vector v = new Vector(12);
        getCaptures(p.y, p.x, v);
        getSlides(p.y, p.x, v);
        v.trimToSize();
        
        return v;
    }

    private Vector getAllBlackMoves() {
        Vector v = new Vector(40);

        int i = 0;
        int j;
        do {
            j = 0;
            do {
                if (isBlack(i, j)) {
                    getCaptures(i, j, v);
                }
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);

        i = 0;
        do {
            j = 0;
            do {
                if (isBlack(i, j)) {
                    getSlides(i, j, v);
                }
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);

        v.trimToSize();
        return v;
    }

    private Vector getAllWhiteMoves() {
        Vector v = new Vector(40);

        int i = 0;
        int j;
        do {
            j = 0;
            do {
                if (isWhite(i, j)) {
                    getCaptures(i, j, v);
                }
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);

        i = 0;
        do {
            j = 0;
            do {
                if (isWhite(i, j)) {
                    getSlides(i, j, v);
                }
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);

        v.trimToSize();
        return v;
    }

    public Move randomBlackMove() throws NoMoreMovesException {
        Vector v = new Vector(12);

        int i = 0;
        int j;
        do {
            j = 0;
            do {
                if (isBlack(i, j)) {
                    getCaptures(i, j, v);
                    v.trimToSize();
                    if (v.size() > 0)
                        return (Move) v.elementAt(Math.abs(this.random.nextInt()) % v.size());
                }
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);

        i = Math.abs(this.random.nextInt()) % 6;
        if (i < 12) {
            do {
                j = Math.abs(this.random.nextInt()) % 6;
                if (j < 12) {
                    do {
                        if (isBlack(i % 6, j % 6)) {
                            getSlides(i % 6, j % 6, v);
                            v.trimToSize();
                            if (v.size() > 0)
                                return (Move) v.elementAt(Math.abs(this.random.nextInt()) % v.size());
                        }
                        j++;
                    } while (j < 12);
                }
                i++;
            } while (i < 12);
        }

        throw new NoMoreMovesException();
    }

    public Move bestBlackMove() throws NoMoreMovesException {
        Object obj = null;
        int best = Integer.MAX_VALUE;
        Vector v = getAllBlackMoves();
        v.trimToSize();

        this.maxDepth = Game.difficulty;

        Enumeration e = v.elements();
        if (e.hasMoreElements()) {
            do {
                Move move = (Move) e.nextElement();
                doMove(move);
                int j = alphaBeta(move, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
                undoMove(move);
                if (j < best) {
                    best = j;
                    obj = move;
                }
            } while (e.hasMoreElements());
        }

        return (Move) obj;
    }

    private int evaluate() {
        int i = 0;
        int j = 0;
        do {
            int k = 0;
            do {
                if (isBlack(j, k)) {
                    i -= 10;
                }
                if (isWhite(j, k)) {
                    i += 10;
                }
                k++;
            } while (k < 6);
            j++;
        } while (j < 6);
        
        if (isWhite(1, 1)) i += 5;
        else if (isBlack(1, 1)) i -= 5;
        
        if (isWhite(2, 2)) i += 5;
        else if (isBlack(2, 2)) i -= 5;
        
        if (isWhite(3, 3)) i += 5;
        else if (isBlack(3, 3)) i -= 5;
        
        if (isWhite(4, 4)) i += 5;
        else if (isBlack(4, 4)) i -= 5;
        
        if (isWhite(1, 4)) i += 5;
        else if (isBlack(1, 4)) i -= 5;
        
        if (isWhite(2, 3)) i += 5;
        else if (isBlack(2, 3)) i -= 5;
            
        if (isWhite(3, 2)) i += 5;
        else if (isBlack(3, 2)) i -= 5;
        
        if (isWhite(4, 1)) i += 5;
        else if (isBlack(4, 1)) i -= 5;
        
        if (isBlack(0, 0)) i += 20;
        else if (isWhite(0, 0)) i -= 11;
        
        if (isBlack(0, 5)) i += 20;
        else if (isWhite(0, 5)) i -= 11;
        
        if (isBlack(5, 0)) i += 20;
        else if (isWhite(5, 0)) i -= 11;
        
        if (isBlack(5, 5)) i += 20;
        else if (isWhite(5, 5)) i -= 11;

        return i;
    }
    
    private int alphaBeta(Move move, int d, int beta, int alpha) {
        Object obj = null;
        Vector v = getAllWhiteMoves();
        v.trimToSize();
        Enumeration e = v.elements();
        
        int b = beta;
        int a = alpha;
        
        if(d == this.maxDepth + 1)
            return evaluate();
            
        Move m;
        if ((d & 0x1) == 0) {
            v = getAllBlackMoves();
            v.trimToSize();
            e = v.elements();
            if (e.hasMoreElements()) {
                do {                    
                    m = (Move) e.nextElement();
                    a = Math.min(a, alphaBeta(m, d + 1, b, a));
                    
                    if (b >= a) return b;
                } while (e.hasMoreElements());
            }
            return a;
        }
        
        if (e.hasMoreElements()) {
            do {                
                m = (Move) e.nextElement();
                b = Math.max(b, alphaBeta(m, d + 1, b, a));
                
                if (b >= a) return a;
            } while (e.hasMoreElements());
        }
        
        return b;
    }
}
