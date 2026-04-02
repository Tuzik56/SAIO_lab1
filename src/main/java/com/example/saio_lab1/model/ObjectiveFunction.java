package com.example.saio_lab1.model;

public class ObjectiveFunction {
    public double a, b;
    public boolean isMax;

    public ObjectiveFunction(double a, double b, boolean isMax) {
        this.a = a;
        this.b = b;
        this.isMax = isMax;
    }
}