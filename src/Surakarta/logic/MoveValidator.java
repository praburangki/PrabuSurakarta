package Surakarta.logic;

import java.awt.Point;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author PrabuRangki
 */
public class MoveValidator {
    private Game game;
    private Piece sourcePiece, targetPiece;

    public MoveValidator(Game game) {
        this.game = game;
    }
    
    public boolean isMoveValid(Move move) {
        int sourceRow = move.sourceRow;
        int sourceColumn = move.sourceColumn;
        int targetRow = move.targetRow;
        int targetColumn = move.targetColumn;

        sourcePiece = game.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
        targetPiece = game.getNonCapturedPieceAtLocation(targetRow, targetColumn);
        
        // source piece does not exist
        if (sourcePiece == null) {
            System.out.println("no source piece");
            return false;
        }

        // source piece has right color?
        if (sourcePiece.getColor() == Piece.COLOR_WHITE
                && this.game.getGameState() == Game.GAME_STATE_WHITE) {
            // ok
        } else if (sourcePiece.getColor() == Piece.COLOR_BLACK
                && this.game.getGameState() == Game.GAME_STATE_BLACK) {
            // ok
        } else {
            System.out.println("it's not your turn");
            return false;
        }

        // check if target location within boundaries
        if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_6
                || targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_F) {
            System.out.println("target row or column out of scope");
            return false;
        }
        
        Vector v = new Vector();
        
        getCaptures(move.sourceRow, move.sourceColumn, v);
        getDirection(move.sourceRow, move.sourceColumn, v);
        v.trimToSize();
        
        Enumeration e = v.elements();
        if(e.hasMoreElements()) {
            do {                
                if(move.equals((Move) e.nextElement())) return true;
            } while (e.hasMoreElements());
        }
        
        return false;
    }
    
    public Vector getLegalMoves(int sourceRow, int sourceColumn) {
        Vector v = new Vector();
        getCaptures(sourceRow, sourceColumn, v);
        getDirection(sourceRow, sourceColumn, v);
        v.trimToSize();
        
        return v;
    }
    
    public void getCaptures(int x, int y, Vector v) {
        boolean white = game.isWhite(x, y);

        game.whites[x][y] = false;
        game.blacks[x][y] = false;

        int idx = Arc.getOuterIdx(x, y);
        int i;
        Point sourcePoint;
        Point targetPoint;
        if (idx >= 0) {
            do {
                i = idx;
                do {
                    i++;
                } while ((game.isEmpty(Arc.getOuterCoord(i))) && (i < idx + 24));

                sourcePoint = Arc.getOuterCoord(i);
                if (((white) && (game.isBlack(sourcePoint.y, sourcePoint.x))) || 
                        ((!white) && (game.isWhite(sourcePoint.y, sourcePoint.x)))) {
                    if (Arc.travelsCorner(idx, i)) {
                        targetPoint = Arc.getOuterCoord(idx);
                        v.addElement(new Move(targetPoint, sourcePoint, white, true));
                    }
                }

                i = idx;
                do {
                    i--;
                } while ((game.isEmpty(Arc.getOuterCoord(i))) && (i > idx - 24));

                sourcePoint = Arc.getOuterCoord(i);
                if (((white) && (game.isBlack(sourcePoint.y, sourcePoint.x))) || 
                        ((!white) && (game.isWhite(sourcePoint.y, sourcePoint.x)))) {
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
                } while ((game.isEmpty(Arc.getInnerCoord(i))) && (i < idx + 24));

                sourcePoint = Arc.getInnerCoord(i);
                if (((white) && (game.isBlack(sourcePoint.y, sourcePoint.x))) || 
                        ((!white) && (game.isWhite(sourcePoint.y, sourcePoint.x)))) {
                    if (Arc.travelsCorner(idx, i)) {
                        targetPoint = Arc.getInnerCoord(idx);
                        v.addElement(new Move(targetPoint, sourcePoint, white, true));
                    }
                }

                i = idx;
                do {
                    i--;
                } while ((game.isEmpty(Arc.getInnerCoord(i))) && (i > idx - 24));

                sourcePoint = Arc.getInnerCoord(i);
                if (((white) && (game.isBlack(sourcePoint.y, sourcePoint.x))) || 
                        ((!white) && (game.isWhite(sourcePoint.y, sourcePoint.x)))) {
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
            game.whites[x][y] = true;
        } else {
            game.blacks[x][y] = true;
        }
    }
    
    public void getDirection(int x, int y, Vector v) {
        Vector dir = new Vector(9);
        boolean white = game.isWhite(x, y);

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
                if ((move.targetColumn < 0) || (move.targetColumn > 5) || 
                        (move.targetRow < 0) || (move.targetRow > 5) || 
                        (!game.isEmpty(move.targetRow, move.targetColumn)))
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
}
