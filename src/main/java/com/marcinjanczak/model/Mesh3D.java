package com.marcinjanczak.model;

import java.util.ArrayList;
import java.util.List;

public class Mesh3D {
    private List<Vertex> vertices;
    private List<Face> faces;

    public Mesh3D() {
        vertices = new ArrayList<>();
        faces = new ArrayList<>();
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    public void addFace(Face f) {
        faces.add(f);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    public List<Face> getFaces() {
        return faces;
    }

    public void translate(double tx, double ty, double tz) {
        for (Vertex v : vertices) {
            v.x += tx;
            v.y += ty;
            v.z += tz;
        }
    }

    public void rotateX(double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        for (Vertex v : vertices) {
            double y = v.y * cos - v.z * sin;
            double z = v.y * sin + v.z * cos;
            v.y = y;
            v.z = z;
        }
    }

    public void scale(double sx, double sy, double sz) {
        for (Vertex v : vertices) {
            v.x *= sx;
            v.y *= sy;
            v.z *= sz;
        }
    }
}