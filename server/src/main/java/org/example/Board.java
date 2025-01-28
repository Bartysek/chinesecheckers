package org.example;

/**
 * class representing the playing board
 */
public class Board {

    /**
     * constructor.
     */
    public Board() {
        initBoard(3);
    }

    private int[][] state;
    private int hexagonSide;

    /**
     * set up the board of any size
     * @param n length of an edge of the central hexagon
     */
    private void initBoard(int n) {
        hexagonSide = n;
        int size = 4 * n - 3;
        state = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i < n - 1) {
                    if (j <= size - n && j >= size - n - i) state[i][j] = 4;
                } else if (i < 2 * n - 2) {
                    if (j <= size - n && j >= size - n - i) state[i][j] = 7;
                    else if (j > size - n && j < size - i + n - 1) state[i][j] = 5;
                    else if (j >= n-1 && j < size-n) state[i][j] = 3;
                } else if (i == size/2) {
                    if (j >= n-1 && j <= size - n) state[i][j] = 7;
                } else {
                    if (state[size-i-1][size-j-1] == 4) state[i][j] = 1;
                    else if (state[size-i-1][size-j-1] == 3) state[i][j] = 6;
                    else if (state[size-i-1][size-j-1] == 5) state[i][j] = 2;
                    else state[i][j] = state[size-i-1][size-j-1];
                }
            }
        }
    }

    /**
     * gets the size of the board
     * @return length of a side of the central hexagon
     */
    public int getHexagonSide() {
        return hexagonSide;
    }

    /**
     * get the current state of the board
     * @return the exact state of the board at the moment
     */
    public int[][] getState() {
        return state;
    }

    /**
     * set the current state of the board
     * @param state the exact state to be set to
     */
    public void setState(int[][] state) {
        this.state = state;
    }

    /**
     * remove a piece
     * @param x coords
     * @param y coords
     */
    public void remove(int x, int y) {
        state[y][x] = 7;
    }

    /**
     * change a tile into a piece
     * @param x coords
     * @param y coords
     * @param piece piece number to set the tile to
     */
    public void add(int x, int y, int piece) {
        state[y][x] = piece;
    }

}
