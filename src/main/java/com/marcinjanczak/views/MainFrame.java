package com.marcinjanczak.views;

import com.marcinjanczak.model.Mesh3D;
import com.marcinjanczak.utlis.MeshLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    private MenuBar menuBar;
    private Mesh3D mesh;
    private RenderPanel renderPanel;
    private double observerDistance = 5.0;

    public MainFrame() {
        setTitle("Grafika 3D - Projekt");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // Panel do renderowania
        renderPanel = new RenderPanel();
        add(renderPanel, BorderLayout.CENTER);

        menuBar = new MenuBar();

        // Panel kontrolny
        JPanel controlPanel = new JPanel(new GridLayout(0, 2));

        // Przyciski i kontrolki
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

        // Transformacje
        JPanel transformPanel = new JPanel(new GridLayout(3, 3));

        // Przesunięcie
        transformPanel.add(new JLabel("Przesunięcie:"));
        JTextField txField = new JTextField("0");
        JTextField tyField = new JTextField("0");
        JTextField tzField = new JTextField("0");
        transformPanel.add(txField);
        transformPanel.add(tyField);
        transformPanel.add(tzField);

        JButton translateButton = new JButton("Przesuń");
//        translateButton.addActionListener(e -> {
//            try {
//                double tx = Double.parseDouble(txField.getText());
//                double ty = Double.parseDouble(tyField.getText());
//                double tz = Double.parseDouble(tzField.getText());
//                if (mesh != null) {
//                    mesh.translate(tx, ty, tz);
//                    renderPanel.repaint();
//                }
//            } catch (NumberFormatException ex) {
//                JOptionPane.showMessageDialog(this, "Nieprawidłowe wartości");
//            }
//        });

        // Dodawanie komponentów
        controlPanel.add(loadButton);
        controlPanel.add(distLabel);
        controlPanel.add(distField);
        controlPanel.add(distButton);
        controlPanel.add(transformPanel);
        controlPanel.add(translateButton);

        add(controlPanel, BorderLayout.SOUTH);

        setJMenuBar(menuBar);
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
