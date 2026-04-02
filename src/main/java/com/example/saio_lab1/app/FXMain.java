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

        stage.setTitle("Линейное программирование");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}