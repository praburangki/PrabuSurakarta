/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08.logic;

import ch08.ai.Evaluate;
import ch08.console.ConsoleGui;
import java.util.*;

/**
 *
 * @author Prabu Rangki
 */
public class Game implements Runnable {

    public int gameState = GAME_STATE_WHITE;
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END_BLACK_WON = 2;
    public static final int GAME_STATE_END_WHITE_WON = 3;

    // 0 = bottom, size = top
    public List<Piece> pieces = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    private MoveValidator moveValidator;
    private IPlayerHandler blackPlayerHandler;
    private IPlayerHandler whitePlayerHandler;
    private IPlayerHandler activePlayerHandler;
    private Map map;

    /**
     * initialize game
     */
    public Game() {
        map = new Map();
        map.init();

        this.moveValidator = new MoveValidator(this, map);

        // create and place pieces
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

        updateMap();
    }

    /**
     * set the player/client for the specified piece color
     *
     * @param pieceColor - the color the client/player represents
     * @param playerHandler - the player/client
     */
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

    /**
     * start main game flow
     */
    public void startGame() {
        // check if all players are ready

        System.out.println("Game : waiting for players");
        while (this.blackPlayerHandler == null || this.whitePlayerHandler == null) {
            // players are still missing
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // set start player
        this.activePlayerHandler = this.whitePlayerHandler;

        // start game flow
        System.out.println("Game : starting game flow");
        while (!isGameEndConditionReached()) {
            waitForMove();
            swapActivePlayer();
        }

        System.out.println("Game : game ended");
        ConsoleGui.printCurrentGameState(this);
        if (this.gameState == Game.GAME_STATE_END_BLACK_WON) {
            System.out.println("Black won!");
        } else if (this.gameState == Game.GAME_STATE_END_WHITE_WON) {
            System.out.println("White won!");
        } else {
            throw new IllegalStateException("Illegal end state : " + this.gameState);
        }
    }

    /**
     * swap active player and change game state
     */
    private void swapActivePlayer() {
        if (this.activePlayerHandler == this.whitePlayerHandler) {
            this.activePlayerHandler = this.blackPlayerHandler;
        } else {
            this.activePlayerHandler = this.whitePlayerHandler;
        }

        this.changeGameState();
    }

    /**
     * Wait for client/player move and execute it. Notify all clients/players
     * about successful execution of move.
     */
    private void waitForMove() {
        Move move = null;
        // wait for a valid move
        
        do {
            move = this.activePlayerHandler.getMove();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            

            if (move != null && this.moveValidator.isMoveValid(move)) {
                break;
            } else if (move != null && !this.moveValidator.isMoveValid(move)) {
                System.out.println("provided move was invalid : " + move);
//                ConsoleGui.printCurrentGameState(this);
                move = null;
                System.exit(0);
            }
        } while (move == null);

        //execute move
        boolean success = this.movePiece(move);
        if (success) {
            this.blackPlayerHandler.moveSuccessfullyExecuted(move);
            this.whitePlayerHandler.moveSuccessfullyExecuted(move);
            Evaluate eva = new Evaluate(this);
//            int[][] m = eva.getMap().map;
//            for (int i = 0; i < m.length; i++) {
//                System.out.println(Arrays.toString(m[i]));
//            }
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

    /**
     * create piece instance and add it to the internal list of pieces
     *
     * @param color on of Pieces.COLOR_..
     * @param row on of Pieces.ROW_..
     * @param column on of Pieces.COLUMN_..
     */
    public void createAndAddPiece(int color, int row, int column) {
        Piece piece = new Piece(color, row, column);
        pieces.add(piece);
    }

    /**
     * Move piece to the specified location. If the target location is occupied
     * by an opponent piece, that piece is marked as 'captured'. If the move
     * could not be executed successfully, 'false' is returned and the game
     * state does not change.
     *
     * @param move to execute
     * @return true, if piece was moved successfully
     */
    public boolean movePiece(Move move) {
        //set captured piece in move
        // this information is needed in the undoMove() method.
        move.capturedPiece = this.getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);

        Piece piece = getNonCapturedPieceAtLocation(move.sourceRow, move.sourceColumn);

        // check if the move is capturing an opponent piece
        int opponentColor = piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        if (isNonCapturedPieceAtLocation(opponentColor, move.targetRow, move.targetColumn)) {
            // handle captured piece 
            Piece opponentPiece = getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
            this.pieces.remove(opponentPiece);
            this.capturedPieces.add(opponentPiece);
            opponentPiece.isCaptured(true);

        }

        // move piece to new position 
        piece.setRow(move.targetRow);
        piece.setColumn(move.targetColumn);

        updateMap();

        return true;
    }

    /**
     * Undo the specified move. It will also adjust the game state
     * appropriately.
     *
     * @param move
     */
    public void undoMove(Move move) {
        Piece piece = getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);

        piece.setRow(move.sourceRow);
        piece.setColumn(move.sourceColumn);

        if (move.capturedPiece != null) {
            move.capturedPiece.setRow(move.targetRow);
            move.capturedPiece.setColumn(move.targetColumn);
            move.capturedPiece.isCaptured(false);
            this.capturedPieces.remove(move.capturedPiece);
            this.pieces.add(move.capturedPiece);
        }

        if (piece.getColor() == Piece.COLOR_BLACK) {
            this.gameState = Game.GAME_STATE_BLACK;
        } else {
            this.gameState = Game.GAME_STATE_WHITE;
        }
        
        updateMap();
    }

    /**
     * check if the games end condition is met: one color does not have any
     * pieces left
     *
     * @return true if the game end condition is met
     */
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

    /**
     * returns the first piece at the specified location that is not marked as
     * 'captured'.
     *
     * @param row one of Piece.ROW_..
     * @param col one of Piece.COLUMN_..
     * @return the first not captured piece at the specified location
     */
    public Piece getNonCapturedPieceAtLocation(int row, int col) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row
                    && piece.getColumn() == col) {
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
     * @param col on of Piece.COLUMN_..
     * @return true, if the location contains a not-captured piece of the
     * specified color
     */
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

    /**
     * Checks whether there is a non-captured piece at the specified location
     *
     * @param row one of Piece.ROW_..
     * @param column on of Piece.COLUMN_..
     * @return true, if the location contains a piece
     */
    boolean isNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == column) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return current game state (one of ChessGame.GAME_STATE_..)
     */
    public int getGameState() {
        return gameState;
    }

    /**
     * @return the internal list of pieces
     */
    public List<Piece> getPieces() {
        return pieces;
    }

    /**
     * switches the game state depending on the current board situation.
     */
    public void changeGameState() {

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

    /**
     * @return current move validator
     */
    public MoveValidator getMoveValidator() {
        return moveValidator;
    }

    @Override
    public void run() {
        this.startGame();
    }

    public ch08.logic.Map getMap() {
        return map;
    }
}
