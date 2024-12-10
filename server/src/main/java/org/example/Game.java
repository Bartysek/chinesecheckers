package org.example;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<PlayerInterface> players = new ArrayList<>();
    private int noPlayers = 0;
    private int playing = 0;
    private boolean inProgress = false;

    private int[][] board = {
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

    public void addPlayer(Player player) {
        synchronized (players) { //this is supposed to be used by a thread
            if (players.isEmpty()) {
                players.add(player);
                noPlayers = player.queryNumPlayers(); //it is supposed to lock adding new players here

            }

            playing++;
            if (playing == 1) {

            }
        }
    }

    private boolean validateMove() {
        // to be implemented in later versions, checks if a move is valid
        return true;
    }
}
