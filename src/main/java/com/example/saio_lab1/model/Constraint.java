package com.example.saio_lab1.model;

public class Constraint {
    public double a, b, c;
    public Sign sign;

    public Constraint(double a, double b, double c, Sign sign) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.sign = sign;
    }
}