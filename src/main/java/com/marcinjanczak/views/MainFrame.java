package com.marcinjanczak.views;

import com.marcinjanczak.model.Mesh3D;
import com.marcinjanczak.utlis.MeshLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    private MenuBar menuBar;
    private Mesh3D mesh;
    private double observerDistance = 5.0;

    private RenderPanel renderPanel;
    private JPanel pointsArea;
    private JPanel transformPanel;
    private JPanel controlPanel;

    public MainFrame() {
        setTitle("Grafika 3D - Projekt");
        setLayout(new GridBagLayout());
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        // Panel do renderowania
        renderPanel = new RenderPanel();

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 9;
        gbc.gridheight = 9;
        gbc.weightx = 0.9;
        gbc.weighty = 0.9;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        add(renderPanel, gbc);


        pointsArea = new JPanel();

        gbc.gridx = 9;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 9;
        gbc.weightx = 0.10;
        gbc.weighty = 0.9;
        add(pointsArea, gbc);
        transformPanel = getTransformPanel();

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 9; // 3/4 szerokości
        gbc.gridheight = 1; // 1/4 wysokości
        gbc.weightx = 0.9;
        gbc.weighty = 0.1;
        add(transformPanel, gbc);



        controlPanel = getControllPanel();
        gbc.gridx = 9;
        gbc.gridy = 9;
        gbc.gridwidth = 1; // 1/4 szerokości
        gbc.gridheight = 1; // 1/4 wysokości
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        add(controlPanel, gbc);

        menuBar = new MenuBar();
        setJMenuBar(menuBar);
    }
    private JPanel getControllPanel(){
        JPanel controlPanel = new JPanel(new GridLayout(0, 3));
        JButton loadButton = new JButton("Wczytaj bryłę");
        loadButton.addActionListener(e -> loadMesh());

        JLabel distLabel = new JLabel("Odległość obserwatora:");
        JTextField distField = new JTextField(String.valueOf(observerDistance));
        JButton distButton = new JButton("Ustaw");


        distButton.addActionListener(e -> {
            try {
                observerDistance = Double.parseDouble(distField.getText());
                renderPanel.setObserverDistance(observerDistance);
                renderPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowa wartość");
            }
        });

        controlPanel.add(loadButton);
        controlPanel.add(distLabel);
        controlPanel.add(distField);
        controlPanel.add(distButton);
//        controlPanel.add(transformPanel);
//        controlPanel.add(translateButton);
//

        return controlPanel;
    }
    private JPanel getTransformPanel(){
        transformPanel = new JPanel(new GridLayout(3, 3));
        transformPanel.add(new JLabel("Przesunięcie:"));
        JTextField txField = new JTextField("0");
        JTextField tyField = new JTextField("0");
        JTextField tzField = new JTextField("0");
        transformPanel.add(txField);
        transformPanel.add(tyField);
        transformPanel.add(tzField);
        JButton translateButton = new JButton("Przesuń");
        translateButton.addActionListener(e -> {
            try {
                double tx = Double.parseDouble(txField.getText());
                double ty = Double.parseDouble(tyField.getText());
                double tz = Double.parseDouble(tzField.getText());
                if (mesh != null) {
//                    mesh.translate(tx, ty, tz);
                    renderPanel.repaint();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowe wartości");
            }
        });
        return transformPanel;
    }

    private void loadMesh() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                mesh = MeshLoader.loadFromFile(fileChooser.getSelectedFile());
                renderPanel.setMesh(mesh);
                renderPanel.setObserverDistance(observerDistance);
                renderPanel.repaint();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Błąd wczytywania pliku");
            }
        }
    }
}
