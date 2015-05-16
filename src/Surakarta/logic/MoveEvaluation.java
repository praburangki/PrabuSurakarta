package Surakarta.logic;

import java.util.Vector;

/**
 *
 * @author praburangki
 */
public class MoveEvaluation {
    public int value;
    public int depth;
    public Vector bestMove;

    public MoveEvaluation(int value, int depth, Vector bestMove) {
        this.value = value;
        this.depth = depth;
        this.bestMove = bestMove;
    }
}
