package Surakarta.logic;

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
    private boolean[][] blacks, whites;

    private List<Piece> pieces = new ArrayList<Piece>();

    public Game() {
        this.blacks = new boolean[6][6];
        this.whites = new boolean[6][6];
        
        int i = 0;
        do {
            int j = 0;
            do {
                this.blacks[i][j] = true;
                createAndAddPiece(Piece.COLOR_BLACK, i, j);
                this.whites[(i + 4)][j] = true;
                createAndAddPiece(Piece.COLOR_WHITE, i + 4, j);
                j++;
            } while (j < 6);
            i++;
        } while (i < 2);
        System.out.println(pieces);
    }

    private void createAndAddPiece(int color, int row, int column) {
        Piece piece = new Piece(color, row, column);
        this.pieces.add(piece);
    }

    public void movePiece(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        Piece piece = getNonCapturedPieceAtLocation(sourceRow, sourceColumn);

        int opponentColor = piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        if (isNonCapturedPieceAtLocation(opponentColor, targetRow, targetColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(targetRow, targetColumn);
            opponentPiece.isCaptured(true);
        }

        piece.setRow(targetRow);
        piece.setColumn(targetColumn);
    }

    private Piece getNonCapturedPieceAtLocation(int row, int column) {
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
