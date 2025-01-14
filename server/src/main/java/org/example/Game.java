package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<PlayerInterface> players = new ArrayList<>();
    private int noPlayers;
    private int playing;
    private boolean inProgress;
    private RulesInterface gameRules;

    private final Board board = new Board();

    Game() {
        initializeGame();
    }

    private void initializeGame() {
        inProgress = false;
        noPlayers = 0;
        playing = 0;
        players.clear();
    }

    public void addPlayer(final PlayerInterface player) {
        synchronized (players) { //this is supposed to be used by a thread
            player.sendMessage("You connected as player " + (playing + 1));
            if (players.isEmpty()) {
                players.add(player);
                noPlayers = player.queryNumPlayers();
                gameRules = player.queryGameRules();
                System.out.println("numplay: " + noPlayers + " gamemode: " + gameRules.toString());
                assert gameRules != null;
                gameRules.setBoard(board, noPlayers);
                playing++; //it is supposed to lock adding new players here
            } else if (playing + 1 <= noPlayers) {
                players.add(player);
                System.out.println("current: " + playing);
                playing++;
            }
            assert playing <= noPlayers; //if not, something is really wrong
            if (playing == noPlayers) {
                startGameLoop();
            }
        }
    }

    public boolean isFull() {
        if (noPlayers == 0) {
            return false;
        } else {
            return noPlayers <= playing;
        }
    }

    private void startGameLoop() {
        inProgress = true;
        int currentActivePlayer = 0;
        for(PlayerInterface p : players) {
            p.sendBoardState(board.getHexagonSide(), board.getState());
        }
        players.getFirst().sendMessage("You are going first!");
        while (inProgress) {
            //getting move
            byte[] move;
            int moveStatus = 0;
            do {
                System.out.println("sending turn to player " + currentActivePlayer);
                players.get(currentActivePlayer).sendTheirTurn();
                System.out.println("sent turn to player " + currentActivePlayer);
                try {
                    move = players.get(currentActivePlayer).listen();
                    if (move.length != 4) {
                        System.out.println("A problem with getting a move from the client. Resetting the game.");
                        throw new IOException();
                    }
                    System.out.println("Received move from player " + currentActivePlayer + ": " + move[0] + " " + move[1] + " " + move[2] + " " + move[3]);
                    moveStatus = gameRules.handleMove(move[0], move[1], move[2], move[3], currentActivePlayer);
                    if(moveStatus == 1 && gameRules.checkEndCon(currentActivePlayer)) {
                        for (PlayerInterface p : players) {
                            p.sendMessage("Player " + (gameRules.getWinner() + 1) + "won!");
                            initializeGame();
                        }
                    }
                    if(moveStatus >= 0) {
                        //sending move
                        int[] piece = gameRules.getNextRemovedPiece();
                        do {
                            for (PlayerInterface p : players) {
                                p.removePiece(piece);
                            }
                            piece = gameRules.getNextRemovedPiece();
                        } while(piece != null);

                        piece = gameRules.getNextAddedPiece();
                        do {
                            for (PlayerInterface p : players) {
                                p.addPiece(piece);
                            }
                            piece = gameRules.getNextAddedPiece();
                        } while(piece != null);

                        for (PlayerInterface p : players) {
                            p.sendEndOfMove();
                        }
                    }
                } catch (IOException e) {
                    System.err.println("IO exception");
                    for (PlayerInterface p : players) {
                        p.closeSocket();
                        players.remove(p);
                    }
                    initializeGame();
                }
            } while (moveStatus < 1);


            //advancing turn
            currentActivePlayer++;
            if (currentActivePlayer >= noPlayers) {
                currentActivePlayer = 0;
            }
        }
    }
}
