package com.example.saio_lab1.util;

import com.example.saio_lab1.model.Point;

public class MathUtils {

    public static Point intersect(
            double a1, double b1, double c1,
            double a2, double b2, double c2) {

        double D = a1 * b2 - a2 * b1;

        if (Math.abs(D) < 1e-9) {
            return null; // параллельные
        }

        double Dx = c1 * b2 - c2 * b1;
        double Dy = a1 * c2 - a2 * c1;

        return new Point(Dx / D, Dy / D);
    }
}