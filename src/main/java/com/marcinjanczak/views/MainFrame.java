package com.marcinjanczak.views;

import com.marcinjanczak.controller.TransformationController;
import com.marcinjanczak.model.Face;
import com.marcinjanczak.model.Matrix4x4;
import com.marcinjanczak.model.Mesh3D;
import com.marcinjanczak.model.Vertex;
import com.marcinjanczak.utlis.MeshLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {
    private MenuBar menuBar;
    private Mesh3D mesh;
    private TransformationController transformationController;
    private double observerDistance = 500;

    private JTextArea meshInfoArea;

    private RenderPanel renderPanel;
    private JPanel infoPanel;
    private JPanel transformPanel;

    private JPanel matrixPanel;
    private JTextArea matrixTextArea;

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
        transformationController = new TransformationController();
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


        infoPanel = getInfoPanel();
        gbc.gridx = 9;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 9;
        gbc.weightx = 0.10;
        gbc.weighty = 0.9;
        add(infoPanel, gbc);

        transformPanel = getTransformPanel();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 9; // 3/4 szerokości
        gbc.gridheight = 1; // 1/4 wysokości
        gbc.weightx = 0.9;
        gbc.weighty = 0.1;
        add(transformPanel, gbc);

        matrixPanel = getMatrixPanel();
        gbc.gridx = 9;
        gbc.gridy = 9;
        gbc.gridwidth = 1; // 1/4 szerokości
        gbc.gridheight = 1; // 1/4 wysokości
        gbc.weightx = 0.1;
        gbc.weighty = 0.1;
        add(matrixPanel, gbc);

        menuBar = new MenuBar();
        setJMenuBar(menuBar);
    }

    private JPanel getInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Informacje o bryle"));
        meshInfoArea = new JTextArea();
        meshInfoArea.setEditable(false);
        meshInfoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(meshInfoArea);
        infoPanel.add(scrollPane, BorderLayout.CENTER);

        return infoPanel;
    }

    public void updateMeshInfo(Mesh3D mesh) {
        if (mesh == null) {
            meshInfoArea.setText("Brak wczytanej bryły");
            return;
        }
        StringBuilder sb = new StringBuilder();

        // Nagłówek
        sb.append("=== Wierzchołki ===\n");

        // Wierzchołki
        List<Vertex> vertices = mesh.getVertices();
        for (int i = 0; i < vertices.size(); i++) {
            Vertex v = vertices.get(i);
            sb.append(String.format("%2d: [%6.2f, %6.2f, %6.2f]\n",
                    i, v.x, v.y, v.z));
        }

        // Separator
        sb.append("\n=== Ściany ===\n");

        // Ściany
        List<Face> faces = mesh.getFaces();
        for (int i = 0; i < faces.size(); i++) {
            Face f = faces.get(i);
            sb.append(String.format("%2d: ", i));
            for (int vertexIndex : f.getVertexIndices()) {
                sb.append(String.format("%2d ", vertexIndex));
            }
            sb.append("\n");
        }

        meshInfoArea.setText(sb.toString());
    }

    private JPanel getTransformPanel() {
        transformPanel = new JPanel(new GridLayout(4, 4, 5, 5));
        transformPanel.setBorder(BorderFactory.createTitledBorder("Transformacje"));

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

    private JPanel getMatrixPanel() {
        JPanel panel = new JPanel(new GridLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Macierz przekształceń"));

        matrixTextArea = new JTextArea();
        matrixTextArea.setEditable(false);
        matrixTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(matrixTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    //
    private void updateMatrixDisplay(Matrix4x4 matrix) {
        if (matrix == null) {
            matrixTextArea.setText("Brak macierzy przekształceń");
            return;
        }

        StringBuilder sb = new StringBuilder();

        // Nagłówek
        sb.append("Aktualna macierz przekształceń:\n\n");

        // Wiersze macierzy
        for (int i = 0; i < 4; i++) {
            sb.append("[ ");
            for (int j = 0; j < 4; j++) {
                sb.append(String.format("%8.4f", matrix.get(i, j)));
                if (j < 3) sb.append(", ");
            }
            sb.append(" ]\n");
        }

        matrixTextArea.setText(sb.toString());

    }

    private void loadMesh() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                mesh = MeshLoader.loadFromFile(fileChooser.getSelectedFile());
                renderPanel.setMesh(mesh);
                updateMeshInfo(mesh);
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
                    transformationController.applyTranslation(tx,ty,tz);
                    mesh.applyTransformation(transformationController.getTransformationMatrix());
                    updateMatrixDisplay(transformationController.getTransformationMatrix());
                    renderPanel.repaint();
                    updateMeshInfo(mesh);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowe wartości");
            }
        });
        scaleButton.addActionListener(e -> {
            try {
                double sx = Double.parseDouble(scaleXField.getText());
                double sy = Double.parseDouble(scaleYField.getText());
                double sz = Double.parseDouble(scaleZField.getText());

                if (mesh != null) {
                    transformationController.applyScaling(sx, sy, sz);
                    mesh.applyTransformation(transformationController.getTransformationMatrix());
                    updateMatrixDisplay(transformationController.getTransformationMatrix());
                    renderPanel.repaint();
                    updateMeshInfo(mesh);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Nieprawidłowe wartości skalowania");
            }
        });
        rotateButton.addActionListener(e -> {
            try {
                double rx = Math.toRadians(Double.parseDouble(rotateXfield.getText()));
                double ry = Math.toRadians(Double.parseDouble(rotateYfield.getText()));
                double rz = Math.toRadians(Double.parseDouble(rotateZfield.getText()));

                if (mesh != null) {
                    transformationController.applyRotation(rx,ry,rz);
                    mesh.applyTransformation(transformationController.getTransformationMatrix());
                    updateMatrixDisplay(transformationController.getTransformationMatrix());
                    renderPanel.repaint();
                    updateMeshInfo(mesh);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,"Nieprawidłowe wartości rotacji.");
            }
        });
    }
}
