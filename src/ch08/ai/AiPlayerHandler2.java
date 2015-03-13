/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.ai;

import ch08.console.ConsoleGui;
import ch08.logic.Game;
import ch08.logic.IPlayerHandler;
import ch08.logic.Move;
import ch08.logic.MoveValidator;
import ch08.logic.Piece;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author praburangki
 */
public class AiPlayerHandler2 implements IPlayerHandler {

    private Game game;
    private MoveValidator validator;

    /**
     * number of moves to look into the future
     */
    public int maxDepth = 2;

    public AiPlayerHandler2(Game game) {
        this.game = game;
        this.validator = this.game.getMoveValidator();
    }
    
    @Override
    public Move getMove() {
        return getBestMove();
    }
    
    private Move getBestMove() {
        System.out.println("getting beest move");
        ConsoleGui.printCurrentGameState(this.game);
        System.out.println("thinking...");
        
        List<Move> validMoves = generateMoves(false);
        int bestResult = Integer.MIN_VALUE;
        Move bestMove = null;
        
        for(Move move: validMoves) {
            doMove(move);
            int evaluationResult = -1 * negaMax(this.maxDepth, "");
            undoMove(move);
            if(evaluationResult > bestResult) {
                bestResult = evaluationResult;
                bestMove = move;
            }
        }
        System.out.println("done thinking! best move is : " + bestMove);
        
        return bestMove;
    }
    
    @Override
    public void moveSuccessfullyExecuted(Move move) {
        System.out.println("executed : " + move);
    }
    
    private int negaMax(int depth, String indent) {
        if(depth <= 0 
                || this.game.getGameState() == Game.GAME_STATE_END_WHITE_WON
                || this.game.getGameState() == Game.GAME_STATE_END_BLACK_WON) {
            return 0;
        }
        
        List<Move> moves = generateMoves(false);
        int currentMax = Integer.MIN_VALUE;
        
        for(Move currentMove : moves) {
            doMove(currentMove);
            int score = -1 * negaMax(depth, indent + " ");
            undoMove(currentMove);
            
            if(score > currentMax) {
                currentMax = score;
            }
        }
        
        return currentMax;
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
                        
                        if(this.validator.isMoveValid(testMove, false)) {
                            validMoves.add(testMove.copy());
                        }
                    }
                }
            }
        }
        
        return validMoves;
    }
}
