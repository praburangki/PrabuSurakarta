/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial.ai;

import artificial.console.Console;
import artificial.logic.Game;
import artificial.logic.IPlayerHandler;
import artificial.logic.Move;
import artificial.logic.MoveValidator;
import artificial.logic.Piece;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author praburangki
 */
public class AiPlayerHandler implements IPlayerHandler {

    private Game game;
    private MoveValidator validator;

    /**
     * number of moves to look into the future
     */
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
        System.out.println("getting best move");
        Console.printCurrentGameState(this.game);
        System.out.println("thinking...");

        List<Move> validMoves = generateMoves(false);
        int bestResult = Integer.MIN_VALUE;
        Move bestMove = null;

        for (Move move : validMoves) {
            executeMove(move);
            //System.out.println("evaluate move: "+move+" =========================================");
            int evaluationResult = -1 * negaMax(this.maxDepth, "");
            //System.out.println("result: "+evaluationResult);
            undoMove(move);
            if (evaluationResult > bestResult) {
                bestResult = evaluationResult;
                bestMove = move;
            }
        }
        System.out.println("done thinking! best move is: " + bestMove);
        return bestMove;
    }

    @Override
    public void moveSuccessfullyExecuted(Move move) {
        // we are using the same game instance, so no need to do anything here.
        System.out.println("executed: " + move);
    }

    /**
     * evaluate current game state according to nega max algorithm
     *
     * @param depth - current depth level (number of counter moves that still
     * need to be evaluated)
     * @param indent - debug string, that is placed in front of each log message
     * @return integer score of game state after looking at "depth" counter
     * moves
     */
    private int negaMax(int depth, String indent) {

        if (depth <= 0
                || this.game.getGameState() == Game.GAME_STATE_END_WHITE_WON
                || this.game.getGameState() == Game.GAME_STATE_END_BLACK_WON) {

            return evaluateState();
        }

        List<Move> moves = generateMoves(false);
        int currentMax = Integer.MIN_VALUE;

        for (Move currentMove : moves) {

            executeMove(currentMove);
            //ChessConsole.printCurrentGameState(this.game);
            int score = -1 * negaMax(depth - 1, indent + " ");
            //System.out.println(indent+"handling move: "+currentMove+" : "+score);
            undoMove(currentMove);

            if (score > currentMax) {
                currentMax = score;
            }
        }
        //System.out.println(indent+"max: "+currentMax);
        return currentMax;
    }

    /**
     * undo specified move
     */
    private void undoMove(Move move) {
        //System.out.println("undoing move");
        this.game.undoMove(move);
        //state.changeGameState();
    }

    /**
     * Execute specified move. This will also change the game state after the
     * move has been executed.
     */
    private void executeMove(Move move) {
        //System.out.println("executing move");
        this.game.movePiece(move);
        this.game.changeGameState();
    }

    /**
     * generate all possible/valid moves for the specified game
     *
     * @param state - game state for which the moves should be generated
     * @return list of all possible/valid moves
     */
    private List<Move> generateMoves(boolean debug) {

        List<Piece> pieces = this.game.getPieces();
        List<Move> validMoves = new ArrayList<Move>();
        Move testMove = new Move(0, 0, 0, 0);

        int pieceColor = (this.game.getGameState() == Game.GAME_STATE_WHITE
                ? Piece.COLOR_WHITE
                : Piece.COLOR_BLACK);

        // iterate over all non-captured pieces
        for (Piece piece : pieces) {

            // only look at pieces of current players color
            if (pieceColor == piece.getColor()) {
                // start generating move
                testMove.sourceRow = piece.getRow();
                testMove.sourceColumn = piece.getColumn();

                // iterate over all board rows and columns
                for (int targetRow = Piece.ROW_1; targetRow <= Piece.ROW_6; targetRow++) {
                    for (int targetColumn = Piece.COLUMN_A; targetColumn <= Piece.COLUMN_F; targetColumn++) {

                        // finish generating move
                        testMove.targetRow = targetRow;
                        testMove.targetColumn = targetColumn;

                        if (debug) {
                            System.out.println("testing move: " + testMove);
                        }

                        // check if generated move is valid
                        if (this.validator.isMoveValid(testMove, true)) {
                            // valid move
                            validMoves.add(testMove.copy());
                        } else {
                            // generated move is invalid, so we skip it
                        }
                    }
                }

            }
        }
        return validMoves;
    }

    /**
     * evaluate the current game state from the view of the current player. High
     * numbers indicate a better situation for the current player.
     *
     * @return integer score of current game state
     */
    private int evaluateState() {

		// add up score
        //
        int scoreWhite = 0;
        int scoreBlack = 0;
        for (Piece piece : this.game.getPieces()) {
            if (piece.getColor() == Piece.COLOR_BLACK) {
//                scoreBlack                        += getScoreForPieceType(piece.getType());
                scoreBlack
                        += getScoreForPiecePosition(piece.getRow(), piece.getColumn());
            } else if (piece.getColor() == Piece.COLOR_WHITE) {
//                scoreWhite+= getScoreForPieceType(piece.getType());
                scoreWhite
                        += getScoreForPiecePosition(piece.getRow(), piece.getColumn());
            } else {
                throw new IllegalStateException(
                        "unknown piece color found: " + piece.getColor());
            }
        }

        // return evaluation result depending on who's turn it is
        int gameState = this.game.getGameState();

        if (gameState == Game.GAME_STATE_BLACK) {
            return scoreBlack - scoreWhite;

        } else if (gameState == Game.GAME_STATE_WHITE) {
            return scoreWhite - scoreBlack;

        } else if (gameState == Game.GAME_STATE_END_WHITE_WON
                || gameState == Game.GAME_STATE_END_BLACK_WON) {
            return Integer.MIN_VALUE + 1;

        } else {
            throw new IllegalStateException("unknown game state: " + gameState);
        }
    }

    /**
     * get the evaluation bonus for the specified position
     *
     * @param row - one of Piece.ROW_..
     * @param column - one of Piece.COLUMN_..
     * @return integer score
     */
    private int getScoreForPiecePosition(int row, int column) {
        byte[][] positionWeight
                = {{1, 1, 1, 1, 1, 1, 1, 1}, {2, 2, 2, 2, 2, 2, 2, 2}, {2, 2, 3, 3, 3, 3, 2, 2}, {2, 2, 3, 4, 4, 3, 2, 2}, {2, 2, 3, 4, 4, 3, 2, 2}, {2, 2, 3, 3, 3, 3, 2, 2}, {2, 2, 2, 2, 2, 2, 2, 2}, {1, 1, 1, 1, 1, 1, 1, 1}
                };
        return positionWeight[row][column];
    }
}
