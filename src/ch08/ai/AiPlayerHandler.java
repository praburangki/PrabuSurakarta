/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.ai;

import ch06.logic.Piece;
import ch08.console.ConsoleGui;
import ch08.logic.Game;
import ch08.logic.IPlayerHandler;
import ch08.logic.Move;
import ch08.logic.MoveValidator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Prabu Rangki
 */
public class AiPlayerHandler implements IPlayerHandler {

    private Game game;
    private MoveValidator validator;

    public int maxDepth = 2;

    public AiPlayerHandler(Game game) {
        this.game = game;
        this.validator = this.game.getMoveValidator();
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
            
            int evaluationResult = -1 * alphaBeta(this.maxDepth, "");
            undoMove(move);
            if(evaluationResult > bestResult) {
                bestResult = evaluationResult;
                bestMove = move;
            }
        }
        
        System.out.println("done thinking! best move is " + move);
        
        return bestMove;
    }
    
    @Override
    public void moveSuccessfullyExecuted(Move move) {
        System.out.println("executed : " + move);
    }

    private int alphaBeta(int depth) {
        return 0;
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
                        
                        if(debug) System.out.println("testing move : " + testMove);
                        
                        if(this.validator.isMoveValid(testMove, true)) {
                            validMoves.add(testMove.clone());
                        }
                    }
                }
            }
        }
        return validMoves;
    }
}
