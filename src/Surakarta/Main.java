package Surakarta;

import Surakarta.gui.Gui;
import Surakarta.logic.AiPlayerHandler;
import Surakarta.logic.Game;
import Surakarta.logic.Piece;
import javax.swing.JOptionPane;

/**
 *
 * @author praburangki
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        Gui gui = new Gui(game);
        
        AiPlayerHandler ai = new AiPlayerHandler(game);
        ai.maxDepth = Integer.parseInt(JOptionPane.showInputDialog("AI Level"));
        
        game.setPlayer(Piece.COLOR_WHITE, gui);
        game.setPlayer(Piece.COLOR_BLACK, ai);
        
        new Thread(game).start();
    }
}
