package ch04.logic;

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

    private List<Piece> pieces = new ArrayList<Piece>();

    public Game() {
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

    public void movePiece(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        Piece piece = getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
        
        if(piece == null) {
            throw new IllegalArgumentException("No piece at source location");
        }
        
        if(piece.getColor() == Piece.COLOR_WHITE && this.gameState != Game.GAME_STATE_WHITE
                || piece.getColor() == Piece.COLOR_BLACK && this.gameState != Game.GAME_STATE_BLACK) {
            throw new IllegalArgumentException("It's not your turn");
        }

        int opponentColor = piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        if (isNonCapturedPieceAtLocation(opponentColor, targetRow, targetColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
            opponentPiece.isCaptured(true);
        }

        piece.setRow(targetRow);
        piece.setColumn(targetColumn);
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

    public int getGameState() {
        return this.gameState;
    }

    public List<Piece> getPieces() {
        return this.pieces;
    }

    public void changeGameState() {
        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            default:
                throw new IllegalStateException("unknown game state : " + this.gameState);
        }
    }
}
