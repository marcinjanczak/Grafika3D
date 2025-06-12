package com.marcinjanczak.model;

public class Matrix4x4 {
    private double[][] m = new double[4][4];

    // Domyślnie tworzymy macierz jednostkową (identity matrix)
    public static Matrix4x4 identity() {
        Matrix4x4 matrix = new Matrix4x4();
        for (int i = 0; i < 4; i++) {
            matrix.m[i][i] = 1;
        }
        return matrix;
    }

    // Translacja - wartości w ostatniej kolumnie
    public static Matrix4x4 translation(double tx, double ty, double tz) {
        Matrix4x4 matrix = identity();
        matrix.m[0][3] = tx; // [wiersz][kolumna]
        matrix.m[1][3] = ty;
        matrix.m[2][3] = tz;
        return matrix;
    }

    // Rotacja wokół osi X
    public static Matrix4x4 rotationX(double angle) {
        Matrix4x4 matrix = identity();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        matrix.m[1][1] = cos;
        matrix.m[1][2] = -sin;
        matrix.m[2][1] = sin;
        matrix.m[2][2] = cos;
        return matrix;
    }

    // Rotacja wokół osi Y
    public static Matrix4x4 rotationY(double angle) {
        Matrix4x4 matrix = identity();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        matrix.m[0][0] = cos;
        matrix.m[0][2] = sin;
        matrix.m[2][0] = -sin;
        matrix.m[2][2] = cos;
        return matrix;
    }

    // Rotacja wokół osi Z
    public static Matrix4x4 rotationZ(double angle) {
        Matrix4x4 matrix = identity();
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        matrix.m[0][0] = cos;
        matrix.m[0][1] = -sin;
        matrix.m[1][0] = sin;
        matrix.m[1][1] = cos;
        return matrix;
    }

    // Skalowanie - macierz diagonalna, pozostaje bez zmian
    public static Matrix4x4 scaling(double sx, double sy, double sz) {
        Matrix4x4 matrix = identity();
        matrix.m[0][0] = sx;
        matrix.m[1][1] = sy;
        matrix.m[2][2] = sz;
        return matrix;
    }
    public Matrix4x4 scale(double sx, double sy, double sz){
        Matrix4x4 scaleMatrix = Matrix4x4.scaling(sx, sy, sz);
        return scaleMatrix.multiply(this);
    }

    // Standardowe mnożenie macierzy (A * B)
    public Matrix4x4 multiply(Matrix4x4 other) {
        Matrix4x4 result = new Matrix4x4();
        for (int i = 0; i < 4; i++) {       // wiersz w macierzy wynikowej
            for (int j = 0; j < 4; j++) {   // kolumna w macierzy wynikowej
                for (int k = 0; k < 4; k++) {
                    // C[i][j] = suma(A[i][k] * B[k][j])
                    result.m[i][j] += this.m[i][k] * other.m[k][j];
                }
            }
        }
        return result;
    }

    // Transformacja wierzchołka (macierz * wektor)
    public Vertex transform(Vertex v) {
        // Zakładamy, że wierzchołek wejściowy to (v.x, v.y, v.z, 1.0)
        double x = m[0][0] * v.x + m[0][1] * v.y + m[0][2] * v.z + m[0][3];
        double y = m[1][0] * v.x + m[1][1] * v.y + m[1][2] * v.z + m[1][3];
        double z = m[2][0] * v.x + m[2][1] * v.y + m[2][2] * v.z + m[2][3];
        double w = m[3][0] * v.x + m[3][1] * v.y + m[3][2] * v.z + m[3][3];

        // Normalizacja przez współrzędną w (ważne dla perspektywy)
        if (w != 0 && w != 1) {
            return new Vertex(x / w, y / w, z / w);
        } else {
            return new Vertex(x, y, z);
        }
    }

    public double get(int row, int col) {
        return m[row][col];
    }
}