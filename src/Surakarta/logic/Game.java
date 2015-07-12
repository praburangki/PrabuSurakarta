package Surakarta.logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author praburangki
 */
public class Game implements Runnable {

    public int gameState = GAME_STATE_BLACK;
    public static final int GAME_STATE_WHITE = 0;
    public static final int GAME_STATE_BLACK = 1;
    public static final int GAME_STATE_END_BLACK_WON = 2;
    public static final int GAME_STATE_END_WHITE_WON = 3;
    public boolean[][] blacks, whites;
    public int moveCount;

    public List<Piece> pieces = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    private MoveValidator moveValidator;
    private IPlayerHandler blackPlayerHandler;
    private IPlayerHandler whitePlayerHandler;
    private IPlayerHandler activePlayerHandler;
    
    public Game() {
        moveCount = 0;
        this.moveValidator = new MoveValidator(this);
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

        this.activePlayerHandler = this.blackPlayerHandler;

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
        long start = System.currentTimeMillis();
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
                move = null;
                System.exit(0);
            }
        } while (move == null);
        long end = System.currentTimeMillis();
        
        System.out.println("Time elapsed: " + (end - start) + " miliseconds");

        //execute move
        boolean success = this.movePiece(move);
        if (success) {
            this.blackPlayerHandler.moveSuccessfullyExecuted(move);
            this.whitePlayerHandler.moveSuccessfullyExecuted(move);
            moveCount++;
            
        } else {
            throw new IllegalStateException("move was valid, but failed to execute it.");
        }
    }

    public void createAndAddPiece(int color, int row, int column) {
        Piece piece = new Piece(color, row, column);
        pieces.add(piece);
    }

    public boolean movePiece(Move move) {
        move.capturedPiece = this.getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
        Piece piece = getNonCapturedPieceAtLocation(move.sourceRow, move.sourceColumn);
        move.isWhite = piece.getColor() == Piece.COLOR_WHITE;

        int opponentColor = piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        if (isNonCapturedPieceAtLocation(opponentColor, move.targetRow, move.targetColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
            this.pieces.remove(opponentPiece);
            this.capturedPieces.add(opponentPiece);
            opponentPiece.isCaptured(true);
        }

        piece.setRow(move.targetRow);
        piece.setColumn(move.targetColumn);
        
        this.whites[move.sourceRow][move.sourceColumn] = false;
        this.blacks[move.sourceRow][move.sourceColumn] = false;
        if (move.isWhite) {
            this.whites[move.targetRow][move.targetColumn] = true;
            this.blacks[move.targetRow][move.targetColumn] = false;
        } else {
            this.blacks[move.targetRow][move.targetColumn] = true;
            this.whites[move.targetRow][move.targetColumn] = false;
        }

        return true;
    }
    
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
        
        this.whites[move.sourceRow][move.sourceColumn] = this.whites[move.targetRow][move.targetColumn];
        this.blacks[move.sourceRow][move.sourceColumn] = this.blacks[move.targetRow][move.targetColumn];
        if (move.isCapture) {
            if (move.isWhite) {
                this.blacks[move.targetRow][move.targetColumn] = true;
                this.whites[move.targetRow][move.targetColumn] = false;
            } else {
                this.blacks[move.targetRow][move.targetColumn] = false;
                this.whites[move.targetRow][move.targetColumn] = true;
            }
        } else {
            this.whites[move.targetRow][move.targetColumn] = false;
            this.blacks[move.targetRow][move.targetColumn] = false;
        }
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
        if (this.isGameEndConditionReached()) {

            if (this.gameState == Game.GAME_STATE_BLACK) {
                this.gameState = Game.GAME_STATE_END_BLACK_WON;
            } else if (this.gameState == Game.GAME_STATE_WHITE) {
                this.gameState = Game.GAME_STATE_END_WHITE_WON;
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
                break;
            default:
                throw new IllegalStateException("unknown game state:" + this.gameState);
        }
    }
    
    public boolean isEmpty(int x, int y) {
        boolean e = this.blacks[x][y];
        if (!e) {
            e = this.whites[x][y];
        }
        return e ^ true;
    }

    public boolean isEmpty(Point p) {
        boolean black = this.blacks[p.y][p.x];
        boolean white = this.whites[p.y][p.x];
        boolean temp = black;
        if (!temp) {
            temp = white;
        }
        return temp ^ true;
    }

    public boolean isBlack(int x, int y) {
        return this.blacks[x][y];
    }

    public boolean isBlack(Point p) {
        return this.blacks[p.y][p.x];
    }

    public boolean isWhite(int x, int y) {
        return this.whites[x][y];
    }

    public boolean isWhite(Point p) {
        return this.whites[p.y][p.x];
    }
    
    public MoveValidator getMoveValidator() {
        return moveValidator;
    }

    @Override
    public void run() {
        this.startGame();
    }
}
