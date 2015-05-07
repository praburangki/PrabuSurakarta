package ch08.logic;

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
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.activePlayerHandler = this.whitePlayerHandler;

        System.out.println("Game : starting game flow");
        while (!isGameEndConditionReached()) {
            waitForMove();
            swapActivePlayer();
        }

        System.out.println("Game : game ended");
        if (this.gameState == Game.GAME_STATE_END_BLACK_WON) {
            System.out.println("Black won!");
        } else if (this.gameState == Game.GAME_STATE_END_WHITE_WON) {
            System.out.println("White won!");
        } else {
            throw new IllegalStateException("Illegal end state : " + this.gameState);
        }
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
                Thread.sleep(100);
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

    public boolean movePiece(Move move) {
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
            if(piece.getColor() == Piece.COLOR_BLACK)
                blackNum++;
            else
                whiteNum++;
        }
        
        return blackNum == 0 || whiteNum == 0;
    }

    public Piece getNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == column && piece.isCaptured() == false) {
                return piece;
            }
        }
        return null;
    }

    boolean isNonCapturedPieceAtLocation(int color, int row, int column) {
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
    
    boolean isNonCapturedPieceAtLocation(int row, int column) {
        for (Piece piece : this.pieces) {
            if (piece.getRow() == row && piece.getColumn() == column && !piece.isCaptured())
                return true;
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
