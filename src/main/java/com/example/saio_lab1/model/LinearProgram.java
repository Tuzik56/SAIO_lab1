package com.example.saio_lab1.model;

import java.util.List;

public class LinearProgram {
    public ObjectiveFunction objective;
    public List<Constraint> constraints;

    public LinearProgram(ObjectiveFunction objective, List<Constraint> constraints) {
        this.objective = objective;
        this.constraints = constraints;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(objective.isMax ? "max " : "min ");
        sb.append(objective.a).append("x1 + ").append(objective.b).append("x2\n");

        for (Constraint c : constraints) {
            sb.append(c.a).append("x1 + ")
                    .append(c.b).append("x2 ")
                    .append(signToString(c.sign)).append(" ")
                    .append(c.c).append("\n");
        }

        return sb.toString();
    }

    private String signToString(Sign s) {
        return switch (s) {
            case LESS_OR_EQUAL -> "<=";
            case GREATER_OR_EQUAL -> ">=";
            case EQUAL -> "=";
        };
    }
}