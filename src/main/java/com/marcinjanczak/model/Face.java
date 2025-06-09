package com.marcinjanczak.model;

import java.util.List;

public class Face {
    private List<Integer> vertexIndices;

    public Face(List<Integer> vertexIndices) {
        this.vertexIndices = vertexIndices;
    }

    public List<Integer> getVertexIndices() {
        return vertexIndices;
    }
}