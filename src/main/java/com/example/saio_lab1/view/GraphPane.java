package com.example.saio_lab1.view;

import javafx.scene.canvas.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import com.example.saio_lab1.model.*;

import java.util.List;

public class GraphPane extends Pane {

    private Canvas canvas = new Canvas(600, 600);
    private GraphicsContext gc = canvas.getGraphicsContext2D();

    private double scale = 50; // пикселей на 1 единицу
    private double gridStep = 1.0; // шаг сетки в координатах
    private double offsetX = 300;
    private double offsetY = 300;

    private List<Constraint> constraints;
    private List<Point> feasible;
    private Point optimal;

    public GraphPane() {
        getChildren().add(canvas);
    }

    private double toScreenX(double x) {
        return offsetX + x * scale;
    }

    private double toScreenY(double y) {
        return offsetY - y * scale;
    }

    public void drawGrid() {

        gc.setStroke(Color.LIGHTGRAY);
        gc.setLineWidth(1);

        double maxCoord = 20; // сколько рисуем в обе стороны

        for (double i = -maxCoord; i <= maxCoord; i += gridStep) {

            double x = toScreenX(i);
            double y = toScreenY(i);

            // вертикальные линии
            gc.strokeLine(x, 0, x, canvas.getHeight());

            // горизонтальные линии
            gc.strokeLine(0, y, canvas.getWidth(), y);
        }

        // оси
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        gc.strokeLine(0, offsetY, canvas.getWidth(), offsetY); // X
        gc.strokeLine(offsetX, 0, offsetX, canvas.getHeight()); // Y
    }

    public void drawConstraints(List<Constraint> constraints) {

        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1.5);

        for (Constraint c : constraints) {

            double x1 = -10;
            double y1 = (c.c - c.a * x1) / c.b;

            double x2 = 10;
            double y2 = (c.c - c.a * x2) / c.b;

            gc.strokeLine(
                    toScreenX(x1), toScreenY(y1),
                    toScreenX(x2), toScreenY(y2)
            );
        }
    }

    public void drawPoints(List<Point> points) {

        gc.setFill(Color.GREEN);

        for (Point p : points) {
            double x = toScreenX(p.x);
            double y = toScreenY(p.y);

            gc.fillOval(x - 4, y - 4, 8, 8);
        }
    }

    public void drawOptimal(Point p) {

        gc.setFill(Color.RED);

        double x = toScreenX(p.x);
        double y = toScreenY(p.y);

        gc.fillOval(x - 6, y - 6, 12, 12);
    }

    private List<Point> sortPoints(List<Point> points) {

        double centerX = 0, centerY = 0;

        for (Point p : points) {
            centerX += p.x;
            centerY += p.y;
        }

        centerX /= points.size();
        centerY /= points.size();

        double finalCenterY = centerY;
        double finalCenterX = centerX;

        points.sort((p1, p2) -> {
            double angle1 = Math.atan2(p1.y - finalCenterY, p1.x - finalCenterX);
            double angle2 = Math.atan2(p2.y - finalCenterY, p2.x - finalCenterX);
            return Double.compare(angle1, angle2);
        });

        return points;
    }

    public void drawRegion(List<Point> points) {

        points = sortPoints(points);

        double[] x = new double[points.size()];
        double[] y = new double[points.size()];

        for (int i = 0; i < points.size(); i++) {
            x[i] = toScreenX(points.get(i).x);
            y[i] = toScreenY(points.get(i).y);
        }

        gc.setFill(Color.rgb(0, 0, 255, 0.2));
        gc.fillPolygon(x, y, points.size());
    }

    public void drawAll(
            List<Constraint> constraints,
            List<Point> feasible,
            Point optimal) {

        this.constraints = constraints;
        this.feasible = feasible;
        this.optimal = optimal;

        redraw();
    }

    private void redraw() {
        gc.clearRect(0, 0, 600, 600);

        drawGrid();
        drawConstraints(constraints);
        drawRegion(feasible);
        drawPoints(feasible);

        if (optimal != null) {
            drawOptimal(optimal);
        }
    }

    public void setScale(double scale) {

        if (scale < 10) scale = 10;
        if (scale > 500) scale = 500;

        this.scale = scale;
        redraw();
    }

    public double getScale() {
        return scale;
    }

    public void setGridStep(double step) {

        if (step < 0.1) step = 0.1;
        if (step > 10) step = 10;

        this.gridStep = step;
        redraw();
    }

    public double getGridStep() {
        return gridStep;
    }
}