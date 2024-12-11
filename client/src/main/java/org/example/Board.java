package org.example;

public class Board {

    Board() {
        initBoard(5);
    }

    private int[][] state;

    private void initBoard(int n) {
        int size = 4 * n - 3;
        state = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < n - 1) {
                    if (j <= size - n && j >= size - n - i) state[i][j] = 4;
                } else if (i < 2 * n - 2) {
                    if (j <= size - n && j >= size - n - i) state[i][j] = 7;
                    else if (j > size - n && j < size - i + n - 1) state[i][j] = 5;
                    else if (j >= n - 1 && j < size - n) state[i][j] = 3;
                } else if (i == size / 2) {
                    if (j >= n - 1 && j <= size - n) state[i][j] = 7;
                } else {
                    if (state[size - i - 1][size - j - 1] == 4) state[i][j] = 1;
                    else if (state[size - i - 1][size - j - 1] == 3) state[i][j] = 6;
                    else if (state[size - i - 1][size - j - 1] == 5) state[i][j] = 2;
                    else state[i][j] = state[size - i - 1][size - j - 1];
                }
            }
        }
    }

    public int[][] getState() {
        return state;
    }

    public void move(int y1, int x1, int y2, int x2) {
        state[y2][x2] = state[y1][x1];
        state[y1][x1] = 7;
    }

}
