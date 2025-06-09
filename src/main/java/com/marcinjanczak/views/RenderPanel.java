package com.marcinjanczak.views;


import com.marcinjanczak.model.Face;
import com.marcinjanczak.model.Mesh3D;
import com.marcinjanczak.model.Vertex;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class RenderPanel extends JPanel {
    private Mesh3D mesh;
    private double observerDistance;

    public void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

    public void setObserverDistance(double distance) {
        this.observerDistance = distance;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mesh == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int scale = 100;

        // Rysowanie osi współrzędnych
        g2d.setColor(Color.RED);
        g2d.drawLine(centerX, centerY, centerX + scale, centerY);
        g2d.setColor(Color.GREEN);
        g2d.drawLine(centerX, centerY, centerX, centerY - scale);
        g2d.setColor(Color.BLUE);
        g2d.drawLine(centerX, centerY, centerX, centerY + scale);

        // Rysowanie bryły
        g2d.setColor(Color.BLACK);
        for (Face face : mesh.getFaces()) {
            if (isFaceVisible(face, mesh.getVertices(), observerDistance)) {
                List<Point> projectedPoints = new ArrayList<>();
                for (int vertexIndex : face.getVertexIndices()) {
                    Vertex v = mesh.getVertices().get(vertexIndex);
                    Point2D p = projectVertex(v, observerDistance);
                    projectedPoints.add(new Point(
                            (int)(centerX + p.getX() * scale),
                            (int)(centerY - p.getY() * scale)
                    ));
                }

                // Rysowanie ściany
                for (int i = 0; i < projectedPoints.size(); i++) {
                    Point p1 = projectedPoints.get(i);
                    Point p2 = projectedPoints.get((i + 1) % projectedPoints.size());
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }

    private boolean isFaceVisible(Face face, List<Vertex> vertices, double d) {
        if (face.getVertexIndices().size() < 3) return false;

        Vertex v1 = vertices.get(face.getVertexIndices().get(0));
        Vertex v2 = vertices.get(face.getVertexIndices().get(1));
        Vertex v3 = vertices.get(face.getVertexIndices().get(2));

        // Wektory krawędzi
        double[] edge1 = {v2.x - v1.x, v2.y - v1.y, v2.z - v1.z};
        double[] edge2 = {v3.x - v1.x, v3.y - v1.y, v3.z - v1.z};

        // Iloczyn wektorowy (normalna)
        double[] normal = {
                edge1[1] * edge2[2] - edge1[2] * edge2[1],
                edge1[2] * edge2[0] - edge1[0] * edge2[2],
                edge1[0] * edge2[1] - edge1[1] * edge2[0]
        };

        // Środek ściany
        double centerX = 0, centerY = 0, centerZ = 0;
        for (int i : face.getVertexIndices()) {
            Vertex v = vertices.get(i);
            centerX += v.x;
            centerY += v.y;
            centerZ += v.z;
        }
        centerX /= face.getVertexIndices().size();
        centerY /= face.getVertexIndices().size();
        centerZ /= face.getVertexIndices().size();

        // Wektor kierunku patrzenia
        double[] viewDir = {0 - centerX, 0 - centerY, -d - centerZ};

        // Iloczyn skalarny
        double dot = normal[0] * viewDir[0] + normal[1] * viewDir[1] + normal[2] * viewDir[2];

        return dot > 0;
    }

    private Point2D projectVertex(Vertex v, double d) {
        double x = v.x * d / (d + v.z);
        double y = v.y * d / (d + v.z);
        return new Point2D.Double(x, y);
    }
}