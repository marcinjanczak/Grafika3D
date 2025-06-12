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
    public void applyRotation(double rx, double ry, double rz) {
        if (rx != 0) applyRotationX(rx);
        if (ry != 0) applyRotationY(ry);
        if (rz != 0) applyRotationZ(rz);
    }

    public void applyRotationX(double angle) {
        transformationMatrix = transformationMatrix.multiply(Matrix4x4.rotationX(angle));
    }

    public void applyRotationY(double angle) {
        transformationMatrix = transformationMatrix.multiply(Matrix4x4.rotationY(angle));
    }

    public void applyRotationZ(double angle) {
        transformationMatrix = transformationMatrix.multiply(Matrix4x4.rotationZ(angle));
    }

    public void applyScaling(double sx, double sy, double sz) {
        Matrix4x4 scalingMatrix = Matrix4x4.identity().scale(sx, sy, sz);
        transformationMatrix = transformationMatrix.multiply(scalingMatrix);
    }

    public void transformMesh(Mesh3D mesh) {
        mesh.applyTransformation(transformationMatrix);
    }

    public void reset() {
        transformationMatrix = Matrix4x4.identity();
    }

    public Matrix4x4 getTransformationMatrix() {
        return transformationMatrix;
    }
}