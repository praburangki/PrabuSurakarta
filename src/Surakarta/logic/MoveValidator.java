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
        Vector v = new Vector(9);
        
        getDirection(move.sourceRow, move.sourceColumn, v);
        getCaptures(move.sourceRow, move.sourceColumn, v);
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
        Vector v = new Vector(12);
        getCaptures(sourceRow, sourceColumn, v);
        getDirection(sourceRow, sourceColumn, v);
        v.trimToSize();
        
        return v;
    }
    
    public void getCaptures(int x, int y, Vector v) {
        
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
                        (move.targetRow < 0) || (move.targetRow > 5) || (!game.isEmpty(move.targetRow, move.targetColumn)))
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
