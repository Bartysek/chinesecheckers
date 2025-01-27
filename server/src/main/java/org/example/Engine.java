package org.example;

import java.util.ArrayList;

public interface Engine {
    ArrayList<int[]> planTurn(Board board, int numOfPlayers, int playerNum);
}
