/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch07.logic;

import java.util.*;

/**
 *
 * @author Prabu Rangki
 */
public class Game implements Runnable {

    private int gameState = GAME_STATE_WHITE;
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END_BLACK_WON = 2;
    public static final int GAME_STATE_END_WHITE_WON = 3;

    // 0 = bottom, size = top
    private List<Piece> pieces = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    private MoveValidator moveValidator;
    private IPlayerHandler blackPlayerHandler;
    private IPlayerHandler whitePlayerHandler;
    private IPlayerHandler activePlayerHandler;
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

    public void setPlayer(int pieceColor, IPlayerHandler playerHandler) {
        switch (pieceColor) {
            case Piece.COLOR_BLACK:
                this.blackPlayerHandler = playerHandler;
                break;
            case Piece.COLOR_WHITE:
                this.whitePlayerHandler = playerHandler;
                break;
            default:
                throw new IllegalArgumentException("Invalid pieceColor : " + pieceColor);
        }
    }

    public void startGame() {
        System.out.println("Game : waiting for players");
        while (this.blackPlayerHandler == null || this.whitePlayerHandler == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

        this.activePlayerHandler = this.whitePlayerHandler;

        System.out.println("Game : starting game flow");
        while (!isGameEndConditionReached()) {
            waitForMove();
            swapActivePlayer();
        }

        System.out.println("Game : game ended");
    }

    private void swapActivePlayer() {
        if (this.activePlayerHandler == this.whitePlayerHandler) {
            this.activePlayerHandler = this.blackPlayerHandler;
        } else {
            this.activePlayerHandler = this.whitePlayerHandler;
        }

        this.changeGameState();
    }

    private void waitForMove() {
        Move move = null;
        do {
            move = this.activePlayerHandler.getMove();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (move == null || !this.moveValidator.isMoveValid(move, false));

        boolean success = this.movePiece(move);
        if (success) {
            this.blackPlayerHandler.moveSuccessfullyExecuted(move);
            this.whitePlayerHandler.moveSuccessfullyExecuted(move);
        } else {
            throw new IllegalStateException("move was valid, but failed to execute it.");
        }
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

    public boolean movePiece(Move move) {

        Piece piece = getNonCapturedPieceAtLocation(move.sourceRow, move.sourceColumn);
//        map.map[move.sourceRow][move.sourceColumn] = 0;
        int opponentColor = piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        if (isNonCapturedPieceAtLocation(opponentColor, move.targetRow, move.targetColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
            this.pieces.remove(opponentPiece);
            this.capturedPieces.add(opponentPiece);
            opponentPiece.isCaptured(true);

        }

        piece.setRow(move.targetRow);
        piece.setColumn(move.targetColumn);
//        map.map[piece.getRow()][piece.getColumn()] = piece.getColor();

        updateMap();

        return true;
    }

    private boolean isGameEndConditionReached() {
        int blackNum = 0;
        int whiteNum = 0;
        for (Piece piece : pieces) {
            if (piece.getColor() == Piece.COLOR_BLACK) {
                blackNum++;
            } else {
                whiteNum++;
            }
        }

        return blackNum == 0 || whiteNum == 0;
    }

    public Piece getNonCapturedPieceAtLocation(int row, int col) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row
                    && piece.getColumn() == col) {
                return piece;
            }
        }
        return null;
    }

    boolean isNonCapturedPieceAtLocation(int color, int row, int col) {
        for (Piece piece : pieces) {
            if (piece.getRow() == row
                    && piece.getColumn() == col
                    && piece.getColor() == color) {
                return true;
            }
        }

        return false;
    }

    boolean isNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == column) {
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

    private void changeGameState() {

		// check if game end condition has been reached
        //
        if (this.isGameEndConditionReached()) {

            if (this.gameState == Game.GAME_STATE_BLACK) {
                this.gameState = Game.GAME_STATE_END_BLACK_WON;
            } else if (this.gameState == Game.GAME_STATE_WHITE) {
                this.gameState = Game.GAME_STATE_END_WHITE_WON;
            } else {
                // leave game state as it is
            }

            return;
        }

        switch (this.gameState) {
            case GAME_STATE_BLACK:
                this.gameState = GAME_STATE_WHITE;
                break;
            case GAME_STATE_WHITE:
                this.gameState = GAME_STATE_BLACK;
                break;
            case GAME_STATE_END_WHITE_WON:
            case GAME_STATE_END_BLACK_WON:
                // don't change anymore
                break;
            default:
                throw new IllegalStateException("unknown game state:" + this.gameState);
        }
    }

    public MoveValidator getMoveValidator() {
        return moveValidator;
    }
    
    @Override
    public void run() {
        this.startGame();
    }
}
