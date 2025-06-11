package com.marcinjanczak.utlis;

public class Vector3 {
    public double x, y, z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double dot(Vector3 other) {
        return x*other.x + y*other.y + z*other.z;
    }

    public Vector3 cross(Vector3 other) {
        return new Vector3(
                y*other.z - z*other.y,
                z*other.x - x*other.z,
                x*other.y - y*other.x
        );
    }

    // Inne metody: normalize(), length() itp.
}
