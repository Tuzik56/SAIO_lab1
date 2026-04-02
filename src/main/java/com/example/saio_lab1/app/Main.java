package com.example.saio_lab1.app;

import com.example.saio_lab1.input.ConsoleInputHandler;
import com.example.saio_lab1.model.*;

public class Main {
    public static void main(String[] args) {

        ConsoleInputHandler input = new ConsoleInputHandler();
        LinearProgram lp = input.read();

        FXMain.setLinearProgram(lp);
        FXMain.main(args);
    }
}