package com.marcinjanczak.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ViewControlPanel extends JPanel {
    private JTextField distanceField;
    private JCheckBox perspectiveCheckBox;
    private JCheckBox hiddenLinesCheckBox;
    private RenderPanel renderPanel;

    public ViewControlPanel(RenderPanel renderPanel) {
        this.renderPanel = renderPanel;
        setLayout(new GridLayout(4, 1));
        setBorder(BorderFactory.createTitledBorder("Ustawienia widoku"));

        initComponents();
    }

    private void initComponents() {
        // Odległość obserwatora
        JPanel distancePanel = new JPanel(new FlowLayout());
        distancePanel.add(new JLabel("Odległość obserwatora:"));
        distanceField = new JTextField("5.0", 5);
        distancePanel.add(distanceField);

        JButton applyButton = new JButton("Zastosuj");
        applyButton.addActionListener(this::applyViewSettings);
        distancePanel.add(applyButton);

        add(distancePanel);

        // Tryb rzutowania
        perspectiveCheckBox = new JCheckBox("Rzutowanie perspektywiczne", true);
        add(perspectiveCheckBox);

        // Usuwanie niewidocznych krawędzi
        hiddenLinesCheckBox = new JCheckBox("Usuń niewidoczne krawędzie");
        add(hiddenLinesCheckBox);

        // Przycisk resetu
        JButton resetButton = new JButton("Resetuj widok");
        resetButton.addActionListener(e -> renderPanel.resetView());
        add(resetButton);
    }

    private void applyViewSettings(ActionEvent e) {
        try {
            double distance = Double.parseDouble(distanceField.getText());
            renderPanel.setObserverDistance(distance);
            renderPanel.setPerspectiveProjection(perspectiveCheckBox.isSelected());
            renderPanel.setRemoveHiddenLines(hiddenLinesCheckBox.isSelected());
            renderPanel.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Nieprawidłowa wartość odległości");
        }
    }
}