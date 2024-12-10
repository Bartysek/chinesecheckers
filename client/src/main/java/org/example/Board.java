package org.example;

public class Board {

    private BoardVisualizer bv = new ConsoleBoardVisualizer();

    private int[][] state = {
            {7,7,7,7,7,7,7,7,7,7,7,7,4,7,7,7,7},
            {7,7,7,7,7,7,7,7,7,7,7,4,4,7,7,7,7},
            {7,7,7,7,7,7,7,7,7,7,4,4,4,7,7,7,7},
            {7,7,7,7,7,7,7,7,7,4,4,4,4,7,7,7,7},
            {7,7,7,7,3,3,3,3,0,0,0,0,0,5,5,5,5},
            {7,7,7,7,3,3,3,0,0,0,0,0,0,5,5,5,7},
            {7,7,7,7,3,3,0,0,0,0,0,0,0,5,5,7,7},
            {7,7,7,7,3,0,0,0,0,0,0,0,0,5,7,7,7},
            {7,7,7,7,0,0,0,0,0,0,0,0,0,7,7,7,7},
            {7,7,7,2,0,0,0,0,0,0,0,0,6,7,7,7,7},
            {7,7,2,2,0,0,0,0,0,0,0,6,6,7,7,7,7},
            {7,2,2,2,0,0,0,0,0,0,6,6,6,7,7,7,7},
            {2,2,2,2,0,0,0,0,0,6,6,6,6,7,7,7,7},
            {7,7,7,7,1,1,1,1,7,7,7,7,7,7,7,7,7},
            {7,7,7,7,1,1,1,7,7,7,7,7,7,7,7,7,7},
            {7,7,7,7,1,1,7,7,7,7,7,7,7,7,7,7,7},
            {7,7,7,7,1,7,7,7,7,7,7,7,7,7,7,7,7},
    };

    public int[][] getState() {
        return state;
    }

    public void move(int y1, int x1, int y2, int x2) {
        state[y2][x2] = state[y1][x1];
        state[y1][x1] = 0;
    }

    public void print() {
        bv.showBoard(this);
        //prints the board state in a user-friendly format

        //probably will be replaced by a dedicated interface
        //to be able to visualize the board in different ways
    }

}
