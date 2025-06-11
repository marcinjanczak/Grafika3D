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


    private JTextArea meshInfoArea;
    private JScrollPane meshInfoScrollPane;

    private RenderPanel renderPanel;
    private JPanel pointsArea;
    private JPanel transformPanel;
    private JPanel controlPanel;


    JButton translateButton;
    JButton scaleButton;
    JButton rotateButton;


    JTextField rotateXfield;
    JTextField rotateYfield;
    JTextField rotateZfield;

    JTextField txField;
    JTextField tyField;
    JTextField tzField;

    JTextField scaleXField;
    JTextField scaleYField;
    JTextField scaleZField;

    JLabel distLabel;
    JTextField distField;
    JButton distButton;

    public MainFrame() {
        setTitle("Grafika 3D - Projekt");
        setLayout(new GridBagLayout());
        setSize(1500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        acctionListeners();
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

        gbc.gridx = 9;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 9;
        gbc.weightx = 0.10;
        gbc.weighty = 0.9;
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

//        controlPanel = getControllPanel();
//        gbc.gridx = 9;
//        gbc.gridy = 9;
//        gbc.gridwidth = 1; // 1/4 szerokości
//        gbc.gridheight = 1; // 1/4 wysokości
//        gbc.weightx = 0.1;
//        gbc.weighty = 0.1;
//        add(controlPanel, gbc);

        menuBar = new MenuBar();
        setJMenuBar(menuBar);
    }

    private JPanel getTransformPanel() {
        transformPanel = new JPanel(new GridLayout(4, 4, 5, 5));

        translateButton = new JButton("Przesuń");
        txField = new JTextField("0");
        tyField = new JTextField("0");
        tzField = new JTextField("0");

        transformPanel.add(translateButton);
        transformPanel.add(txField);
        transformPanel.add(tyField);
        transformPanel.add(tzField);

        scaleButton = new JButton("Skaluj");
        scaleXField = new JTextField("0.0");
        scaleYField = new JTextField("0.0");
        scaleZField = new JTextField("0.0");

        transformPanel.add(scaleButton);
        transformPanel.add(scaleXField);
        transformPanel.add(scaleYField);
        transformPanel.add(scaleZField);

        rotateButton = new JButton("Obróć");
        rotateXfield = new JTextField("0.0");
        rotateYfield = new JTextField("0.0");
        rotateZfield = new JTextField("0.0");

        transformPanel.add(rotateButton);
        transformPanel.add(rotateXfield);
        transformPanel.add(rotateYfield);
        transformPanel.add(rotateZfield);

        distLabel = new JLabel("Odległość obserwatora:");
        distField = new JTextField(String.valueOf(observerDistance));
        distButton = new JButton("Ustaw");

        transformPanel.add(distButton);
        transformPanel.add(distField);
        transformPanel.add(distLabel);
        transformPanel.add(new JLabel(""));


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

    private void acctionListeners() {
        ///  Menu Bar listeners
        menuBar.getLoadItem().addActionListener(e -> loadMesh());
        menuBar.getExitMenuItem().addActionListener(e -> System.exit(0));

        ///  Distance Listener
        distButton.addActionListener(e -> {
            try {
                observerDistance = Double.parseDouble(distField.getText());
                renderPanel.setObserverDistance(observerDistance);
                renderPanel.repaint();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowa wartość");
            }
        });

        ///  Transformacje listeners
        translateButton.addActionListener(e -> {
            try {
                double tx = Double.parseDouble(txField.getText());
                double ty = Double.parseDouble(tyField.getText());
                double tz = Double.parseDouble(tzField.getText());
                if (mesh != null) {
                    mesh.translate(tx, ty, tz);
                    renderPanel.repaint();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowe wartości");
            }
        });


    }
}
