/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial;

import artificial.ai.AiPlayerHandler;
import artificial.gui.Gui;
import artificial.logic.Game;
import artificial.logic.Piece;

/**
 *
 * @author praburangki
 */
public class Main {
    public static void main(String[] args) {
        // create the game
        Game game = new Game();
        
        // create the players
        Gui gui = new Gui(game);
        
        AiPlayerHandler ai = new AiPlayerHandler(game);
        
        // set strength of ai
        ai.maxDepth = 1;
        
        // attach the players to the game
        game.setPlayer(Piece.COLOR_WHITE, ai);
        game.setPlayer(Piece.COLOR_BLACK, gui);
        
        // start the game
        new Thread(game).start();
    }
}
