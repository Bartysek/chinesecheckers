package org.example;

import java.util.Arrays;

public class ConsoleBoardVisualizer implements BoardVisualizer{
    public void showBoard(Board board) {
        // TODO ladne wyswietlenie planszy w konsoli, na razie jest tylko wydrukowanie tablicy intów
        System.out.println(Arrays.deepToString(board.getState()));
    }
}
