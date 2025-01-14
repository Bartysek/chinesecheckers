package org.example;

public class ConsoleBoardVisualizer implements BoardVisualizer {
    public void showBoard(Board board) {
        int[][] s = board.getState();
        int size = s.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < i; j++) {
                System.out.print(" ");

            }
            for (int j = 0; j < size; j++) {
                if (s[i][j] == 0) {
                    System.out.print("  ");
                } else if (s[i][j] == 7) {
                    System.out.print("o ");
                } else {
                    System.out.print(s[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    @Override
    public void yourTurn() {
        System.out.println("It's Your turn!");
    }

    @Override
    public void notYourTurn() {
        System.out.println("Wait for your turn...");
    }
}
