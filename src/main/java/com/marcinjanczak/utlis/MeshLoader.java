package com.marcinjanczak.utlis;


import com.marcinjanczak.model.Face;
import com.marcinjanczak.model.Mesh3D;
import com.marcinjanczak.model.Vertex;

import java.io.*;
import java.util.*;

public class MeshLoader {
    public static Mesh3D loadFromFile(File file) throws IOException {
        Mesh3D mesh = new Mesh3D();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        boolean readingVertices = false;
        boolean readingFaces = false;

        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.equals("vertices:")) {
                readingVertices = true;
                readingFaces = false;
                continue;
            } else if (line.equals("faces:")) {
                readingVertices = false;
                readingFaces = true;
                continue;
            }

            if (readingVertices) {
                String[] parts = line.split("\\s+");
                double x = Double.parseDouble(parts[0]);
                double y = Double.parseDouble(parts[1]);
                double z = Double.parseDouble(parts[2]);
                mesh.addVertex(new Vertex(x, y, z));
            } else if (readingFaces) {
                String[] parts = line.split("\\s+");
                List<Integer> indices = new ArrayList<>();
                for (String part : parts) {
                    indices.add(Integer.parseInt(part));
                }
                mesh.addFace(new Face(indices));
            }
        }

        reader.close();
        return mesh;
    }
}