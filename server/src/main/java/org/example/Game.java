package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<PlayerInterface> players = new ArrayList<>();
    private int noPlayers;
    private int playing;
    private boolean inProgress;

    private Board board = new Board();

    Game() {
        initializeGame();
    }

    private void initializeGame() {
        inProgress = false;
        noPlayers = 0;
        playing = 0;
        players.clear();
    }

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
                    if (move.length != 4) {
                        System.out.println("A problem with getting a move from the client. Reseting the game.");
                        throw new IOException();
                    }
                } catch (IOException e) {
                    System.err.println("IO exception");
                    for (PlayerInterface p:players) {
                        p.closeSocket();
                        players.remove(p);
                    }
                    initializeGame();
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
