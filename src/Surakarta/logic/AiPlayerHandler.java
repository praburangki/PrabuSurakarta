package Surakarta.logic;

import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author praburangki
 */
public class AiPlayerHandler implements IPlayerHandler {
    private Game game;
    private MoveValidator moveValidator;
    private Evaluate evaluate;
    
    public int maxDepth;

    public AiPlayerHandler(Game game, boolean enemy) {
        this.game = game;
        this.moveValidator = this.game.getMoveValidator();
        if(enemy) evaluate = new Evaluate(game);
        else evaluate = new EvaluateEnemy(game);
    }
    
    @Override
    public void moveSuccessfullyExecuted(Move move) {
        System.out.println("executed : " + move);
    }
    
    @Override
    public Move getMove() {
        return getBestMove();
    }
    
    private Move getBestMove() {
        System.out.println("Getting best move");
        System.out.println("thingking...");
        Move bestMove = null;
        
        int bestValue = Integer.MIN_VALUE;
        
        Vector v = null;
        int color = -1;
        if (game.getGameState() == Game.GAME_STATE_BLACK) {
            v = generateMoves(Piece.COLOR_BLACK);
            color = Piece.COLOR_BLACK;
        } else if (game.getGameState() == Game.GAME_STATE_WHITE) {
            v = generateMoves(Piece.COLOR_WHITE);
            color = Piece.COLOR_WHITE;
        }
        v.trimToSize();
        
        Enumeration e = v.elements();
        if (e.hasMoreElements()) {
            do {                
                Move move = (Move) e.nextElement();
                doMove(move);
                int abValue = alphaBeta(1, Integer.MIN_VALUE, Integer.MAX_VALUE, color ^ 3);
                undoMove(move);
                if(abValue > bestValue) {
                    bestValue = abValue;
                    bestMove = move;
                    bestMove.score = bestValue;
                }
            } while (e.hasMoreElements());
        }
        
        System.out.println("bestMove : " + bestMove + " -> " + bestMove.score);
        return bestMove;
    }
    
    private int alphaBeta(int depth, int alpha, int beta, int color) {
        if(depth == this.maxDepth 
                || this.game.getGameState() == Game.GAME_STATE_END_WHITE_WON
                || this.game.getGameState() == Game.GAME_STATE_END_BLACK_WON) 
            return evaluate.evaluate(game, color);
        
        Vector v = generateMoves(color);
        v.trimToSize();
        Enumeration e = v.elements();
        if (e.hasMoreElements()) {
            do {                
                Move move = (Move) e.nextElement();
                doMove(move);
                int value = -1 * alphaBeta(depth + 1, -1 * beta, -1 * alpha, color ^ 3);
                undoMove(move);
                if (value > alpha) alpha = value;
                if (value >= beta) break;
            } while (e.hasMoreElements());
        }
        
        return alpha;
    }
    
    private void doMove(Move move) {
        this.game.movePiece(move);
        this.game.changeGameState();
    }
    
    private void undoMove(Move move) {
        this.game.undoMove(move);
    }
    
    private Vector generateMoves(int color) {
        Vector v = new Vector(40);
        
        int i = 0;
        int j;
        do {            
            j = 0;
            do {                
                if(color == Piece.COLOR_WHITE) {
                    if(game.isWhite(i, j))
                        game.getMoveValidator().getCaptures(i, j, v);
                } else if(color == Piece.COLOR_BLACK) {
                    if(game.isBlack(i, j))
                        game.getMoveValidator().getCaptures(i, j, v);
                }
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);
        
        i = 0;
        do {            
            j = 0;
            do {                
                if(color == Piece.COLOR_WHITE) {
                    if(game.isWhite(i, j))
                        game.getMoveValidator().getDirection(i, j, v);
                } else if(color == Piece.COLOR_BLACK) {
                    if(game.isBlack(i, j))
                        game.getMoveValidator().getDirection(i, j, v);
                }
                j++;
            } while (j < 6);
            i++;
        } while (i < 6);
        
        return v;
    }
}
