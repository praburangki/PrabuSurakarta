package Surakarta.logic;

import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author praburangki
 */
public class AiPlayerHandler implements IPlayerHandler {
    private Game game;
    private Evaluate evaluate;
    private TranspositionTable transpositionTable;
    
    public int maxDepth;

    public AiPlayerHandler(Game game) {
        this.game = game;
        evaluate = new Evaluate(game);
        transpositionTable = new TranspositionTable();
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
        
        Vector v = new Vector();
        int color = Piece.COLOR_BLACK;
        v = generateMoves(color);
        
        v.trimToSize();
        
        Enumeration e = v.elements();
        if (e.hasMoreElements()) {
            do {                
                Move move = (Move) e.nextElement();
                doMove(move);
//                System.out.println("The move: " + move);
                int abValue = alphaBeta(move, 1, Integer.MIN_VALUE, Integer.MAX_VALUE, color ^ 3);
//                System.out.println("The move: " + move + " with value " + abValue);
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
    
    private int alphaBeta(Move move, int depth, int alpha, int beta, int color) {
        if(depth == this.maxDepth 
                || this.game.getGameState() == Game.GAME_STATE_END_WHITE_WON
                || this.game.getGameState() == Game.GAME_STATE_END_BLACK_WON) 
            return evaluate.evaluate(game, color);
        
        if(transpositionTable.containsKey(move)) {
            MoveEvaluation previousEvaluation = transpositionTable.get(move);
            if(previousEvaluation.depth >= depth) {
                move.bestMove = previousEvaluation.bestMove;
                return previousEvaluation.value;
            }
        }
        
        Vector v = generateMoves(color);
        v.trimToSize();
        Enumeration e = v.elements();
        if (e.hasMoreElements()) {
            do {                
                Move moveTemp = (Move) e.nextElement();
                doMove(moveTemp);
                int value = -1 * alphaBeta(moveTemp, depth + 1, -1 * beta, -1 * alpha, color ^ 3);
                undoMove(moveTemp);
                if (value > alpha) {
                    alpha = value;
                    move.bestMove = v;
                }
                if (value >= beta) break;
            } while (e.hasMoreElements());
        }
        
        transpositionTable.put(move, new MoveEvaluation(alpha, depth, move.bestMove));
        
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
