/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Surakarta.logic;

import java.util.Vector;

/**
 *
 * @author praburangki
 */
public class Board {
    Game game;
    int [][] board;
    Vector bestMove;
    
    public Board(Game game) {
        this.game = game;
        board = new int[6][6];
        generateBoard();
    }
    
    private void generateBoard() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (game.isBlack(i, j))
                    board[i][j] = 2;
                else if (game.isWhite(i, j))
                    board[i][j] = 1;
                else
                    board[i][j] = 0;
            }
        }
    }
}
