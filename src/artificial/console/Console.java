/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial.console;

import artificial.logic.Game;
import artificial.logic.IPlayerHandler;
import artificial.logic.Move;
import artificial.logic.Piece;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author praburangki
 */
public class Console implements IPlayerHandler {

    private Game game;

    public Console(Game game) {
        this.game = game;
        printCurrentGameState(this.game);
    }

    /**
     * Return move object corresponding to string
     *
     * @param input - a valid move-string (e.g. "e7-e6")
     */
    private Move convertStringToMove(String input) {
        if (input == null || input.length() != 5) {
            return null;
        }

        String strSourceColumn = input.substring(0, 1);
        String strSourceRow = input.substring(1, 2);
        String strTargetColumn = input.substring(3, 4);
        String strTargetRow = input.substring(4, 5);

        int sourceColumn = 0;
        int sourceRow = 0;
        int targetColumn = 0;
        int targetRow = 0;

        sourceColumn = convertColumnStrToColumnInt(strSourceColumn);
        sourceRow = convertRowStrToRowInt(strSourceRow);
        targetColumn = convertColumnStrToColumnInt(strTargetColumn);
        targetRow = convertRowStrToRowInt(strTargetRow);

        return new Move(sourceRow, sourceColumn, targetRow, targetColumn);
    }

    /**
     * Converts a column string (e.g. 'a') into its internal representation.
     *
     * @param strColumn a valid column string (e.g. 'a')
     * @return internal integer representation of the column
     */
    private int convertColumnStrToColumnInt(String strColumn) {
        if (strColumn.equalsIgnoreCase("a")) {
            return Piece.COLUMN_A;
        } else if (strColumn.equalsIgnoreCase("b")) {
            return Piece.COLUMN_B;
        } else if (strColumn.equalsIgnoreCase("c")) {
            return Piece.COLUMN_C;
        } else if (strColumn.equalsIgnoreCase("d")) {
            return Piece.COLUMN_D;
        } else if (strColumn.equalsIgnoreCase("e")) {
            return Piece.COLUMN_E;
        } else if (strColumn.equalsIgnoreCase("f")) {
            return Piece.COLUMN_F;
        } else {
            throw new IllegalArgumentException("invalid column: " + strColumn);
        }
    }

    /**
     * Converts a row string (e.g. '1') into its internal representation.
     *
     * @param strRow a valid row string (e.g. '1')
     * @return internal integer representation of the row
     */
    private int convertRowStrToRowInt(String strRow) {
        if (strRow.equalsIgnoreCase("1")) {
            return Piece.ROW_1;
        } else if (strRow.equalsIgnoreCase("2")) {
            return Piece.ROW_2;
        } else if (strRow.equalsIgnoreCase("3")) {
            return Piece.ROW_3;
        } else if (strRow.equalsIgnoreCase("4")) {
            return Piece.ROW_4;
        } else if (strRow.equalsIgnoreCase("5")) {
            return Piece.ROW_5;
        } else if (strRow.equalsIgnoreCase("6")) {
            return Piece.ROW_6;
        } else {
            throw new IllegalArgumentException("invalid column: " + strRow);
        }
    }

    /**
     * Print current game board and game state information.
     */
    public static void printCurrentGameState(Game game) {

        System.out.println("  a  b  c  d  e  f  g  h  ");
        for (int row = Piece.ROW_6; row >= Piece.ROW_1; row--) {

            System.out.println(" +--+--+--+--+--+--+--+--+");
            String strRow = (row + 1) + "|";
            for (int column = Piece.COLUMN_A; column <= Piece.COLUMN_F; column++) {
                Piece piece = game.getNonCapturedPieceAtLocation(row, column);
                String pieceStr = getNameOfPiece(piece);
                strRow += pieceStr + "|";
            }
            System.out.println(strRow + (row + 1));
        }
        System.out.println(" +--+--+--+--+--+--+--+--+");
        System.out.println("  a  b  c  d  e  f  g  h  ");

        String gameStateStr = "unknown";
        switch (game.getGameState()) {
            case Game.GAME_STATE_BLACK:
                gameStateStr = "black";
                break;
            case Game.GAME_STATE_END_WHITE_WON:
                gameStateStr = "white won";
                break;
            case Game.GAME_STATE_END_BLACK_WON:
                gameStateStr = "black won";
                break;
            case Game.GAME_STATE_WHITE:
                gameStateStr = "white";
                break;
        }
        System.out.println("state: " + gameStateStr);

    }

    /**
     * Returns the name of the specified piece. The name is based on color and
     * type.
     *
     * The first letter represents the color: B=black, W=white, ?=unknown
     *
     * The second letter represents the type: B=Bishop, K=King, N=Knight,
     * P=Pawn, Q=Queen, R=Rook, ?=unknown
     *
     * A two letter empty string is returned in case the specified piece is null
     *
     * @param piece a chess piece
     * @return string representation of the piece or a two letter empty string
     * if the specified piece is null
     */
    private static String getNameOfPiece(Piece piece) {
        if (piece == null) {
            return "  ";
        }

        String strColor;
        switch (piece.getColor()) {
            case Piece.COLOR_BLACK:
                strColor = "B";
                break;
            case Piece.COLOR_WHITE:
                strColor = "W";
                break;
            default:
                strColor = "?";
                break;
        }

        return strColor;
    }

    @Override
    public Move getMove() {
        System.out.println("your move (format: e2-e3): ");

        Move move = null;
        while (move == null) {
            //read user input
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            String input;
            try {
                input = inputReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            // exit, if user types 'exit'
            if (input.equalsIgnoreCase("exit")) {
                System.exit(0);
            } else {
                move = this.convertStringToMove(input);
            }
        }
        return move;
    }

    @Override
    public void moveSuccessfullyExecuted(Move move) {
        printCurrentGameState(this.game);

        if (this.game.getGameState() == Game.GAME_STATE_END_BLACK_WON) {
            System.out.println("game end reached! Black won!");
        } else if (this.game.getGameState() == Game.GAME_STATE_END_WHITE_WON) {
            System.out.println("game end reached! White won!");
        }
    }
}
