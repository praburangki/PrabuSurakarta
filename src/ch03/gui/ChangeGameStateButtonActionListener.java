/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch03.gui;

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
