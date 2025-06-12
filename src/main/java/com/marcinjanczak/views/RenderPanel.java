package com.marcinjanczak.views;

import com.marcinjanczak.model.Face;
import com.marcinjanczak.model.Mesh3D;
import com.marcinjanczak.model.Vertex;
import com.marcinjanczak.utlis.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class RenderPanel extends JPanel {
    private Mesh3D mesh;
    private double observerDistance = 5.0;
    private boolean perspectiveProjection = true;
    private boolean removeHiddenLines = false;

    public void setMesh(Mesh3D mesh) {
        this.mesh = mesh;
    }

    public void setObserverDistance(double distance) {
        this.observerDistance = distance;
    }

    public void setPerspectiveProjection(boolean enabled) {
        this.perspectiveProjection = enabled;
    }

    public void setRemoveHiddenLines(boolean enabled) {
        this.removeHiddenLines = enabled;
    }

    public void resetView() {
        if (mesh != null) {
            mesh.resetTransformation();
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (mesh == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int scale = 1;

        drawCoordinateSystem(g2d, centerX, centerY, scale);
        drawMesh(g2d, centerX, centerY, scale);
    }

    private void drawCoordinateSystem(Graphics2D g2d, int centerX, int centerY, int scale) {
        g2d.setColor(Color.RED);
        g2d.drawLine(centerX, centerY, centerX + scale, centerY);
        g2d.drawString("X", centerX + scale + 5, centerY);

        g2d.setColor(Color.GREEN);
        g2d.drawLine(centerX, centerY, centerX, centerY - scale);
        g2d.drawString("Y", centerX, centerY - scale - 5);

        g2d.setColor(Color.BLUE);
        g2d.drawLine(centerX, centerY, centerX, centerY + scale);
        g2d.drawString("Z", centerX, centerY + scale + 15);
    }

    private void drawMesh(Graphics2D g2d, int centerX, int centerY, int scale) {
        g2d.setColor(Color.BLACK);

        for (Face face : mesh.getFaces()) {
            if (removeHiddenLines && !isFaceVisible(face)) {
                continue;
            }

            Polygon polygon = new Polygon();
            for (int vertexIndex : face.getVertexIndices()) {
                Vertex v = mesh.getVertices().get(vertexIndex);
                Point2D p = projectVertex(v);
                polygon.addPoint(
                        (int)(centerX + p.getX() * scale),
                        (int)(centerY - p.getY() * scale)
                );
            }

            g2d.draw(polygon);
        }
    }

    private Point2D projectVertex(Vertex v) {
        if (perspectiveProjection) {
            double x = v.x * observerDistance / (observerDistance + v.z);
            double y = v.y * observerDistance / (observerDistance + v.z);
            return new Point2D.Double(x, y);
        } else {
            return new Point2D.Double(v.x, v.y);
        }
    }

    private boolean isFaceVisible(Face face) {
        if (face.getVertexIndices().size() < 3) return false;

        Vertex v1 = mesh.getVertices().get(face.getVertexIndices().get(0));
        Vertex v2 = mesh.getVertices().get(face.getVertexIndices().get(1));
        Vertex v3 = mesh.getVertices().get(face.getVertexIndices().get(2));

        Vector3 normal = calculateNormal(v1, v2, v3);
        Vector3 viewDir = calculateViewDirection(face);

        return normal.dot(viewDir) > 0;
    }

    private Vector3 calculateNormal(Vertex v1, Vertex v2, Vertex v3) {
        Vector3 edge1 = new Vector3(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
        Vector3 edge2 = new Vector3(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
        return edge1.cross(edge2);
    }

    private Vector3 calculateViewDirection(Face face) {
        Vertex center = calculateFaceCenter(face);
        return new Vector3(-center.x, -center.y, -observerDistance - center.z);
    }

    private Vertex calculateFaceCenter(Face face) {
        double x = 0, y = 0, z = 0;
        for (int i : face.getVertexIndices()) {
            Vertex v = mesh.getVertices().get(i);
            x += v.x;
            y += v.y;
            z += v.z;
        }
        int count = face.getVertexIndices().size();
        return new Vertex(x/count, y/count, z/count);
    }
}