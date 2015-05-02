package ch04.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author praburangki
 */
public class ChangeGameStateButtonActionListener implements ActionListener {
    private Gui gui;

    public ChangeGameStateButtonActionListener(Gui gui) {
        this.gui = gui;
    }
   
    @Override 
    public void actionPerformed(ActionEvent e) {
        this.gui.changeGameState();
    }
}
