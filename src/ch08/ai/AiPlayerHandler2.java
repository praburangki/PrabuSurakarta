/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.ai;

import ch08.logic.Piece;
import ch08.console.ConsoleGui;
import ch08.logic.Game;
import ch08.logic.IPlayerHandler;
import ch08.logic.Map;
import ch08.logic.Move;
import ch08.logic.MoveValidator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Prabu Rangki
 */
public class AiPlayerHandler2 implements IPlayerHandler {

    private Game game;
    private MoveValidator validator;
    private Map map;
    private Evaluate3 evaluate;

    public int maxDepth;

    public AiPlayerHandler2(Game game) {
        this.game = game;
        this.map = game.getMap();
        this.validator = this.game.getMoveValidator();
        evaluate = new Evaluate3(game);
    }

    @Override
    public Move getMove() {
        return getBestMove();
    }

    /**
     * get best move for current game situation
     *
     * @return a valid Move instance
     */
    private Move getBestMove() {
        System.out.println("Getting best move");
        ConsoleGui.printCurrentGameState(game);
        System.out.println("thingking...");
        
        List<Move> validMoves = generateMoves(false);
        int bestResult = Integer.MIN_VALUE;
        Move bestMove = null;
        
        for(Move move : validMoves) {
            doMove(move);
            
            int evaluationResult = -1 * negaScout(this.maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
            undoMove(move);
            if(evaluationResult > bestResult) {
                bestResult = evaluationResult;
                bestMove = move;
            }
        }
        
        System.out.println("done thinking! best move is " + bestMove);
        
        return bestMove;
    }
    
    @Override
    public void moveSuccessfullyExecuted(Move move) {
        System.out.println("executed : " + move);
    }

    private int negaScout(int depth, int alpha, int beta, int color) {
        if(depth <= 0
                || this.game.getGameState() == Game.GAME_STATE_END_WHITE_WON
                || this.game.getGameState() == Game.GAME_STATE_END_BLACK_WON) {
            return evaluate.evaluate(map.map, color, depth);
        }
        int score = Integer.MIN_VALUE; // return value
        int n = beta;
        List<Move> moves = generateMoves(false);
        for (Move move : moves) {
            doMove(move);
            int cur = -1 * negaScout(depth - 1, -n, -alpha, color ^ 3);
            if(cur > score) {
                if(n == beta || depth <= 2) {
                    score = cur;
                }
                else {
                    score = -negaScout(depth - 1, -beta, -cur, color ^ 3);
                }
            }
            if (score > alpha) alpha = score;
            undoMove(move);
            if(alpha >= beta) return alpha;
            n = alpha + 1;
        }
        return score;
    }
    
    private void undoMove(Move move) {
        this.game.undoMove(move);
    }
    
    private void doMove(Move move) {
        this.game.movePiece(move);
        this.game.changeGameState();
    }
    
    private List<Move> generateMoves(boolean debug) {
        List<Piece> pieces = this.game.getPieces();
        List<Move> validMoves = new ArrayList<>();
        Move testMove = new Move(0, 0, 0, 0);
        
        int pieceColor = this.game.getGameState() == Game.GAME_STATE_WHITE ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        
        for(Piece piece : pieces) {
            if(pieceColor == piece.getColor()) {
                testMove.sourceRow = piece.getRow();
                testMove.sourceColumn = piece.getColumn();
                
                for(int targetRow = Piece.ROW_1; targetRow <= Piece.ROW_6; targetRow++) {
                    for(int targetColumn = Piece.COLUMN_A; targetColumn <= Piece.COLUMN_F; targetColumn++) {
                        
                        testMove.targetRow = targetRow;
                        testMove.targetColumn = targetColumn;
                        
                        if(this.validator.isMoveValid(testMove)) {
                            validMoves.add(testMove.copy());
                        }
                    }
                }
            }
        }
        return validMoves;
    }
}
