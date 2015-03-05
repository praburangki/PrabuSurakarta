/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch06.logic;

import java.util.*;

/**
 *
 * @author Prabu Rangki
 */
public class Game {

    private int gameState = GAME_STATE_WHITE;
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END = 2;

    
    // 0 = bottom, size = top
    private List<Piece> pieces = new ArrayList<Piece>();
    private MoveValidator moveValidator;
    private Map map;

    public Game() {
        map = new Map();
        map.init();


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

        
        moveValidator = new MoveValidator(this, map);
        updateMap();
        
    }

    private void updateMap() {
        map.clear();
        for (Piece piece : pieces) {
            int x = piece.getRow();
            int y = piece.getColumn();
            int color = piece.getColor();

            if (color == Piece.COLOR_WHITE && !piece.isCaptured()) {
                map.map[x][y] = 1;
            } else if (color == Piece.COLOR_BLACK && !piece.isCaptured()) {
                map.map[x][y] = 2;
            } 
        }
        
        moveValidator.setMap(map);
    }

    private void createAndAddPiece(int color, int row, int column) {
        Piece piece = new Piece(color, row, column);
        pieces.add(piece);
    }

    /**
     * Move piece to the specified location. If the target location is occupied
     * by an opponent piece, that piece is marked as 'captured'
     *
     * @param sourceRow the source row (Piece.ROW_..) of the piece to move
     * @param sourceColumn the source column (Piece.COLUMN_..) of the piece to
     * move
     * @param targetRow the target row (Piece.ROW_..)
     * @param targetColumn the target column (Piece.COLUMN_..)
     */
    public boolean movePiece(Move move) {
        if (!moveValidator.isMoveValid(move)) {
            System.out.println("move invalid");
            return false;
        }

        Piece piece = getNonCapturedPieceAtLocation(move.sourceRow, move.sourceColumn);
//        map.map[move.sourceRow][move.sourceColumn] = 0;
        int opponentColor = piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        if (isNonCapturedPieceAtLocation(opponentColor, move.targetRow, move.targetColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
            opponentPiece.isCaptured(true);

        }

        piece.setRow(move.targetRow);
        piece.setColumn(move.targetColumn);
//        map.map[piece.getRow()][piece.getColumn()] = piece.getColor();

        if (isGameEndConditionReached()) {
            gameState = GAME_STATE_END;
        } else {
            changeGameState();
        }

        updateMap();

        return true;
    }

    private boolean isGameEndConditionReached() {
        int blackNum = 0;
        int whiteNum = 0;
        for (Piece piece : pieces) {
            if(piece.getColor() == Piece.COLOR_BLACK)
                blackNum++;
            else
                whiteNum++;
        }
        
        return blackNum == 0 || whiteNum == 0;
    }

    /**
     * returns the first piece at the specified location that is not marked as
     * 'captured'.
     *
     * @param row one of Piece.ROW_..
     * @param column one of Piece.COLUMN_..
     * @return the first not captured piece at the specified location
     */
    public Piece getNonCapturedPieceAtLocation(int row, int col) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row
                    && piece.getColumn() == col
                    && piece.isCaptured() == false) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Checks whether there is a piece at the specified location that is not
     * marked as 'captured' and has the specified color.
     *
     * @param color one of Piece.COLOR_..
     * @param row one of Piece.ROW_..
     * @param column on of Piece.COLUMN_..
     * @return true, if the location contains a not-captured piece of the
     * specified color
     */
    private boolean isNonCapturedPieceAtLocation(int color, int row, int col) {
        for (Piece piece : pieces) {
            if (piece.getRow() == row
                    && piece.getColumn() == col
                    && !piece.isCaptured()
                    && piece.getColor() == color) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether there is a non-captured piece at the specified location
     *
     * @param row one of Piece.ROW_..
     * @param column on of Piece.COLUMN_..
     * @return true, if the location contains a piece
     */
    public boolean isNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == column
                    && piece.isCaptured() == false) {
                return true;
            }
        }
        return false;
    }

    public int getGameState() {
        return gameState;
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void changeGameState() {
        if (isGameEndConditionReached()) {

            if (this.gameState == Game.GAME_STATE_BLACK) {
                System.out.println("Game over! Black won!");
            } else {
                System.out.println("Game over! White won!");
            }

            this.gameState = Game.GAME_STATE_END;
            return;
        }

        switch (gameState) {
            case GAME_STATE_BLACK:
                gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                gameState = GAME_STATE_BLACK;
                break;
            default:
                throw new IllegalStateException("unknown game state : " + gameState);
        }
    }

    public MoveValidator getMoveValidator() {
        return moveValidator;
    }
}
