/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch05.logic;

/**
 *
 * @author praburangki
 */
public class MoveValidator {

    private Game game;
    private Piece sourcePiece;
    private Piece targetPiece;

    public MoveValidator(Game game) {
        this.game = game;
    }

    public boolean isMoveValid(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {
        sourcePiece = game.getNonCapturedPieceAtLocation(sourceRow, sourceColumn);
        targetPiece = game.getNonCapturedPieceAtLocation(targetRow, targetColumn);

        if (sourcePiece == null) {
            System.out.println("no source piece");

            return false;
        }

        if (sourcePiece.getColor() == Piece.COLOR_WHITE
                && this.game.getGameState() == Game.GAME_STATE_WHITE) {
            // ok
        } else if (sourcePiece.getColor() == Piece.COLOR_BLACK
                && this.game.getGameState() == Game.GAME_STATE_BLACK) {
            // ok
        } else {
            System.out.println("it's not your turn");
            return false;
        }
        
        // check if target location within boundaries
        if (targetRow < Piece.ROW_1 || targetRow > Piece.ROW_6
                || targetColumn < Piece.COLUMN_A || targetColumn > Piece.COLUMN_F) {
            System.out.println("target row or column out of scope");
            return false;
        }

        // validate piece movement rules
        boolean validPieceMove = isValidMove(sourceRow, sourceColumn, targetRow, targetColumn);

        if (!validPieceMove) {
            return false;
        }

        return true;
    }
    
    private boolean isTargetLocationCaptureable() {
        if (targetPiece == null) {
            return false;
        } else {
            return targetPiece.getColor() != sourcePiece.getColor();
        }
    }

    private boolean isTargetLocationFree() {
        return targetPiece == null;
    }

    private boolean isValidMove(int sourceRow, int sourceColumn, int targetRow, int targetColumn) {

        // target location possible?
        if (isTargetLocationFree() || isTargetLocationCaptureable()) {
            //ok
        } else {
            System.out.println("target location not free and not captureable");
            return false;
        }

		// The king moves one square in any direction, the king has also a special move which is
        // called castling and also involves a rook.
        boolean isValid = true;
        if (sourceRow + 1 == targetRow && sourceColumn == targetColumn) {
            //up
            isValid = true;
        } else if (sourceRow + 1 == targetRow && sourceColumn + 1 == targetColumn) {
            //up right
            isValid = true;
        } else if (sourceRow == targetRow && sourceColumn + 1 == targetColumn) {
            //right
            isValid = true;
        } else if (sourceRow - 1 == targetRow && sourceColumn + 1 == targetColumn) {
            //down right
            isValid = true;
        } else if (sourceRow - 1 == targetRow && sourceColumn == targetColumn) {
            //down
            isValid = true;
        } else if (sourceRow - 1 == targetRow && sourceColumn - 1 == targetColumn) {
            //down left
            isValid = true;
        } else if (sourceRow == targetRow && sourceColumn - 1 == targetColumn) {
            //left
            isValid = true;
        } else if (sourceRow + 1 == targetRow && sourceColumn - 1 == targetColumn) {
            //up left
            isValid = true;
        } else {
            System.out.println("moving too far");
            isValid = false;
        }

        return isValid;
    }
}
