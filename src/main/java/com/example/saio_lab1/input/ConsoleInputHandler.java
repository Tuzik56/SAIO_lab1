package com.example.saio_lab1.input;

import com.example.saio_lab1.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleInputHandler {

    private final Scanner scanner = new Scanner(System.in);

    public LinearProgram read() {

        System.out.print("Введите тип задачи (max/min): ");
        boolean isMax = scanner.next().equalsIgnoreCase("max");

        System.out.println("Введите коэффициенты целевой функции:");
        System.out.print("a (при x1): ");
        double a = scanner.nextDouble();

        System.out.print("b (при x2): ");
        double b = scanner.nextDouble();

        ObjectiveFunction objective = new ObjectiveFunction(a, b, isMax);

        System.out.print("Введите количество ограничений: ");
        int n = scanner.nextInt();

        List<Constraint> constraints = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("\nОграничение " + (i + 1) + ":");

            System.out.print("a: ");
            double ca = scanner.nextDouble();

            System.out.print("b: ");
            double cb = scanner.nextDouble();

            System.out.print("знак (<=, >=, =): ");
            String signStr = scanner.next();

            System.out.print("c: ");
            double cc = scanner.nextDouble();

            Sign sign = parseSign(signStr);

            constraints.add(new Constraint(ca, cb, cc, sign));
        }

        return new LinearProgram(objective, constraints);
    }

    private Sign parseSign(String s) {
        return switch (s) {
            case "<=" -> Sign.LESS_OR_EQUAL;
            case ">=" -> Sign.GREATER_OR_EQUAL;
            case "=" -> Sign.EQUAL;
            default -> throw new IllegalArgumentException("Неверный знак: " + s);
        };
    }
}