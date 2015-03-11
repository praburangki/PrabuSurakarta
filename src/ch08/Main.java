/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch08;

import ch08.ai.AiPlayerHandler;
import ch08.gui.Gui;
import ch08.logic.Game;
import ch08.logic.Piece;

/**
 *
 * @author Prabu Rangki
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        
        Gui gui = new Gui(game);
//        ConsoleGui console = new ConsoleGui(game);
        AiPlayerHandler ai = new AiPlayerHandler(game);
        ai.maxDepth = 2;
        
        game.setPlayer(Piece.COLOR_WHITE, ai);
        game.setPlayer(Piece.COLOR_BLACK, gui);
        
        new Thread(game).start();
    }
}
