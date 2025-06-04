package com.marcinjanczak;

import com.marcinjanczak.views.MainFrame;

import javax.swing.*;

public class Start {
    public static void main(String[] args) {
        System.out.printf("Aplikacja 3-D");
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
