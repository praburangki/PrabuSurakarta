/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch06.console;

import ch06.logic.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author praburangki
 */
public class ConsoleGui {

    private Game game;

    public ConsoleGui() {
        this.game = new Game();
    }

    public static void main(String[] args) {
        new ConsoleGui().run();
    }

    public void run() {
        String input;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            this.printCurrentGameState();
            System.out.println("your move (e.g e2-e4):");
            try {
                input = inputReader.readLine();

                if (input.equalsIgnoreCase("exit")) {
                    return;
                } else {
                    this.handleMove(input);
                }
                
                if(this.game.getGameState() == Game.GAME_STATE_END) {
                    this.printCurrentGameState();
                    System.out.println("game end reached! you won!");
                    return;
                }
            } catch (Exception e) {
                System.out.println(e.getClass() + " : " + e.getMessage());
            }
        }
    }

    private void handleMove(String input) {
        String strSourceColumn = input.substring(0, 1);
        String strSourceRow = input.substring(1, 2);
        String strTargetColumn = input.substring(3, 4);
        String strTargetRow = input.substring(4, 5);

        int sourceColumn;
        int sourceRow;
        int targetColumn;
        int targetRow;

        sourceColumn = convertColumnStrToColumnInt(strSourceColumn);
        sourceRow = convertRowStrToRowInt(strSourceRow);
        targetColumn = convertColumnStrToColumnInt(strTargetColumn);
        targetRow = convertRowStrToRowInt(strTargetRow);

        game.movePiece(new Move(sourceRow, sourceColumn, targetRow, targetColumn));
    }

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

    private void printCurrentGameState() {
        System.out.println("  a  b  c  d  e  f  ");
        for (int row = Piece.ROW_6; row >= Piece.ROW_1; row--) {
            System.out.println(" +--+--+--+--+--+--+");
            String strRow = (row + 1) + "|";
            for (int column = Piece.COLUMN_A; column <= Piece.COLUMN_F; column++) {
                Piece piece = this.game.getNonCapturedPieceAtLocation(row, column);
                String pieceStr = getNameOfPiece(piece);
                strRow += pieceStr + "|";
            }
            System.out.println(strRow + (row + 1));
        }
        System.out.println(" +--+--+--+--+--+--+");
        System.out.println("  a  b  c  d  e  f  ");
        
        String gameStateStr = "unknown";
        switch(game.getGameState()) {
            case Game.GAME_STATE_BLACK : gameStateStr = "black"; break;
            case Game.GAME_STATE_END : gameStateStr = "end"; break;
            case Game.GAME_STATE_WHITE : gameStateStr = "white"; break;
        }
        System.out.println("state : " + gameStateStr);
    }

    private String getNameOfPiece(Piece piece) {
        if (piece == null) {
            return "  ";
        }
        
        String strColor;
        switch(piece.getColor()) {
            case Piece.COLOR_BLACK :
                strColor = " B";
                break;
            case Piece.COLOR_WHITE :
                strColor = " W";
                break;
            default :
                strColor = "?";
                break;
        }
        
        return strColor;
    }
}
