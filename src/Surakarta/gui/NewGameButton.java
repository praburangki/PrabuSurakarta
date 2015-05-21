package Surakarta.gui;

import Surakarta.logic.AiPlayerHandler;
import Surakarta.logic.Game;
import Surakarta.logic.Piece;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author praburangki
 */
public class NewGameButton implements ActionListener {
    private Gui gui;
    private JFrame f;

    public NewGameButton(Gui gui, JFrame f) {
        this.gui = gui;
        this.f = f;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        f.setVisible(false);
        Game game = new Game();
        gui = new Gui(game);
        AiPlayerHandler ai = new AiPlayerHandler(game);
        ai.maxDepth = Integer.parseInt(JOptionPane.showInputDialog("AI Level"));
        
        game.setPlayer(Piece.COLOR_WHITE, gui);
        game.setPlayer(Piece.COLOR_BLACK, ai);
        
        new Thread(game).start();
    }
    
}
