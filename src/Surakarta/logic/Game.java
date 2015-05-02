package Surakarta.logic;

import java.awt.Point;
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
    public boolean[][] blacks, whites;

    private List<Piece> pieces = new ArrayList<Piece>();

    private MoveValidator moveValidator;
    
    public Game() {
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

    private void createAndAddPiece(int color, int row, int column) {
        Piece piece = new Piece(color, row, column);
        pieces.add(piece);
    }

    public boolean movePiece(Move move) {
        if (!moveValidator.isMoveValid(move)) {
            System.out.println("move invalid");

            return false;
        }
        
        Piece piece = getNonCapturedPieceAtLocation(move.sourceRow, move.sourceColumn);
        move.isWhite = piece.getColor() == Piece.COLOR_WHITE;

        int opponentColor = piece.getColor() == Piece.COLOR_BLACK ? Piece.COLOR_WHITE : Piece.COLOR_BLACK;
        if (isNonCapturedPieceAtLocation(opponentColor, move.targetRow, move.targetColumn)) {
            Piece opponentPiece = getNonCapturedPieceAtLocation(move.targetRow, move.targetColumn);
            opponentPiece.isCaptured(true);
        }

        piece.setRow(move.targetRow);
        piece.setColumn(move.targetColumn);
        
        if (isGameEndConditionReached()) {
            gameState = GAME_STATE_END;
        } else {
            changeGameState();
        }
        
        
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
                    && !piece.isCaptured())
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
        if (isGameEndConditionReached()) {
            if (this.gameState == Game.GAME_STATE_BLACK)
                System.out.println("Game over! Black won!");
            else
                System.out.println("Game over! White won!");

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
}
