package ch05.logic;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author praburangki
 */
public class Game {

    private int gameState = GAME_STATE_WHITE;
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END = 2;

    private List<Piece> pieces = new ArrayList<Piece>();

    private MoveValidator moveValidator;

    public Game() {
        this.moveValidator = new MoveValidator(this);
        int currentColumn = Piece.COLUMN_A;
        for (int i = 0; i < 6; i++) {
            createAndAddPiece(Piece.COLOR_WHITE, Piece.ROW_1, currentColumn);
            currentColumn++;
        }

        currentColumn = Piece.COLUMN_A;
        for (int i = 0; i < 6; i++) {
            createAndAddPiece(Piece.COLOR_WHITE, Piece.ROW_2, currentColumn);
            currentColumn++;
        }

        currentColumn = Piece.COLUMN_A;
        for (int i = 0; i < 6; i++) {
            createAndAddPiece(Piece.COLOR_BLACK, Piece.ROW_5, currentColumn);
            currentColumn++;
        }

        currentColumn = Piece.COLUMN_A;
        for (int i = 0; i < 6; i++) {
            createAndAddPiece(Piece.COLOR_BLACK, Piece.ROW_6, currentColumn);
            currentColumn++;
        }
    }

    private void createAndAddPiece(int color, int row, int column) {
        Piece piece = new Piece(color, row, column);
        this.pieces.add(piece);
    }

    public boolean movePiece(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        if (!this.moveValidator.isMoveValid(sourceRow, sourceColumn, targetRow, targetColumn)) {
            System.out.println("move invalid");

            return false;
        }

        Piece piece = getNonCapturedPieceAtLocation(sourceRow, sourceColumn);

        int opponentColor = piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        if (isNonCapturedPieceAtLocation(opponentColor, targetRow, targetColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
            opponentPiece.isCaptured(true);
        }

        piece.setRow(targetRow);
        piece.setColumn(targetColumn);

        if (isGameEndConditionReached()) {
            this.gameState = GAME_STATE_END;
        } else {
            this.changeGameState();
        }

        return true;
    }

    private boolean isGameEndConditionReached() {
        for (Piece piece : this.pieces) {

        }

        return false;
    }

    public Piece getNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row
                    && piece.getColumn() == column
                    && piece.isCaptured() == false) {
                return piece;
            }
        }
        return null;
    }

    private boolean isNonCapturedPieceAtLocation(int color, int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row
                    && piece.getColumn() == column
                    && !piece.isCaptured()
                    && piece.getColor() == color) {
                return true;
            }
        }
        return false;
    }

    public boolean isNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row
                    && piece.getColumn() == column
                    && !piece.isCaptured()) {
                return true;
            }
        }
        return false;
    }

    public int getGameState() {
        return this.gameState;
    }

    public List<Piece> getPieces() {
        return this.pieces;
    }

    public void changeGameState() {
        if (this.isGameEndConditionReached()) {
            if (this.gameState == Game.GAME_STATE_BLACK) {
                System.out.println("Game over! Black won!");
            } else {
                System.out.println("Game over! White won!");
            }

            this.gameState = Game.GAME_STATE_END;
            return;
        }

        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            case GAME_STATE_END:
                // don't change anymore
                break;
            default:
                throw new IllegalStateException("unknown game state : " + this.gameState);
        }
    }
}
