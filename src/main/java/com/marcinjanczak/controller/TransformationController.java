package com.marcinjanczak.controller;

import com.marcinjanczak.model.Matrix4x4;
import com.marcinjanczak.model.Mesh3D;



public class TransformationController {
    private Matrix4x4 transformationMatrix;

    public TransformationController() {
        transformationMatrix = Matrix4x4.identity();
    }

    public void applyTranslation(double tx, double ty, double tz) {
        Matrix4x4 translationMatrix = Matrix4x4.translation(tx, ty, tz);
        transformationMatrix = transformationMatrix.multiply(translationMatrix);
    }

    public void applyRotationX(double angle) {
        Matrix4x4 rotationMatrix = Matrix4x4.rotationX(angle);
        transformationMatrix = transformationMatrix.multiply(rotationMatrix);
    }

    public void applyRotationY(double angle) {
        Matrix4x4 rotationMatrix = Matrix4x4.rotationY(angle);
        transformationMatrix = transformationMatrix.multiply(rotationMatrix);
    }

    public void applyRotationZ(double angle) {
        Matrix4x4 rotationMatrix = Matrix4x4.rotationZ(angle);
        transformationMatrix = transformationMatrix.multiply(rotationMatrix);
    }

    public void applyScaling(double sx, double sy, double sz) {
        Matrix4x4 scalingMatrix = Matrix4x4.scaling(sx, sy, sz);
        transformationMatrix = transformationMatrix.multiply(scalingMatrix);
    }

    public void transformMesh(Mesh3D mesh) {
        mesh.applyTransformation(transformationMatrix);
    }

    public void reset() {
        transformationMatrix = Matrix4x4.identity();
    }
}