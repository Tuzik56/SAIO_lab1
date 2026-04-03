package com.example.saio_lab1.view;

import javafx.scene.canvas.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import com.example.saio_lab1.model.*;

import java.util.ArrayList;
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

    private double getMinX() {
        return (0 - offsetX) / scale;
    }

    private double getMaxX() {
        return (canvas.getWidth() - offsetX) / scale;
    }

    private double getMinY() {
        return (offsetY - canvas.getHeight()) / scale;
    }

    private double getMaxY() {
        return offsetY / scale;
    }

    public void drawGrid() {

        gc.setStroke(Color.LIGHTGRAY);

        double minX = getMinX();
        double maxX = getMaxX();
        double minY = getMinY();
        double maxY = getMaxY();

        // округляем к шагу сетки
        double startX = Math.floor(minX / gridStep) * gridStep;
        double startY = Math.floor(minY / gridStep) * gridStep;

        for (double x = startX; x <= maxX; x += gridStep) {
            double sx = toScreenX(x);
            gc.strokeLine(sx, 0, sx, canvas.getHeight());
        }

        for (double y = startY; y <= maxY; y += gridStep) {
            double sy = toScreenY(y);
            gc.strokeLine(0, sy, canvas.getWidth(), sy);
        }

        // оси
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);

        gc.strokeLine(0, offsetY, canvas.getWidth(), offsetY);
        gc.strokeLine(offsetX, 0, offsetX, canvas.getHeight());
    }

    public void drawConstraints(List<Constraint> constraints) {

        gc.setStroke(Color.BLUE);

        double minX = getMinX();
        double maxX = getMaxX();

        for (Constraint c : constraints) {

            if (Math.abs(c.b) > 1e-6) {
                // обычная линия
                double y1 = (c.c - c.a * minX) / c.b;
                double y2 = (c.c - c.a * maxX) / c.b;

                gc.strokeLine(
                        toScreenX(minX), toScreenY(y1),
                        toScreenX(maxX), toScreenY(y2)
                );
            } else {
                // вертикальная линия (ВАЖНО!)
                double x = c.c / c.a;

                gc.strokeLine(
                        toScreenX(x), 0,
                        toScreenX(x), canvas.getHeight()
                );
            }
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

        if (points == null || points.size() < 3) return;

        points = sortPoints(new ArrayList<>(points)); // копия!

        double[] x = new double[points.size()];
        double[] y = new double[points.size()];

        for (int i = 0; i < points.size(); i++) {
            x[i] = toScreenX(points.get(i).x);
            y[i] = toScreenY(points.get(i).y);
        }

        gc.setFill(Color.rgb(0, 0, 255, 0.2)); // прозрачный синий
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

        drawRegion(feasible); //

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