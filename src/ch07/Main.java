/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch07;

import ch07.console.ConsoleGui;
import ch07.gui.Gui;
import ch07.logic.Game;
import ch07.logic.Piece;

/**
 *
 * @author Prabu Rangki
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        
        Gui gui = new Gui(game);
        ConsoleGui console = new ConsoleGui(game);
        
        game.setPlayer(Piece.COLOR_WHITE, gui);
        game.setPlayer(Piece.COLOR_BLACK, console);
        
        new Thread(game).start();
    }
}
