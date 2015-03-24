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
public class AiPlayerHandler implements IPlayerHandler {

    private Game game;
    private MoveValidator validator;
    private Map map;
    private Evaluate evaluate;

    public int maxDepth = 2;

    public AiPlayerHandler(Game game) {
        evaluate = new Evaluate(game);
        this.game = game;
        this.validator = this.game.getMoveValidator();
        this.map = game.getMap();
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
        System.out.println("thingking...");

        System.out.println("done thinking! best move is " + bestMove + "with bestResult : " + bestResult);

        return bestMove;
    }

    private String alphaBeta(int depth, int beta, int alpha, String move, int player) {
        String list = possibleMoves();
        if (depth == 0 || list.length() == 0) {
            Evaluate eval = new Evaluate(game);
            return move + (eval.evalue(game.getMap().map, player) * (player * 2 - 1));
        }

        player = 1 - player;

        for (int i = 0; i < list.length(); i += 5) {
            doMove(list.substring(i, i + 5));
            flipBoard();
            String returnString = alphaBeta(depth - 1, beta, alpha, list.substring(i, i + 5), player);
            int value = Integer.valueOf(returnString.substring(5));
            flipBoard();
            undoMove(list.substring(i, i + 5));
            if (player == 0) {
                if (value <= beta) {
                    beta = value;
                    if (depth == maxDepth) {
                        move = returnString.substring(0, 5);
                    }
                }
            } else {
                if (value > alpha) {
                    alpha = value;
                    if (depth == maxDepth) {
                        move = returnString.substring(0, 5);
                    }
                }
            }

            if (alpha >= beta) {
                if (player == 0) {
                    return move + beta;
                } else {
                    return move + alpha;
                }
            }
        }

        if (player == 0) {
            return move + beta;
        } else {
            return move + alpha;
        }
    }

    @Override
    public void moveSuccessfullyExecuted(Move move) {
        System.out.println("executed : " + move);
    }

    private void undoMove(Move move) {
        this.game.undoMove(move);
    }

    private void doMove(Move move) {
        this.game.movePiece(move);
        this.game.changeGameState();
    }
}
