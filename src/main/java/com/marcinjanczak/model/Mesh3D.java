package com.marcinjanczak.model;

import java.util.ArrayList;
import java.util.List;

public class Mesh3D {
    private List<Vertex> vertices;
    private List<Vertex> originalVertices;
    private List<Face> faces;
    private Matrix4x4 transformationMatrix;

    public Mesh3D() {
        vertices = new ArrayList<>();
        originalVertices = new ArrayList<>();
        faces = new ArrayList<>();
        transformationMatrix = Matrix4x4.identity();
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
        originalVertices.add(new Vertex(v.x, v.y, v.z));
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

    public void applyTransformation(Matrix4x4 matrix) {
        transformationMatrix = matrix;
        updateVertices();
    }

    public void resetTransformation() {
        transformationMatrix = Matrix4x4.identity();
        vertices.clear();
        for (Vertex v : originalVertices) {
            vertices.add(new Vertex(v.x, v.y, v.z));
        }
    }

    private void updateVertices() {
        for (int i = 0; i < vertices.size(); i++) {
            Vertex original = originalVertices.get(i);
            Vertex transformed = transformationMatrix.transform(original);
            vertices.set(i, transformed);
        }
    }
}