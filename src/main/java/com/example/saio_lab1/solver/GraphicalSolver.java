package com.example.saio_lab1.solver;

import com.example.saio_lab1.model.*;
import com.example.saio_lab1.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

public class GraphicalSolver {

    public List<Point> findAllIntersections(List<Constraint> constraints) {  // пересечения

        List<Point> points = new ArrayList<>();

        for (int i = 0; i < constraints.size(); i++) {
            for (int j = i + 1; j < constraints.size(); j++) {

                Constraint c1 = constraints.get(i);
                Constraint c2 = constraints.get(j);

                Point p = MathUtils.intersect(
                        c1.a, c1.b, c1.c,
                        c2.a, c2.b, c2.c
                );

                if (p != null) {
                    points.add(p);
                }
            }
        }

        return points;
    }


    private boolean satisfies(Constraint c, Point p) {  // точка удовлетворяет ограничению

        double left = c.a * p.x + c.b * p.y;
        double right = c.c;

        double eps = 1e-6;

        return switch (c.sign) {
            case LESS_OR_EQUAL -> left <= right + eps;
            case GREATER_OR_EQUAL -> left >= right - eps;
            case EQUAL -> Math.abs(left - right) < eps;
        };
    }


    private boolean isFeasible(Point p, List<Constraint> constraints) {  // точка удовлетворяет всем ограничениям

        // x ≥ 0, y ≥ 0
        if (p.x < -1e-6 || p.y < -1e-6) {
            return false;
        }

        for (Constraint c : constraints) {
            if (!satisfies(c, p)) {
                return false;
            }
        }

        return true;
    }


    public List<Point> filterFeasible(  // какие точки удовлетворяют ограничениям
            List<Point> points,
            List<Constraint> constraints) {

        constraints.add(new Constraint(1, 0, 0, Sign.GREATER_OR_EQUAL)); // x ≥ 0
        constraints.add(new Constraint(0, 1, 0, Sign.GREATER_OR_EQUAL)); // y ≥ 0

        List<Point> feasible = new ArrayList<>();

        for (Point p : points) {
            if (isFeasible(p, constraints)) {
                feasible.add(p);
            }
        }

        return feasible;
    }


    private double valueAt(Point p, ObjectiveFunction obj) {  //значение целевой функции
        return obj.a * p.x + obj.b * p.y;
    }


    public Solution findOptimalSolution(  //оптимальное решение
            List<Point> points,
            ObjectiveFunction obj) {

        if (points.isEmpty()) return null;

        Point best = points.get(0);
        double bestValue = valueAt(best, obj);

        for (Point p : points) {
            double value = valueAt(p, obj);

            if (obj.isMax) {
                if (value > bestValue) {
                    bestValue = value;
                    best = p;
                }
            } else {
                if (value < bestValue) {
                    bestValue = value;
                    best = p;
                }
            }
        }

        return new Solution(best, bestValue);
    }
}