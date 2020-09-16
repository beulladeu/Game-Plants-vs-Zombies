package main.java.view;

import main.java.model.*;
import main.java.view.StartView;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame{

    public GameFrame() {
        this.setTitle("Plants vs Zombies");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(Constant.WIDTH_FRAME, Constant.HEIGHT_FRAME));
        this.setResizable(false);
        this.add(new StartView(this));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
