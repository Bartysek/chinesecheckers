package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<PlayerInterface> players = new ArrayList<>();
    private int noPlayers = 0;
    private int playing = 0;
    private boolean inProgress = false;

    private int[][] board = { //TODO this should be generated in a way that can be parametrized
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

    public void addPlayer(PlayerInterface player) {
        synchronized (players) { //this is supposed to be used by a thread
            if (players.isEmpty()) {
                players.add(player);
                noPlayers = player.queryNumPlayers();
                playing++; //it is supposed to lock adding new players here
            }
            else if (playing + 1 <= noPlayers) {
                players.add(player);
                playing++;
            }
            assert playing <= noPlayers; //if not, something is really wrong
            if (playing == noPlayers) {
                startGameLoop();
            }
        }
    }

    private boolean validateMove(byte[] move) {
        // to be implemented in later versions, checks if a move is valid
        return true;
    }

    public boolean isFull() {
        if(noPlayers == 0)
            return false;
        else
            return noPlayers <= playing;
    }

    private void startGameLoop() {
        inProgress = true;
        int currentActivePlayer = 0;
        players.getFirst().sendMessage("Zaczynasz!");
        while(inProgress){
            //getting move
            byte[] move = new byte[4];
            do {
                players.get(currentActivePlayer).sendTheirTurn();
                try {
                    move = players.get(currentActivePlayer).listen();
                } catch (IOException e) {
                    System.err.println("IO exception");
                }
            } while (!validateMove(move));
            //sending move
            for (PlayerInterface p : players) {
                p.sendMove(move[0], move[1], move[2], move[3]);
            }
            //advancing turn
            currentActivePlayer++;
            if (currentActivePlayer >= noPlayers)
                currentActivePlayer = 0;
        }
    }
}
