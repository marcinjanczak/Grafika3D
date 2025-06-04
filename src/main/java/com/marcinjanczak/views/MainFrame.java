package com.marcinjanczak.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private final int WINDOW_WIDTH = 1000;
    private final int WINDOW_HEIGHT = 800;

    private final MenuBar menuBar;

    public MainFrame(){
        super("Grafika 3D");
        setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuBar = new MenuBar();

        setJMenuBar(menuBar);

        setMenuBarListeners();


        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void setMenuBarListeners(){
        menuBar.getExitMenuItem().addActionListener(e -> System.exit(0));
    }
}
