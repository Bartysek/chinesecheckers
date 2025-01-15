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

    /**
     * set up a new game
     */
    private void initializeGame() {
        inProgress = false;
        noPlayers = 0;
        playing = 0;
        players.clear();
    }

    /**
     * add a player to the game, then ask them for game parameters if they are 1st or start the game if they are last
     * @param player
     */
    public void addPlayer(final PlayerInterface player) {
        synchronized (players) { //this is supposed to be used by a thread
            player.sendMessage("You connected as player " + (playing + 1));
            try {
                if (players.isEmpty()) {
                    players.add(player);
                    noPlayers = player.queryNumPlayers();
                    gameRules = player.queryGameRules();
                    assert gameRules != null;
                    gameRules.setBoard(board, noPlayers);
                    playing++; //it is supposed to lock adding new players here
                } else if (playing + 1 <= noPlayers) {
                    players.add(player);
                    playing++;
                }
                assert playing <= noPlayers; //if not, something is really wrong
                if (playing == noPlayers) {
                    startGameLoop();
                }
            } catch (Exception e) { //if something is wrong, restart the game
                for (PlayerInterface p : players) {
                    p.closeSocket();
                }
                initializeGame();
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

    /**
     * proper game: send the starting board state to every player,
     * then get moves from players and send them to every other player.
     * check for win condition upon every completed turn.
     */
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
            int moveStatus;
            do {
                try {
                    System.out.println("sending turn to player " + currentActivePlayer);
                    players.get(currentActivePlayer).sendTheirTurn();
                    move = players.get(currentActivePlayer).listen();
                    if (move.length != 4) {
                        System.out.println("A problem with getting a move from the client. Resetting the game.");
                        throw new IOException();
                    }
                    System.out.println("Received move from player " + currentActivePlayer + ": " + move[0] + " " + move[1] + " " + move[2] + " " + move[3]);
                    moveStatus = gameRules.handleMove(move[0], move[1], move[2], move[3], currentActivePlayer);
                    if(moveStatus == 1 && gameRules.checkEndCon(currentActivePlayer)) {
                        for (PlayerInterface p : players) {
                            p.sendMessage("Player " + (gameRules.getWinner() + 1) + " won!");
                        }
                        initializeGame();
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
                    }
                    initializeGame();
                    break;
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
