package com.example.saio_lab1.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.example.saio_lab1.model.*;
import com.example.saio_lab1.solver.GraphicalSolver;
import com.example.saio_lab1.view.GraphPane;

import java.util.List;

public class FXMain extends Application {

    private static LinearProgram lp;

    public static void setLinearProgram(LinearProgram program) {
        lp = program;
    }

    @Override
    public void start(Stage stage) {

        GraphicalSolver solver = new GraphicalSolver();

        List<Point> all = solver.findAllIntersections(lp.constraints);
        List<Point> feasible = solver.filterFeasible(all, lp.constraints);
        Solution sol = solver.findOptimalSolution(feasible, lp.objective);

        GraphPane pane = new GraphPane();
        pane.drawAll(lp.constraints, feasible, sol.point);

        Scene scene = new Scene(pane, 600, 600);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {

                case PLUS:
                case EQUALS: // часто + это Shift + =
                    pane.setScale(pane.getScale() * 1.2);
                    break;

                case MINUS:
                    pane.setScale(pane.getScale() / 1.2);
                    break;
            }
        });

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {

                case PLUS:
                case EQUALS:
                    pane.setScale(pane.getScale() * 1.2);
                    break;

                case MINUS:
                    pane.setScale(pane.getScale() / 1.2);
                    break;

                case UP: // увеличить шаг сетки
                    pane.setGridStep(pane.getGridStep() * 1.5);
                    break;

                case DOWN: // уменьшить шаг сетки
                    pane.setGridStep(pane.getGridStep() / 1.5);
                    break;
            }
        });

        stage.setTitle("Линейное программирование");
        stage.setScene(scene);
        stage.show();
        pane.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}