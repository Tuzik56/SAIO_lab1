package com.example.saio_lab1.app;

import com.example.saio_lab1.input.ConsoleInputHandler;
import com.example.saio_lab1.model.*;
import com.example.saio_lab1.solver.GraphicalSolver;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        ConsoleInputHandler input = new ConsoleInputHandler();
        LinearProgram lp = input.read();

        System.out.println("Задача успешно считана!");
        System.out.println(lp);

        GraphicalSolver solver = new GraphicalSolver();

        List<Point> all = solver.findAllIntersections(lp.constraints);
        List<Point> feasible = solver.filterFeasible(all, lp.constraints);

        Solution sol = solver.findOptimalSolution(feasible, lp.objective);

        System.out.println("\nОптимальное решение:");
        System.out.println("Точка: " + sol.point);
        System.out.println("Значение: " + sol.value);
    }
}