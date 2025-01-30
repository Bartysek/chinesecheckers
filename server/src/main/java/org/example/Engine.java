package org.example;

import java.util.ArrayList;

public interface Engine {
    ArrayList<int[]> planTurn(Board board, int numOfPlayers, int playerNum);
    int evaluateRating(Plan plan);
    int[] getPieceOwnership();
    void createRules();
}
