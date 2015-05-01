package Surakarta;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Panel;

/**
 * @author praburangki
 */
public class Gui extends Applet {
    Graphics g;
    
    @Override
    public void init() {
        setLayout(new BorderLayout());
        Panel panel = new Panel();
        panel.setLayout(new BorderLayout(0, 12));
        add("East", panel);
        
        Label label = new Label("Level of difficulty");
        label.setAlignment(1);
        panel.add("North", label);

        Choice choice = new Choice();
        choice.addItem("Idiot");
        choice.addItem("Beginner");
        choice.addItem("Smart");
        choice.addItem("Intermediate");
        choice.addItem("DeepBlue");
//        choice.select(Game.difficulty);
        panel.add("East", choice);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(Game.boardGui.image, 0, 0, this);
    }
}
