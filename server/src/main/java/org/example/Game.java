package org.example;

import entities.GameInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * class representing a single game.
 */
public class Game {
    private final List<PlayerInterface> players = new ArrayList<>();
    private int noPlayers;
    private int playing;
    private boolean inProgress;
    private AbstractRules gameRules;
    private GameInfo save;
    private boolean prepared;
    private boolean playback = false;

    /**
     * constructor. Sets up a new game.
     */
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
        for(PlayerInterface p : players) {
            p.closeSocket();
        }
        players.clear();
        prepared = false;
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
                    if(!prepared) {
                        noPlayers = player.queryNumPlayers();
                        gameRules = player.queryGameRules();
                        save = new GameInfo(gameRules.getRuleNum(), noPlayers);
                        assert gameRules != null;
                        gameRules.setupBoard(new Board(), noPlayers);
                    }
                    playing++; //it is supposed to lock adding new players here
                    // temp: test of bot
                    //players.add(new BotPlayer(new NaturalEngine(), playing));
                    //playing++;
                    //
                } else if (playing + 1 <= noPlayers) {
                    players.add(player);
                    playing++;
                }
                assert playing <= noPlayers; //if not, something is really wrong
                if (playing == noPlayers) {
                    if(!playback) {
                        GameDAO dao = Server.getInstance().getDao();
                        players.add(new GameRecorder(save, dao));
                    }
                    startGameLoop();
                }
            } catch (Exception e) { //if something is wrong, restart the game

                initializeGame();
            }
        }
    }

    /**
     * check if the game is full
     * @return if the game is full
     */
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
        Board board = gameRules.getBoard();
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
                            p.sendEndOfMove(currentActivePlayer);
                        }
                    }
                    if(moveStatus == 1 && gameRules.checkEndCon(currentActivePlayer)) {
                        for (PlayerInterface p : players) {
                            p.sendMessage("Player " + (gameRules.getWinner() + 1) + " won!");
                        }
                        initializeGame();
                    }

                } catch (IOException e) {
                    System.err.println("IO exception");
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

    public void setGameRules(AbstractRules rules) {
        gameRules = rules;
    }

    public void setNoPlayers(int number) {
        noPlayers = number;
    }

    public void setPrepared(boolean p) {
        prepared = p;
    }

    public void setPlayback(boolean p) {
        playback = p;
    }

    public void setSave(GameInfo save){
        this.save = save;
    }

    public List<PlayerInterface> getPlayers() {
        return players;
    }

    public void setPlaying(int playing) {
        this.playing = playing;
    }
}
