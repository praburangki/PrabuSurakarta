package Applet;

import java.awt.Choice;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Label;

public class Surakarta extends java.applet.Applet {
    Player player;
    Graphics g = null;
    
    @Override
    public void init() {
        setLayout(new java.awt.BorderLayout());
        java.awt.Panel panel = new java.awt.Panel();
        panel.setLayout(new java.awt.BorderLayout(0, 12));
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
        choice.select(Game.difficulty);
        panel.add("East", choice);

        this.player = new Player();
        Game.init(this);
        this.g = Game.board.img.getGraphics();
    }
    
    @Override
    public void start() {
        Game.setPlayer(false);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(Game.board.img, 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public boolean mouseDown(Event evt, int x, int y) {
        if (this.player != null) {
            this.player.mouseDown(new java.awt.Point(x, y));
        }
        return true;
    }

    @Override
    public boolean mouseUp(Event evt, int x, int y) {
        if (this.player != null) {
            this.player.mouseUp(new java.awt.Point(x, y));
        }
        return true;
    }

    @Override
    public boolean action(Event evt, Object obj) {
        if ((evt.target instanceof Choice)) {
            Game.difficulty = ((Choice) evt.target).getSelectedIndex();
        }
        return true;
    }

    public void playSound(String paramString) {
        play(getCodeBase(), paramString);
    }
}