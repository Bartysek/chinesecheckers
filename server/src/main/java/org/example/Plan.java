package org.example;

import java.util.ArrayList;

public class Plan implements Cloneable {
    private ArrayList<int[]> moves;
    public int rating;
    public int rating2;

    Plan(int x, int y) {
        moves = new ArrayList<>();
        addMove(x, y);
    }

    Plan(Plan parent) {
        moves = new ArrayList<>();
        for (int[] move : parent.moves) {
            addMove(move[0], move[1]);
        }
    }

    public void addMove(int x, int y) {
        moves.add(new int[] {x, y} );
    }

    public int[] getFirstMove() { return moves.getFirst(); }
    public int[] getLastMove() {
        return moves.getLast();
    }

    public int getSize() {
        return moves.size();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public ArrayList<int[]> getAsArray() {
        return moves;
    }

    @Override
    public String toString() {
        String string = "";
        for (int[] move : moves) {
            string += move[0] + ", " + move[1] + " -> ";
        }
        string += "rating " + rating;
        return string;
    }

}
